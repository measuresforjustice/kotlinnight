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

}