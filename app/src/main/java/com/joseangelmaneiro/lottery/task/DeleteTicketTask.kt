package com.joseangelmaneiro.lottery.task

import com.joseangelmaneiro.lottery.Either
import com.joseangelmaneiro.lottery.view.NumbersView
import com.joseangelmaneiro.lottery.model.Ticket
import com.joseangelmaneiro.lottery.data.LocalDataSource
import com.joseangelmaneiro.lottery.executor.Task
import com.joseangelmaneiro.lottery.executor.TaskExecutor
import java.lang.ref.WeakReference

class DeleteTicketTask(
    private val localDataSource: LocalDataSource,
    view: NumbersView,
    taskExecutor: TaskExecutor
): Task<Ticket, Boolean>(taskExecutor) {

    private val view = WeakReference(view)

    override fun onPreExecute() {
        view.get()?.loading()
    }

    override fun doInBackground(request: Ticket): Either<Exception, Boolean> {
        val response = localDataSource.deleteTicket(request)
        return if (response) {
            Either.right(response)
        } else {
            Either.left(Exception("No se ha eliminado correctamente el ticket " + request.number))
        }
    }

    override fun onPostExecute(response: Either<Exception, Boolean>) {
        view.get()?.refreshNumbers()
    }
}
