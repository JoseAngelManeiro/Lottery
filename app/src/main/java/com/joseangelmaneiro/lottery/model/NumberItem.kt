package com.joseangelmaneiro.lottery.model

data class NumberItem(
    val number: String,
    val prize: Prize
)

enum class PrizeStatus {
    NoInfo,
    NonWinning,
    Winning
}

data class Prize(
    val status: PrizeStatus,
    val value: Int = 0 // No prize by default
)
