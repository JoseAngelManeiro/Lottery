package com.joseangelmaneiro.lottery.presentation

import com.joseangelmaneiro.lottery.model.NumberItem

interface NumbersView {
    fun showNumbers(numbers: List<NumberItem>)
}
