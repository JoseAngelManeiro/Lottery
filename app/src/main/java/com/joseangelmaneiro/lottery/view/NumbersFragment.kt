package com.joseangelmaneiro.lottery.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.joseangelmaneiro.lottery.Injector
import com.joseangelmaneiro.lottery.LotteryType
import com.joseangelmaneiro.lottery.R
import com.joseangelmaneiro.lottery.model.NumberItem
import com.joseangelmaneiro.lottery.model.Ticket
import com.joseangelmaneiro.lottery.task.DeleteAllTicketsTask
import com.joseangelmaneiro.lottery.task.DeleteTicketTask
import com.joseangelmaneiro.lottery.task.GetNumbersTask
import com.joseangelmaneiro.lottery.task.SaveTicketTask
import kotlinx.android.synthetic.main.fragment_numbers.*

class NumbersFragment : Fragment(), NumbersView, ActivityButtonsListener {

    companion object {
        private const val LOTTERY_TYPE = "lottery_type"

        fun newInstance(lotteryType: LotteryType) = NumbersFragment().apply {
            arguments = Bundle().apply {
                putSerializable(LOTTERY_TYPE, lotteryType)
            }
        }
    }

    private lateinit var saveTicketTask: SaveTicketTask
    private lateinit var deleteTicketTask: DeleteTicketTask
    private lateinit var getNumbersTask: GetNumbersTask
    private lateinit var deleteAllTicketsTask: DeleteAllTicketsTask

    private lateinit var lotteryType: LotteryType

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
        getNumbersTask(Unit)
    }

    override fun loading() {
        swipe_refresh.isRefreshing = true
    }

    override fun showNumbers(numbers: List<NumberItem>) {
        swipe_refresh.setOnRefreshListener {
            loadNumbers()
        }
        recycler_view.adapter = NumbersAdapter(items = numbers, lotteryType = lotteryType) {
            activity?.apply {
                val ticket = Ticket(it.number, it.eurosBet)
                showTicketInfoDialog(ticket) { deleteTicketTask(ticket) }
            }
        }
        swipe_refresh.isRefreshing = false
    }

    override fun showError(exception: Exception) {
        swipe_refresh.isRefreshing = false
        activity?.apply { showErrorDialog { loadNumbers() } }
    }

    override fun refreshNumbers() {
        loadNumbers()
    }

    override fun onSyncButtonClick() {
        loadNumbers()
    }

    override fun onDeleteButtonClick() {
        activity?.apply {
            showDeleteAllTicketsDialog(lotteryType) { deleteAllTicketsTask.invoke(Unit) }
        }
    }

    override fun onAddButtonClick() {
        activity?.apply { showAddTicketDialog { saveTicketTask.invoke(it) } }
    }
}