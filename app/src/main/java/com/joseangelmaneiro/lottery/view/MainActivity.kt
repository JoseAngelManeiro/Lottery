package com.joseangelmaneiro.lottery.view

import android.os.Bundle
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
}