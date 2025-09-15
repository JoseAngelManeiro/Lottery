package com.joseangelmaneiro.lottery.view

import com.joseangelmaneiro.lottery.LotteryType

interface TabsHost {
    fun updateTabCount(lotteryType: LotteryType, count: Int)
}
