package com.joseangelmaneiro.lottery.model

import com.google.gson.annotations.SerializedName

data class NumberDetail (
    @SerializedName("numero")
    val number: Int,
    @SerializedName("premio")
    val prize: Int,
    val timestamp: Int,
    val status: Int,
    val error: Int
)