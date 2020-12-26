package com.joseangelmaneiro.lottery.executor

import com.joseangelmaneiro.lottery.Either

abstract class Task<Request, Response>(
  private val taskExecutor: TaskExecutor
) {

  operator fun invoke(request: Request) {
    onPreExecute()
    taskExecutor.runOnBgThread {
      val response = doInBackground(request)
      taskExecutor.runOnMainThread {
        onPostExecute(response)
      }
    }
  }

  protected abstract fun onPreExecute()

  protected abstract fun doInBackground(request: Request): Either<Exception, Response>

  protected abstract fun onPostExecute(response: Either<Exception, Response>)
}