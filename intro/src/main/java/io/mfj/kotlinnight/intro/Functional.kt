package io.mfj.kotlinnight.intro

import java.util.concurrent.TimeUnit

object Functional {

	@JvmStatic
	fun main( args:Array<String> ) {

		time(100) {
			println("Hello World" )
		}

	}

	// body takes zero arguments, and returns Unit
	fun time( times:Int, body:()->Unit ) {
		val t0 = System.nanoTime()
		(0..times).forEach { i ->
			body()
		}
		val t1 = System.nanoTime()
		println( "Doing a thing ${times} times took ${TimeUnit.NANOSECONDS.toMillis(t1-t0)} ms" )
	}

	// let's make the body take a parameter and return something

}

// You can define functions in any scope
object InnerFUnction{

	@JvmStatic
	fun main( args:Array<String> ) {

		listOf( "Tom", "Dick","Harry", "Sally" ).forEach { name ->

			fun say(text:String) {
				println( "${name} said \"${text}\"" )
			}

			say("Hello")
			say("Goodbye")

		}

	}
}
