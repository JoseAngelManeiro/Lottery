package com.joseangelmaneiro.lottery.presentation.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.joseangelmaneiro.lottery.LotteryType
import com.joseangelmaneiro.lottery.R
import com.joseangelmaneiro.lottery.presentation.ActivityButtonsListener
import com.joseangelmaneiro.lottery.presentation.adapter.PageAdapter
import com.joseangelmaneiro.lottery.presentation.TabsHost

class MainActivity : AppCompatActivity(), TabsHost {

  private lateinit var tabLayout: TabLayout
  private lateinit var viewPager: ViewPager
  private lateinit var fabAddTicket: FloatingActionButton
  private lateinit var viewPagerAdapter: PageAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    tabLayout = findViewById(R.id.tabLayout)
    viewPager = findViewById(R.id.viewPager)
    fabAddTicket = findViewById(R.id.fab)

    viewPagerAdapter = PageAdapter(supportFragmentManager)
    viewPager.adapter = viewPagerAdapter
    tabLayout.setupWithViewPager(viewPager)

    setUpFabButton()
  }

  private fun setUpFabButton() {
    fabAddTicket.setOnClickListener {
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

  override fun updateTabCount(lotteryType: LotteryType, count: Int) {
    val title = lotteryType.tabTitle + (if(count > 0) " ($count)" else "")
    tabLayout.getTabAt(lotteryType.tabIndex)?.text = title
  }


  private fun getNumbersListener(): ActivityButtonsListener {
    return viewPagerAdapter.getItem(viewPager.currentItem) as ActivityButtonsListener
  }
}
