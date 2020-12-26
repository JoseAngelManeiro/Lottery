package com.joseangelmaneiro.lottery.view

import android.content.Context
import com.joseangelmaneiro.lottery.data.ApiClient
import com.joseangelmaneiro.lottery.data.LocalDataSource
import com.joseangelmaneiro.lottery.executor.BackgroundRunner
import com.joseangelmaneiro.lottery.executor.MainRunner
import com.joseangelmaneiro.lottery.executor.TaskExecutor
import com.joseangelmaneiro.lottery.task.DeleteTicketTask
import com.joseangelmaneiro.lottery.task.GetNumbersTask
import com.joseangelmaneiro.lottery.task.SaveTicketTask

class Injector(context: Context) {

    private val taskExecutor = TaskExecutor(
        runOnBgThread = BackgroundRunner(),
        runOnMainThread = MainRunner())
    private val localDataSource = LocalDataSource(context)
    private val apiClient = ApiClient()

    fun getSaveTicketTask(mainView: MainView): SaveTicketTask {
        return SaveTicketTask(localDataSource, mainView, taskExecutor)
    }

    fun getDeleteTicketTask(mainView: MainView): DeleteTicketTask {
        return DeleteTicketTask(localDataSource, mainView, taskExecutor)
    }

    fun getGetNumbersTask(mainView: MainView): GetNumbersTask {
        return GetNumbersTask(apiClient, localDataSource, mainView, taskExecutor)
    }
}