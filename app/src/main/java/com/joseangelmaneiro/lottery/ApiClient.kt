package com.joseangelmaneiro.lottery

import com.google.gson.Gson
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request

class ApiClient {

    private val client = OkHttpClient()
    private val gson = Gson()

    fun getInfo(number: Int) : Either<Exception, NumberDetail> {
        val request = Request.Builder()
            .url("https://api.elpais.com/ws/LoteriaNavidadPremiados?n=$number")
            .build()
        val response = client.newCall(request).execute()
        return if (response.isSuccessful) {
            val bodyText = response.body().string()
            val result = gson.fromJson(bodyText.removePrefix("busqueda="), NumberDetail::class.java)
            Either.right(result)
        } else {
            Either.left(Exception())
        }
    }
}