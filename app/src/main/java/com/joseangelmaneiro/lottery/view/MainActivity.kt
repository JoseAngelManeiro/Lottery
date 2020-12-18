package com.joseangelmaneiro.lottery.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.joseangelmaneiro.lottery.model.NumberItem
import com.joseangelmaneiro.lottery.R
import com.joseangelmaneiro.lottery.data.ApiClient
import com.joseangelmaneiro.lottery.data.LocalDataSource
import com.joseangelmaneiro.lottery.model.Ticket
import com.joseangelmaneiro.lottery.task.GetNumbersTask
import com.joseangelmaneiro.lottery.task.SaveTicketTask
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity(), MainView {

  private lateinit var apiClient: ApiClient
  private lateinit var localDataSource: LocalDataSource

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    apiClient = ApiClient()
    localDataSource = LocalDataSource(this)

    fab.setOnClickListener {
      showAddTicketDialog { saveTicket(it) }
    }
  }

  override fun loading() {
    progress_bar.visibility = View.VISIBLE
  }

  override fun showNumbers(numbers: List<NumberItem>) {
    progress_bar.visibility = View.GONE
    numbers_text_view.text = numbers.toString()
  }

  override fun showError(exception: Exception) {
    progress_bar.visibility = View.GONE
    Log.i("Numbers", exception.toString())
  }

  override fun refreshNumbers() {
    loadNumbers()
  }

  private fun loadNumbers() {
    GetNumbersTask(apiClient, localDataSource, this).execute()
  }

  private fun saveTicket(ticket: Ticket) {
    SaveTicketTask(localDataSource, this).execute(ticket)
  }
}