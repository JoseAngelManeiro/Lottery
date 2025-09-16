package com.joseangelmaneiro.lottery.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.joseangelmaneiro.lottery.LotteryType
import com.joseangelmaneiro.lottery.R
import com.joseangelmaneiro.lottery.model.NumberItem
import com.joseangelmaneiro.lottery.model.Ticket
import kotlinx.coroutines.flow.collect

class NumbersFragment : Fragment(), NumbersView, ActivityButtonsListener {

    companion object {
        private const val LOTTERY_TYPE = "lottery_type"

        fun newInstance(lotteryType: LotteryType) = NumbersFragment().apply {
            arguments = Bundle().apply {
                putSerializable(LOTTERY_TYPE, lotteryType)
            }
        }
    }

    private val lotteryType: LotteryType by lazy {
        arguments?.get(LOTTERY_TYPE) as LotteryType
    }

    private val viewModel by viewModels<LotteryViewModel> {
        ViewModelFactory(lotteryType)
    }

    private var numbersRecyclerView: RecyclerView? = null

    private var syncButtonClicked: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_numbers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        numbersRecyclerView = view.findViewById(R.id.recycler_view)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.numberItems.collect { numbers ->
                showNumbers(numbers)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.loading.collect { visible ->
                progressBar.isVisible = visible

                if (syncButtonClicked && !visible) {
                    Toast.makeText(
                        requireActivity(),
                        "Información actualizada",
                        Toast.LENGTH_SHORT
                    ).show()
                    syncButtonClicked = false
                }
            }
        }
    }

    override fun showNumbers(numbers: List<NumberItem>) {
        (requireActivity() as? TabsHost)?.updateTabCount(lotteryType, numbers.size)

        numbersRecyclerView?.adapter = NumbersAdapter(numbers.sortByLotteryType()) {
            activity?.apply {
                val ticket = Ticket(it.number)
                showTicketInfoDialog(ticket) {
                    viewModel.removeNumber(ticket.number)
                }
            }
        }


    }

    override fun onSyncButtonClick() {
        syncButtonClicked = true
        viewModel.refreshWinnersMapping()
    }

    override fun onDeleteButtonClick() { // TODO: Remove all numbers
        activity?.apply {
            showDeleteAllTicketsDialog(lotteryType) {
                //deleteAllTicketsTask?.invoke(Unit)
            }
        }
    }

    override fun onAddButtonClick() {
        activity?.apply {
            showAddTicketDialog {
                viewModel.addNumber(it.number)
            }
        }
    }

    override fun onDestroyView() {
        numbersRecyclerView = null
        super.onDestroyView()
    }

    /**
     * If LotteryType is "NAVIDAD" we'll display the list of numbers sorted in ascending order
     * by the full numeric value.
     * If LotteryType is "EL_NINO" we'll display the list of numbers sorted in ascending order
     * the last digit of each number.
     */
    private fun List<NumberItem>.sortByLotteryType(): List<NumberItem> {
        return if (lotteryType == LotteryType.NAVIDAD) {
            sortedBy { it.number.toInt() }
        } else {
            sortedBy { it.number.last() }
        }
    }
}
