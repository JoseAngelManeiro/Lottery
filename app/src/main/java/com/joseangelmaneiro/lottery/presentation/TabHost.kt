package com.joseangelmaneiro.lottery.presentation

import com.joseangelmaneiro.lottery.LotteryType

interface TabsHost {
    fun updateTabCount(lotteryType: LotteryType, count: Int)
}
