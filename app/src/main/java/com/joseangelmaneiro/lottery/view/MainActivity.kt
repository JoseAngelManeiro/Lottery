package com.joseangelmaneiro.lottery.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.joseangelmaneiro.lottery.model.NumberItem
import com.joseangelmaneiro.lottery.R
import com.joseangelmaneiro.lottery.model.Ticket
import com.joseangelmaneiro.lottery.task.DeleteTicketTask
import com.joseangelmaneiro.lottery.task.GetNumbersTask
import com.joseangelmaneiro.lottery.task.SaveTicketTask
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity(), MainView {

  private lateinit var injector: Injector

  private lateinit var saveTicketTask: SaveTicketTask
  private lateinit var deleteTicketTask: DeleteTicketTask
  private lateinit var getNumbersTask: GetNumbersTask

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    injector = Injector(this)
    saveTicketTask = injector.getSaveTicketTask(this)
    deleteTicketTask = injector.getDeleteTicketTask(this)
    getNumbersTask = injector.getGetNumbersTask(this)

    setUpFabButton()
  }

  override fun onResume() {
    super.onResume()
    loadNumbers()
  }

  private fun setUpFabButton() {
    fab.setOnClickListener {
      showAddTicketDialog { saveTicket(it) }
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
      onItemClickListener = { onNumberItemClicked(it) }
    )
    progress_bar.visibility = View.GONE
    fab.isEnabled = true
  }

  override fun showError(exception: Exception) {
    progress_bar.visibility = View.GONE
    showErrorDialog { loadNumbers() }
  }

  override fun refreshNumbers() {
    loadNumbers()
  }

  private fun saveTicket(ticket: Ticket) {
    saveTicketTask(ticket)
  }

  private fun onNumberItemClicked(numberItem: NumberItem) {
    val ticket = Ticket(numberItem.number, numberItem.eurosBet)
    showTicketInfoDialog(ticket) {
      deleteTicketTask(ticket)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    val inflater: MenuInflater = menuInflater
    inflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action_refresh -> {
        loadNumbers()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }
}