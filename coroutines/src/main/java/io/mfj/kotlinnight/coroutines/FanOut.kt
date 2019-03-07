package io.mfj.kotlinnight.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import java.util.*

// Lets look through a more real example

// We have a source of things. In this case, integers 0 - 1000

// We need to process each one, and want to use 10 workers to do it.

fun main() = runBlocking {

	val workerCount = 10

	val source = (0..1000).asSequence()

	// Create a channel that we can receive items from.
	val channel = produce {
		source.forEach {
			send(it)
		}
	}

	(0 until workerCount)
			.forEach { worker ->
				// launch a worker to read items from the channel and process them.
				launch {
					for ( item in channel ) {
						process(worker,item)
					}
				}
			}

}

val rand = Random()

fun process(worker:Int,item:Int) {
	Thread.sleep( rand.nextInt(10).toLong() )
	println("[${worker}] ${item}")
}

// send is the suspension point. Every time send is called,
// that is the end of the continuation, and the dispatcher
// can switch to another coroutine.
