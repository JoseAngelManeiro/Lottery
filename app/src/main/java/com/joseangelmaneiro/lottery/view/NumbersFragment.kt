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
import com.joseangelmaneiro.lottery.task.DeleteTicketTask
import com.joseangelmaneiro.lottery.task.GetNumbersTask
import com.joseangelmaneiro.lottery.task.SaveTicketTask
import kotlinx.android.synthetic.main.fragment_numbers.*

class NumbersFragment : Fragment(), NumbersView {

    private lateinit var saveTicketTask: SaveTicketTask
    private lateinit var deleteTicketTask: DeleteTicketTask
    private lateinit var getNumbersTask: GetNumbersTask

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_numbers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.applicationContext?.let { context ->
            val injector = Injector(context, LotteryType.NAVIDAD)
            saveTicketTask = injector.getSaveTicketTask(this)
            deleteTicketTask = injector.getDeleteTicketTask(this)
            getNumbersTask = injector.getGetNumbersTask(this)

            setUpFabButton()

            loadNumbers()
        }
    }

    private fun setUpFabButton() {
        fab.setOnClickListener {
            activity?.apply { showAddTicketDialog { saveTicketTask.invoke(it) } }
        }
    }

    private fun loadNumbers() {
        getNumbersTask(Unit)
    }

    override fun loading() {
        progress_bar.visibility = View.VISIBLE
        fab.isEnabled = false
    }

    override fun showNumbers(numbers: List<NumberItem>) {
        recycler_view.adapter = NumbersAdapter(
            items = numbers,
            onItemClickListener = {
                activity?.apply {
                    val ticket = Ticket(it.number, it.eurosBet)
                    showTicketInfoDialog(ticket) { deleteTicketTask(ticket) }
                }
            }
        )
        progress_bar.visibility = View.GONE
        fab.isEnabled = true
    }

    override fun showError(exception: Exception) {
        progress_bar.visibility = View.GONE
        activity?.apply { showErrorDialog { loadNumbers() } }
    }

    override fun refreshNumbers() {
        loadNumbers()
    }
}