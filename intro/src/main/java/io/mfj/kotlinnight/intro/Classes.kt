// Kotlin is a lot like Java.

// It uses the same packages
package io.mfj.kotlinnight.intro

// It uses the standard library
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// What?! No semicolon?!

// The Kotlin compiler is way smarter than javac.

// There are interfaces like you'd expect

/**
 * This is an interface.
 */
interface Thingy {

	/**
	 * This is a function.
	 *
	 * Function definitions start with "fun".
	 I The return type is at the end, after a colon.
	 */
	fun equip():Boolean


	/**
	 * Parameters are similar, the type goes after the name.
	 *
	 * Unit is like void. You can omit it.
	 */
	fun trade(toPerson:String):Unit

	/**
	 * No ":Unit" at the end.
	 */
	fun discard()

	/**
	 * Interfaces can define values in addition to functions.
	 */
	val name:String

	/**
	 * A nullable value. The [name] value is not nullable, because there is no ?.
	 *
	 * This value is nullable.
	 */
	val description:String?


}

/**
 * Let's make another interface that extends the first.
 */
interface Item: Thingy {

	/**
	 * This value is nullable
	 */
	val value:Int?

	/**
	 * There are a few things going on here.
	 * 1) Interfaces can have implementations!
	 * 2) ?: Elvis lives!
	 * 3) You do not have to declare exceptions.
	 */
	fun sell() {
		value ?: throw Exception("You cannot sell something with no value!")
		discard()
	}

}

/**
 * Okay, let's make a class.
 */
open class Sword: Item { // implementing an interface is about how you'd expect.
	// open means we can extend it. You can't by default.

	/**
	 * Note the "override"
	 */
	override val name:String = "Sword"

	// There are some cool regex things.
	override val description:String = "descr3pt9on".replace(Regex("\\d"),"i")

	override val value:Int? = 4

	/**
	 * This is a variable. "var", not "val". It can change.
	 * Note we did not have to put the ":Int" because Kotlin figured that out.
	 * Like Java, it is strongly-typed.
	 * Unlike Java, it is really good at type-inference.
	 */
	var strength = 10

	// This is a handy thing to do when you haven't gotten around to implementing things
	override fun discard() = TODO()

	override fun equip():Boolean =
			if ( canEquip() ) {
				println( "You are now holding ${name}!" )
				true
			} else {
				println( "You cannot equip ${name}. Maybe you should ${if ( value ?: 0 > 0 ) "sell it for ${value}" else "give it away"}" )
				// That line got kind of long. It would be nice if we had multi-line strings.
				false
			}

	override fun trade(toPerson:String) {
		// We do have multi-line strings!
		println( """
			You traded ${name} to ${toPerson} at
			${DateTimeFormatter.BASIC_ISO_DATE.format( LocalDate.now() ) }
			Hopefully ${toPerson} does not become your enemy and use it against you.
		""".trimIndent())
		// We can do complex expression inside of strings (single and multiline)

		discard()
	}

	/**
	 * This is a private function.
	 * By default things are public. There are also "protected" and "internal"
	 */
	private fun canEquip():Boolean {
		return true
	}

	// This is where we put our singlton stuff.
	companion object {
		const val A_CONSTANT = "Hello World."
		const val ANOTHER_CONSTANT = 12.4
		val YET_ANOTHER = Sword() // only primitives can be const.

		// static functions go here
		fun sayHello() {
			println(A_CONSTANT)
		}

		// If you need it to be static, do this:
		@JvmStatic
		fun sayGoodbye() {
			System.exit(0)
		}
		// For details: see https://kotlinlang.org/docs/reference/java-to-kotlin-interop.html#static-methods
	}
}

enum class Color { Red, Blue, Green }

sealed class SpecialSword(val color:Color):Sword() {
	// sealed means can only extend from within this file.

	override fun toString() =
			when ( color ) {
				Color.Red -> "red sword"
				Color.Blue -> "blue sword"
				else -> "some color sword"
			}

}

class SpecialRedSword:SpecialSword(Color.Red)
class SpecialBlueSword:SpecialSword(Color.Blue)
class SpecialGreenSword:SpecialSword(Color.Green)

// varargs!
class MultiColoredSword( val hiltColor:Color, vararg val bladeColors:Color ):Sword()

// no "new"
val sword1 = MultiColoredSword( Color.Red, Color.Blue, Color.Green )

// named parameters
val mySword = MultiColoredSword(
		hiltColor = Color.Red,
		bladeColors = *arrayOf( Color.Blue, Color.Green )
)

/* You may have noticed that we declared several classes here,
none of which were called Classes. That's totally allowed.

/*
This is still the comment.
 */

This is still the comment. Kotlin counts /* and */ so you can comment out blocks
with comments. This is super nice.

 */
// The comment ended there
