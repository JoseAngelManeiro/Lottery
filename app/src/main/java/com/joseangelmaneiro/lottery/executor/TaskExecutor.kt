package com.joseangelmaneiro.lottery.executor

class TaskExecutor(
  val runOnMainThread: Runner,
  val runOnBgThread: Runner
)
