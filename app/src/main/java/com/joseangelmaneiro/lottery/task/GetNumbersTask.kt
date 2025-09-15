package com.joseangelmaneiro.lottery.task

import com.joseangelmaneiro.lottery.Either
import com.joseangelmaneiro.lottery.view.NumbersView
import com.joseangelmaneiro.lottery.model.NumberItem
import com.joseangelmaneiro.lottery.data.ApiClient
import com.joseangelmaneiro.lottery.data.LocalDataSource
import com.joseangelmaneiro.lottery.executor.Task
import com.joseangelmaneiro.lottery.executor.TaskExecutor
import com.joseangelmaneiro.lottery.model.PrizeStatus
import com.joseangelmaneiro.lottery.model.Prize
import java.lang.ref.WeakReference

class GetNumbersTask(
    private val apiClient: ApiClient,
    private val localDataSource: LocalDataSource,
    view: NumbersView,
    taskExecutor: TaskExecutor
): Task<Unit, List<NumberItem>>(taskExecutor) {

    private val view = WeakReference(view)

    override fun onPreExecute() { }

    override fun doInBackground(request: Unit): Either<Exception, List<NumberItem>> {
        val ticketsSaved = localDataSource.getTickets()
        if (ticketsSaved.isEmpty()) {
            return Either.right(emptyList())
        } else {
            val apiResponse = apiClient.getNumbers()
            if (apiResponse.isLeft) {
                return Either.left(apiResponse.leftValue)
            } else {
                val winningNumbersMap = apiResponse.rightValue
                val result = mutableListOf<NumberItem>()
                ticketsSaved.forEach { ticket ->
                    val prize = winningNumbersMap.getPrize(ticket.number)
                    val numberItem = NumberItem(
                        number = ticket.number,
                        prize = prize
                    )
                    result.add(numberItem)
                }
                return Either.right(result)
            }

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

    private fun Map<Int, Int>.getPrize(ticketNumber: String): Prize {
        val winningNumber = this[ticketNumber.toInt()]
        return when {
            this.isEmpty() -> Prize(PrizeStatus.NoInfo)
            winningNumber == null -> Prize(PrizeStatus.NonWinning)
            else -> Prize(PrizeStatus.Winning, winningNumber)
        }
    }
}
