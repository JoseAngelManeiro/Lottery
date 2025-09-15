package com.joseangelmaneiro.lottery.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.joseangelmaneiro.lottery.Injector
import com.joseangelmaneiro.lottery.LotteryType
import com.joseangelmaneiro.lottery.R
import com.joseangelmaneiro.lottery.model.NumberItem
import com.joseangelmaneiro.lottery.model.Ticket
import com.joseangelmaneiro.lottery.task.DeleteAllTicketsTask
import com.joseangelmaneiro.lottery.task.DeleteTicketTask
import com.joseangelmaneiro.lottery.task.GetNumbersTask
import com.joseangelmaneiro.lottery.task.SaveTicketTask

class NumbersFragment : Fragment(), NumbersView, ActivityButtonsListener {

    companion object {
        private const val LOTTERY_TYPE = "lottery_type"

        fun newInstance(lotteryType: LotteryType) = NumbersFragment().apply {
            arguments = Bundle().apply {
                putSerializable(LOTTERY_TYPE, lotteryType)
            }
        }
    }

    private var numbersRecyclerView: RecyclerView? = null

    private var saveTicketTask: SaveTicketTask? = null
    private var deleteTicketTask: DeleteTicketTask? = null
    private var getNumbersTask: GetNumbersTask? = null
    private var deleteAllTicketsTask: DeleteAllTicketsTask? = null

    private lateinit var lotteryType: LotteryType

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

        lotteryType = arguments?.get(LOTTERY_TYPE) as LotteryType

        numbersRecyclerView = view.findViewById(R.id.recycler_view)

        activity?.applicationContext?.let { context ->
            val injector = Injector(context, lotteryType)
            saveTicketTask = injector.getSaveTicketTask(this)
            deleteTicketTask = injector.getDeleteTicketTask(this)
            getNumbersTask = injector.getGetNumbersTask(this)
            deleteAllTicketsTask = injector.getDeleteAllTicketsTask(this)

            loadNumbers()
        }
    }

    private fun loadNumbers() {
        getNumbersTask?.invoke(Unit)
    }

    override fun showNumbers(numbers: List<NumberItem>) {
        (requireActivity() as? TabsHost)?.updateTabCount(lotteryType, numbers.size)

        numbersRecyclerView?.adapter = NumbersAdapter(numbers.sortByLotteryType()) {
            activity?.apply {
                val ticket = Ticket(it.number)
                showTicketInfoDialog(ticket) { deleteTicketTask?.invoke(ticket) }
            }
        }

        if (syncButtonClicked) {
            Toast.makeText(requireActivity(), "Información actualizada", Toast.LENGTH_SHORT).show()
            syncButtonClicked = false
        }
    }

    override fun showError(exception: Exception) {
        activity?.apply { showErrorDialog { loadNumbers() } }
    }

    override fun refreshNumbers() {
        loadNumbers()
    }

    override fun onSyncButtonClick() {
        syncButtonClicked = true
        loadNumbers()
    }

    override fun onDeleteButtonClick() {
        activity?.apply {
            showDeleteAllTicketsDialog(lotteryType) { deleteAllTicketsTask?.invoke(Unit) }
        }
    }

    override fun onAddButtonClick() {
        activity?.apply { showAddTicketDialog { saveTicketTask?.invoke(it) } }
    }

    override fun onDestroyView() {
        numbersRecyclerView = null
        saveTicketTask = null
        deleteTicketTask = null
        getNumbersTask = null
        deleteAllTicketsTask = null
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
