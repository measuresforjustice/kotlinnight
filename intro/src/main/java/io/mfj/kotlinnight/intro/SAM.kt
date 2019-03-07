package io.mfj.kotlinnight.intro

import java.io.File
import kotlin.concurrent.thread

// Java SAM (Single Abstract Method) interfaces can be called with a lambda.
object SAM {

	@JvmStatic
	fun main( args:Array<String> ) {

		// Thread's constructor takes a Runnable, which has a single abstract method, "run"

		// Here is how we would do that with an anonymous object:
		Thread(
				object:Runnable {
					override fun run() {
						println( "Hello world [${Thread.currentThread().name}]" )
					}
				}
		).start()

		// It is much nicer if we just pass a lambda:
		Thread {
			println( "Hello world [${Thread.currentThread().name}]" )
		}.start()

		// Tangent: this is so common, that Kotlin has an extension function
		// to create threads with lambda, and gives other useful arguments:
		thread(start = true, name = "hello-thread",isDaemon = false) {
			println( "Hello World ${Thread.currentThread().name}]" )
		}

		// File.listFiles(FileFilter) is another common SAM case:
		/*
		File(System.getProperty("user.home"))
				.listFiles { file:File ->
					file.extension == "jpg"
				}
				.forEach { file ->
					println(file.absolutePath)
				}
				*/

	}

}

// You can only do SAM conversions with Java interfaces.
// If you want to use a lambda when calling a Kotlin function, use first class functions.
// Type aliases can make this look nicer

typealias Handler = (Int)->Int

object TypeAliasExample {

	@JvmStatic
	fun main( args:Array<String> ) {
		handle { i ->
			i * 2
		}
	}

	fun handle( handler:Handler ) {
		(0 until 2).forEach {
			println( "${handler(it)}" )
		}
	}
}
