package io.mfj.kotlinnight.intro

import java.io.File
import kotlin.concurrent.thread

object SAM {

	@JvmStatic
	fun main( args:Array<String> ) {

		Thread {
			println( "Hello world [${Thread.currentThread().name}]" )
		}.start()

		thread {
			println( "Hello World [${Thread.currentThread().name}]" )
		}

		thread(start = true, name = "hello-thread",isDaemon = false) {
			println( "Hello World ${Thread.currentThread().name}]" )
		}

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

	// You can only do SAM conversions with Java interfaces.
	// If you want to use a lambda when calling a Kotlin function, use first class functions.
	// Type aliases can make this look nicer

}

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
