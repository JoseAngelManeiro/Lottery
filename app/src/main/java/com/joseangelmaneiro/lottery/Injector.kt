package com.joseangelmaneiro.lottery

import android.content.Context
import com.joseangelmaneiro.lottery.data.ApiClient
import com.joseangelmaneiro.lottery.data.LocalDataSource
import com.joseangelmaneiro.lottery.executor.BackgroundRunner
import com.joseangelmaneiro.lottery.executor.MainRunner
import com.joseangelmaneiro.lottery.executor.TaskExecutor
import com.joseangelmaneiro.lottery.task.DeleteAllTicketsTask
import com.joseangelmaneiro.lottery.task.DeleteTicketTask
import com.joseangelmaneiro.lottery.task.GetNumbersTask
import com.joseangelmaneiro.lottery.task.SaveTicketTask
import com.joseangelmaneiro.lottery.view.NumbersView

class Injector(context: Context, lotteryType: LotteryType) {

    companion object {
        private val taskExecutor = TaskExecutor(
            runOnBgThread = BackgroundRunner(),
            runOnMainThread = MainRunner()
        )
    }

    private val localDataSource = LocalDataSource(context, lotteryType)
    private val apiClient = ApiClient(lotteryType)

    fun getSaveTicketTask(numbersView: NumbersView): SaveTicketTask {
        return SaveTicketTask(localDataSource, numbersView, taskExecutor)
    }

    fun getDeleteTicketTask(numbersView: NumbersView): DeleteTicketTask {
        return DeleteTicketTask(localDataSource, numbersView, taskExecutor)
    }

    fun getGetNumbersTask(numbersView: NumbersView): GetNumbersTask {
        return GetNumbersTask(apiClient, localDataSource, numbersView, taskExecutor)
    }

    fun getDeleteAllTicketsTask(numbersView: NumbersView): DeleteAllTicketsTask {
        return DeleteAllTicketsTask(localDataSource, numbersView, taskExecutor)
    }
}