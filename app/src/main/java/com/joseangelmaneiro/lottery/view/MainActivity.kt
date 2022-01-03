package com.joseangelmaneiro.lottery.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.joseangelmaneiro.lottery.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    viewPager.adapter = PageAdapter(supportFragmentManager)
    tabLayout.setupWithViewPager(viewPager)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    val inflater: MenuInflater = menuInflater
    inflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action_delete -> {
        val numbersListener = getNumbersListener()
        showDeleteAllTicketsDialog(numbersListener.getLotteryType()) {
          numbersListener.deleteAll()
        }
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  private fun getNumbersListener(): NumbersListener {
    return viewPager.adapter?.instantiateItem(viewPager, viewPager.currentItem) as NumbersListener
  }

}