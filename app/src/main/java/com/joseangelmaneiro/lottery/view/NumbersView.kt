package com.joseangelmaneiro.lottery.view

import com.joseangelmaneiro.lottery.model.NumberItem

interface NumbersView {
    fun loading()
    fun showNumbers(numbers: List<NumberItem>)
    fun showError(exception: Exception)
    fun refreshNumbers()
}