package com.joseangelmaneiro.lottery.view

import com.joseangelmaneiro.lottery.model.NumberItem

interface NumbersView {
    fun showNumbers(numbers: List<NumberItem>)
}
