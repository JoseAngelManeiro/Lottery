package com.joseangelmaneiro.lottery.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.joseangelmaneiro.lottery.LotteryType

class PageAdapter(fm:FragmentManager) : FragmentPagerAdapter(fm) {

    private val navidadNumbersFragment: NumbersFragment by lazy {
        NumbersFragment.newInstance(LotteryType.NAVIDAD)
    }
    private val elNinoNumbersFragment: NumbersFragment by lazy {
        NumbersFragment.newInstance(LotteryType.EL_NINO)
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> navidadNumbersFragment
            else -> elNinoNumbersFragment
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "NAVIDAD"
            else -> "EL NIÑO"
        }
    }

}
