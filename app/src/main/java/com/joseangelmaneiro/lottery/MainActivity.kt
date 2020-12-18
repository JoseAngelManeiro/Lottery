package com.joseangelmaneiro.lottery

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

  private val apiClient = ApiClient()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    Task().execute()
  }

  inner class Task: AsyncTask<Void, Void, Either<Exception, NumberDetail>>() {
    override fun doInBackground(vararg p0: Void?): Either<Exception, NumberDetail> {
      return apiClient.getInfo(26590)
    }

    override fun onPostExecute(result: Either<Exception, NumberDetail>) {
      if (result.isRight) Log.i("Number", result.rightValue.toString())
    }
  }

  /*
    val defaultLotteryTicketPrice = 20
  * val totalPrize = (NumberDetail.premio * EurosBet) / defaultLotteryTicketPrice
  * */

}