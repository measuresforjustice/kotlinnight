package io.mfj.kotlinnight.coroutines

import kotlinx.coroutines.*

// We have 8 and 16 core computers.
// Let's use them.

// The old way - fire up a bunch of threads.
// Thread creation and switching is expensive.
// If you have 10 or 100 threads you are okay. 1000, 10000 threads is not going to perform well.
// Threaded parallelism spends a lot of time blocking.

// Coroutines lets us parallelize many processes without using more threads than we have cores.
// The coroutine dispatchers sets up a thead pool, and we can use that dispatcher for multiple thingss.

// With Threads, we rely on the OS to forcibly switch us out.
// With coroutines, we cooperate.

// suspend keyword means the function does not return immediately
suspend fun getToken(): String {
	// hit some backend
	return "abcdefd"
}

suspend fun post( body:String ): String {
	val token = getToken() // async
	val result = doPost( token, body ) // async
	return result
}

fun doPost( token:String, body:String ): String {
	// hit some backend
	return """{"result":"success"}"""
}

// Where is the suspending actually happening?
// look for the little marks in the IDE

// we can do normal things like loops and exception handling.
suspend fun makeRequests() {
	(0..3).forEach {
		try {
			val response = post( "request ${it}" )
			println(response)
		} catch ( e:Exception ) {
			e.printStackTrace()
		}
	}
}

// You cannot call a suspending function from a non-suspended context
// Try removing "suspend" from makeRequests

// How do we call a suspending function?

// Use coroutine builders

fun main() {
	// launch starts a coroutine scope in the GlobalScope coroutine dispatcher.
	// This is where the forking happens.
	val job:Job = GlobalScope.launch {
		makeRequests()
	}
	println("hello")
	runBlocking {
		job.join()
	}
	println( "done" )
}

// For more in-depth, see Roman Elizarov's presentations at KotlinConf:
// https://www.youtube.com/watch?v=_hfBv0a09Jc
// https://www.youtube.com/watch?v=a3agLJQ6vt8
// Watch both before you try to write any code - some stuff showed in the first one changed before 1.0
