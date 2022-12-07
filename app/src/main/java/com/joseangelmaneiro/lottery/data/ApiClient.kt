package com.joseangelmaneiro.lottery.data

import com.google.gson.Gson
import com.joseangelmaneiro.lottery.Either
import com.joseangelmaneiro.lottery.LotteryType
import com.joseangelmaneiro.lottery.model.NumberDetail
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request

class ApiClient(lotteryType: LotteryType) {

    private val client = OkHttpClient()
    private val gson = Gson()

    private val urlPrefix = if (lotteryType == LotteryType.NAVIDAD) {
        "https://api.elpais.com/ws/LoteriaNavidadPremiados?n="
    } else {
        "http://api.elpais.com/ws/LoteriaNinoPremiados?n="
    }

    fun getInfo(number: Int) : Either<Exception, NumberDetail> {
        val request = Request.Builder()
            .url(urlPrefix + number)
            .build()
        return try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val bodyText = response.body().string()
                val result = gson.fromJson(
                    bodyText.removePrefix("busqueda="),
                    NumberDetail::class.java
                )
                Either.right(result)
            } else {
                Either.left(Exception())
            }
        } catch (e: Exception) {
            Either.left(Exception())
        }
    }
}