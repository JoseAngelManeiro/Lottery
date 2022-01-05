package com.joseangelmaneiro.lottery.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.joseangelmaneiro.lottery.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  private lateinit var viewPagerAdapter: PageAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    viewPagerAdapter = PageAdapter(supportFragmentManager)
    viewPager.adapter = viewPagerAdapter
    tabLayout.setupWithViewPager(viewPager)

    setUpFabButton()
  }

  private fun setUpFabButton() {
    fab.setOnClickListener {
      getNumbersListener().onAddButtonClick()
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    val inflater: MenuInflater = menuInflater
    inflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action_delete -> {
        getNumbersListener().onDeleteButtonClick()
        true
      }
      R.id.action_sync -> {
        getNumbersListener().onSyncButtonClick()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  private fun getNumbersListener(): ActivityButtonsListener {
    return viewPagerAdapter.getItem(viewPager.currentItem) as ActivityButtonsListener
  }
}