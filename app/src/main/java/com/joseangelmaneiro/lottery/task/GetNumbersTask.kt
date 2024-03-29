package com.joseangelmaneiro.lottery.task

import com.joseangelmaneiro.lottery.Either
import com.joseangelmaneiro.lottery.view.NumbersView
import com.joseangelmaneiro.lottery.model.NumberItem
import com.joseangelmaneiro.lottery.data.ApiClient
import com.joseangelmaneiro.lottery.data.LocalDataSource
import com.joseangelmaneiro.lottery.executor.Task
import com.joseangelmaneiro.lottery.executor.TaskExecutor
import java.lang.ref.WeakReference

class GetNumbersTask(
    private val apiClient: ApiClient,
    private val localDataSource: LocalDataSource,
    view: NumbersView,
    taskExecutor: TaskExecutor
): Task<Unit, List<NumberItem>>(taskExecutor) {

    companion object {
        const val DEFAULT_TICKET_PRICE = 20
    }

    private val view = WeakReference(view)

    override fun onPreExecute() {
        view.get()?.loading()
    }

    override fun doInBackground(request: Unit): Either<Exception, List<NumberItem>> {
        val ticketsSaved = localDataSource.getTickets()
        if (ticketsSaved.isEmpty()) {
            return Either.right(emptyList())
        } else {
            val result = mutableListOf<NumberItem>()
            ticketsSaved.forEach { ticket ->
                val apiResponse = apiClient.getInfo(ticket.number.toInt())
                if (apiResponse.isLeft) {
                    val numberItem = NumberItem(
                        ticket.number,
                        ticket.eurosBet,
                        0,
                        -1, // Status = -1 -> No info (Service error)
                        0
                    )
                    result.add(numberItem)
                } else {
                    val numberDetail = apiResponse.rightValue
                    val numberItem = NumberItem(
                        ticket.number,
                        ticket.eurosBet,
                        getTotalPrize(numberDetail.prize, ticket.eurosBet),
                        numberDetail.status,
                        numberDetail.timestamp
                    )
                    result.add(numberItem)
                }
            }
            return Either.right(result)
        }
    }

    override fun onPostExecute(response: Either<Exception, List<NumberItem>>) {
        view.get()?.run {
            response.fold(
                leftOp = { showError(response.leftValue) },
                rightOp = { showNumbers(response.rightValue) }
            )
        }
    }

    private fun getTotalPrize(prizeByDefaultTicket: Int, eurosBet: Int): Int{
        return (prizeByDefaultTicket * eurosBet) / DEFAULT_TICKET_PRICE
    }
}