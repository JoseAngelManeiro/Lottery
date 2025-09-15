package com.joseangelmaneiro.lottery.task

import com.joseangelmaneiro.lottery.Either
import com.joseangelmaneiro.lottery.view.NumbersView
import com.joseangelmaneiro.lottery.data.LocalDataSource
import com.joseangelmaneiro.lottery.executor.Task
import com.joseangelmaneiro.lottery.executor.TaskExecutor
import java.lang.ref.WeakReference

class DeleteAllTicketsTask(
    private val localDataSource: LocalDataSource,
    view: NumbersView,
    taskExecutor: TaskExecutor
): Task<Unit, Boolean>(taskExecutor) {

    private val view = WeakReference(view)

    override fun onPreExecute() { }

    override fun doInBackground(request: Unit): Either<Exception, Boolean> {
        val response = localDataSource.deleteAll()
        return if (response) {
            Either.right(response)
        } else {
            Either.left(Exception("No se han eliminado correctamente lost tickets"))
        }
    }

    override fun onPostExecute(response: Either<Exception, Boolean>) {
        view.get()?.refreshNumbers()
    }
}
