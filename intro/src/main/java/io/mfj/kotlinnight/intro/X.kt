package io.mfj.kotlinnight.intro

// Kotlin lets you add functions to existing classes with Extension Functions

// This is the syntax to create one:

fun String.excite():String = "${this}!!!!!!!"

object Ext {

	@JvmStatic
	fun main( args:Array<String> ) {

		// You can call it just like any other function
		println( "hello world".excite() )
	}

}

// Classes can contain extension functions, and they are in the class' scope
class Foo( private val name:String ) {

	// This extension function has access to the instance.
	fun String.said() = "${name} said \"${this}.\""

	fun sayHello() {
		println( "hello world".said() )
	}

	companion object {
		@JvmStatic
		fun main( args:Array<String> ) {
			Foo("John").sayHello()
		}
	}

}
