package com.joseangelmaneiro.lottery.task

import android.os.AsyncTask
import com.joseangelmaneiro.lottery.view.MainView
import com.joseangelmaneiro.lottery.model.Ticket
import com.joseangelmaneiro.lottery.data.LocalDataSource
import java.lang.ref.WeakReference

class DeleteTicketTask(
    private val localDataSource: LocalDataSource,
    view: MainView
): AsyncTask<Ticket, Void, Boolean>() {

    private val view = WeakReference(view)

    override fun onPreExecute() {
        view.get()?.loading()
    }

    override fun doInBackground(vararg tickets: Ticket): Boolean {
        return localDataSource.deleteTicket(tickets[0])
    }

    override fun onPostExecute(result: Boolean) {
        view.get()?.refreshNumbers()
    }
}