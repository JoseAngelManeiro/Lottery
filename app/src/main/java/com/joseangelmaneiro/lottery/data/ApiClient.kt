package com.joseangelmaneiro.lottery.data

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.joseangelmaneiro.lottery.Either
import com.joseangelmaneiro.lottery.LotteryType
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request

class ApiClient(private val lotteryType: LotteryType) {

    private val client = OkHttpClient()

    fun getNumbers(): Either<Exception, Map<Int, Int>>{
        val request = Request.Builder()
            .url(lotteryType.apiUrl)
            .build()
        return try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val bodyText = response.body().string()
                val jsonObject = Gson().fromJson(bodyText, JsonObject::class.java)

                val map = jsonObject.entrySet()
                    .filter { it.key.all { ch -> ch.isDigit() } } // skip "status", keep only numeric keys
                    .associate { it.key.toInt() to it.value.asInt / 10 }
                Either.right(map)
            } else {
                Either.left(Exception())
            }
        } catch (_: Exception) {
            Either.left(Exception())
        }
    }
}
