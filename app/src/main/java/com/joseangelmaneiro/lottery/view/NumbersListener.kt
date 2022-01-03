package com.joseangelmaneiro.lottery.view

import com.joseangelmaneiro.lottery.LotteryType

interface NumbersListener {
    fun getLotteryType(): LotteryType
    fun deleteAll()
}