package io.mfj.kotlinnight.intro

// Kotlin has its own set of collections that (in most cases) wrap the ones in java.util.
// They can be mutable or immutable.
// Immutability is important for functional programming.

// (Objects are singletons classes)
object CollectionsIntro {

	val someStuff = listOf( "a", "b", 0 )
	// someStuff is mutable.
	// what is the type? (show hints)

	@JvmStatic
	fun main( args:Array<String> ) {
		val thisCanChange = mutableListOf("a", "b", "c")
		thisCanChange.add("d")

		//someStuff.add("d")

		val someStuffPlusD = someStuff.plus("d")
		for ( x in someStuffPlusD ) {
			println(x)
		}
	}

	val aMap:Map<String,Int> = mapOf( "a" to 1, "b" to 2, "c" to 3 )
	// If we declare the type in the LHS, we can omit it from the RHS, because the compiler will infer it.

	// tangent: what is this "to"
	infix fun Int.times(other:Int):Int {
		return this * other
	}

	// lets go look at more of this with some unit tests!

}