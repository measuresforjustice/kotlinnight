package io.mfj.kotlinnight.intro

import java.util.concurrent.TimeUnit

// In Kotlin, functions are first-class!

// Anything you can do with a value, you can do with a function.

object Functional {

	// body takes zero arguments, and returns Unit
	fun time( times:Int, body:()->Unit ) {
		val t0 = System.nanoTime()
		(0..times).forEach { i ->
			body()
		}
		val t1 = System.nanoTime()
		println( "Doing a thing ${times} times took ${TimeUnit.NANOSECONDS.toMillis(t1-t0)} ms" )
	}

	// let's run it
	@JvmStatic
	fun main( args:Array<String> ) {

		time(100) {
			println("Hello World" )
		}

		// you can store functions as variables and pass them.
		val body = {
			println("Hello World" )
		}
		time(1,body)

	}

	// let's make the body take a parameter and return something

}

// You can have a function as a class member
class HasAFunction( val handler:(String,String)->String ) {

	fun doStuff( a:String, b:String ): String {
		return handler(a,b)
	}

	companion object {
		@JvmStatic
		fun main( args:Array<String> ) {
			val result = HasAFunction( { a, b -> "${a} ${b}" } )
					.doStuff( "hello", "world" )
			println( result )
		}
	}

}

// You can define functions in any scope
object InnerFunction {

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
