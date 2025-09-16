package com.joseangelmaneiro.lottery.presentation

import com.joseangelmaneiro.lottery.LotteryType
import com.joseangelmaneiro.lottery.data.ApiClient
import com.joseangelmaneiro.lottery.domain.GetNumbersUseCase

class ViewModelFactory(
    private val lotteryType: LotteryType
) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(LotteryViewModel::class.java))
        return LotteryViewModel(lotteryType, GetNumbersUseCase(ApiClient(lotteryType))) as T
    }
}
