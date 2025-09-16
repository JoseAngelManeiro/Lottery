package com.joseangelmaneiro.lottery.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.joseangelmaneiro.lottery.Either
import com.joseangelmaneiro.lottery.LotteryType
import com.joseangelmaneiro.lottery.data.firestore.FirestoreRepo
import com.joseangelmaneiro.lottery.model.NumberItem
import com.joseangelmaneiro.lottery.domain.GetNumbersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class LotteryViewModel(
    private val lotteryType: LotteryType,
    private val getNumbersUseCase: GetNumbersUseCase
) : ViewModel() {

    // Raw numbers from Firestore (Navidad OR ElNino)
    private val _numbersRaw = MutableStateFlow<List<String>>(emptyList())
    // Trigger to force re-map (e.g., pull-to-refresh)
    private val refreshTrigger = MutableStateFlow(0)

    // UI side channels
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    // Final mapped list for the screen
    val numberItems: StateFlow<List<NumberItem>> =
        combine(_numbersRaw, refreshTrigger) { list, _ -> list }
            .mapLatest { list ->
                _loading.value = true
                try {
                    // Run use case off the main thread
                    val result: Either<Exception, List<NumberItem>> =
                        withContext(Dispatchers.IO) {
                            getNumbersUseCase(list)
                        }

                    if (result.isLeft) {
                        emptyList()
                    } else {
                        result.rightValue
                    }
                } finally {
                    _loading.value = false
                }
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private var reg: ListenerRegistration? = null

    init {
        viewModelScope.launch {
            // Create doc if missing
            FirestoreRepo.init()

            reg = FirestoreRepo.listenNumbers(
                onUpdate = { data ->
                    _numbersRaw.value = when(lotteryType) {
                        LotteryType.NAVIDAD -> data.navidad
                        LotteryType.EL_NINO -> data.elNino
                    }
                }
            )
        }
    }

    fun addNumber(number: String) {
        viewModelScope.launch {
            FirestoreRepo.addNumber(lotteryType, number)
        }
    }

    fun removeNumber(number: String) {
        viewModelScope.launch {
            FirestoreRepo.removeNumber(lotteryType, number)
        }
    }

    fun removeAllNumbers() {
        viewModelScope.launch {
            FirestoreRepo.removeAllNumbers(lotteryType)
        }
    }

    /** Re-run GetNumbers() with the current raw list (e.g., user taps "Refresh"). */
    fun refreshWinnersMapping() { refreshTrigger.value++ }

    override fun onCleared() {
        reg?.remove()
        reg = null
        super.onCleared()
    }
}

