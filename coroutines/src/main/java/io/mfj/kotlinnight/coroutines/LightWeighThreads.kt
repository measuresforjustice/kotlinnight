package io.mfj.kotlinnight.coroutines

import kotlinx.coroutines.*

// a million threads would be a problem.
// we can do a million jobs in a coroutine no problem.
fun main() = runBlocking {
	val jobs = List(100_000) {
		launch {
			delay(1000) // suspend for 1 second
			print(".")
		}
	}
	jobs.forEach { it.join() }
	println("done")
}