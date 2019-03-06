package io.mfj.kotlinnight.intro

import org.junit.Test
import kotlin.test.assertEquals

// Take a look at intro/build.gradle.kts to see how tests are enabled

class CollectionTests {

	// a test
	@Test
	fun test() {
		assertEquals(1,1)
	}

	@Test
	fun testMap() {
		val list = listOf(
				"a",
				"b",
				"c"
		)
		assertEquals(
				listOf( "A", "B", "C" ),

				list.map { value -> value.toUpperCase() }
		)

		assertEquals(
				listOf( "A", "B", "C" ),

				list.map(String::toUpperCase)
		)
	}

	@Test
	fun testMapMap() {
		val map = mapOf(
				1 to "a",
				2 to "b",
				3 to "c",
				4 to "d"
		)
		assertEquals(
				mapOf(
						-1 to "a",
						-2 to "b",
						-3 to "c",
						-4 to "d"
				),

				map.mapKeys { (k,v) ->
					k * -1
				}
		)

	}

	@Test
	fun testFilter() {
		val list = listOf(
				1,
				2,
				3,
				4
		)
		assertEquals(
				listOf(4, 8),

				list
						.filter { it % 2 == 0 }
						.map { it * 2 }
		)
	}


}