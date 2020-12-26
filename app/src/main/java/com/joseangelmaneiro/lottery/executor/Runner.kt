package com.joseangelmaneiro.lottery.executor

interface Runner {
  operator fun invoke(c: () -> Unit)
}
