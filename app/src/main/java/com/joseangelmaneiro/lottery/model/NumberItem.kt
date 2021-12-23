package com.joseangelmaneiro.lottery.model

data class NumberItem(
    val number: String,
    val eurosBet: Int,
    val prize: Int,
    val status: Int,
    val timestamp: Int
)