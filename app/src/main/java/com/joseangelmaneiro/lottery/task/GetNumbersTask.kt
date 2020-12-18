package com.joseangelmaneiro.lottery.task

import android.os.AsyncTask
import com.joseangelmaneiro.lottery.Either
import com.joseangelmaneiro.lottery.view.MainView
import com.joseangelmaneiro.lottery.model.NumberItem
import com.joseangelmaneiro.lottery.data.ApiClient
import com.joseangelmaneiro.lottery.data.LocalDataSource
import java.lang.ref.WeakReference

class GetNumbersTask(
    private val apiClient: ApiClient,
    private val localDataSource: LocalDataSource,
    view: MainView
): AsyncTask<Void, Void, Either<Exception, List<NumberItem>>>() {

    companion object {
        const val DEFAULT_TICKET_PRICE = 20
    }

    private val view = WeakReference(view)

    override fun onPreExecute() {
        view.get()?.loading()
    }

    override fun doInBackground(vararg p0: Void?): Either<Exception, List<NumberItem>> {
        val ticketsSaved = localDataSource.getTickets()
        if (ticketsSaved.isEmpty()) {
            return Either.right(emptyList())
        } else {
            val result = mutableListOf<NumberItem>()
            ticketsSaved.forEach { ticket ->
                val numberDetail = apiClient.getInfo(ticket.number.toInt())
                if (numberDetail.isLeft) {
                    return Either.left(numberDetail.leftValue)
                } else {
                    val numberItem = NumberItem(
                        ticket.number,
                        ticket.eurosBet,
                        getTotalPrize(numberDetail.rightValue.prize, ticket.eurosBet)
                    )
                    result.add(numberItem)
                }
            }
            return Either.right(result)
        }
    }

    override fun onPostExecute(result: Either<Exception, List<NumberItem>>) {
        view.get()?.run {
            result.fold(
                leftOp = { showError(result.leftValue) },
                rightOp = { showNumbers(result.rightValue) }
            )
        }
    }

    private fun getTotalPrize(prizeByDefaultTicket: Int, eurosBet: Int): Int{
        return (prizeByDefaultTicket * eurosBet) / DEFAULT_TICKET_PRICE
    }
}