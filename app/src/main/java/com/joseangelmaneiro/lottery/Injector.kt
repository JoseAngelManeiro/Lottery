package com.joseangelmaneiro.lottery

import android.content.Context
import com.joseangelmaneiro.lottery.data.ApiClient
import com.joseangelmaneiro.lottery.data.LocalDataSource
import com.joseangelmaneiro.lottery.task.DeleteTicketTask
import com.joseangelmaneiro.lottery.task.GetNumbersTask
import com.joseangelmaneiro.lottery.task.SaveTicketTask
import com.joseangelmaneiro.lottery.view.MainView

class Injector(context: Context) {

    private val apiClient = ApiClient()
    private val localDataSource = LocalDataSource(context)

    fun provideGetNumbersTask(mainView: MainView): GetNumbersTask {
        return GetNumbersTask(apiClient, localDataSource, mainView)
    }

    fun provideSaveTicketTask(mainView: MainView): SaveTicketTask {
        return SaveTicketTask(localDataSource, mainView)
    }

    fun provideDeleteTicketTask(mainView: MainView): DeleteTicketTask {
        return DeleteTicketTask(localDataSource, mainView)
    }
}