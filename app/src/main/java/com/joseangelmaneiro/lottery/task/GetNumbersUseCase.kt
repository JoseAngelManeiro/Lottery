package com.joseangelmaneiro.lottery.task

import com.joseangelmaneiro.lottery.Either
import com.joseangelmaneiro.lottery.model.NumberItem
import com.joseangelmaneiro.lottery.data.ApiClient
import com.joseangelmaneiro.lottery.model.PrizeStatus
import com.joseangelmaneiro.lottery.model.Prize

class GetNumbersUseCase(private val apiClient: ApiClient) {

    operator fun invoke(numbers: List<String>): Either<Exception, List<NumberItem>> {
        val apiResponse = apiClient.getNumbers()
        if (apiResponse.isLeft) {
            return Either.left(apiResponse.leftValue)
        } else {
            val winningNumbersMap = apiResponse.rightValue
            val result = mutableListOf<NumberItem>()
            numbers.forEach { number ->
                val prize = winningNumbersMap.getPrize(number)
                val numberItem = NumberItem(
                    number = number,
                    prize = prize
                )
                result.add(numberItem)
            }
            return Either.right(result)
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
