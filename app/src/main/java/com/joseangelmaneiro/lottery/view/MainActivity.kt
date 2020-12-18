package com.joseangelmaneiro.lottery.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.joseangelmaneiro.lottery.Injector
import com.joseangelmaneiro.lottery.model.NumberItem
import com.joseangelmaneiro.lottery.R
import com.joseangelmaneiro.lottery.task.GetNumbersTask
import com.joseangelmaneiro.lottery.task.SaveTicketTask
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity(), MainView {

  private lateinit var getNumbersTask: GetNumbersTask
  private lateinit var saveTicketTask: SaveTicketTask

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val injector = Injector(this)
    getNumbersTask = injector.provideGetNumbersTask(this)
    saveTicketTask = injector.provideSaveTicketTask(this)
  }

  private fun loadNumbers() {
    getNumbersTask.execute()
  }

  override fun loading() {
    progress_bar.visibility = View.VISIBLE
  }

  override fun showNumbers(numbers: List<NumberItem>) {
    progress_bar.visibility = View.GONE
    Log.i("Numbers", numbers.toString())
  }

  override fun showError(exception: Exception) {
    progress_bar.visibility = View.GONE
    Log.i("Numbers", exception.toString())
  }

  override fun refreshNumbers() {
    loadNumbers()
  }
}