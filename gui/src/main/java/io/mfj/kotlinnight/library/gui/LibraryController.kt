package io.mfj.kotlinnight.library.gui

import io.mfj.kotlinnight.library.*

import tornadofx.*
import java.time.LocalDate

class LibraryController:Controller() {

	val inventory:Inventory = InventoryImpl

	val titles = mutableListOf<Title>().observable()

	fun loadTitles() {
		titles.setAll( inventory.getTitles() )
	}

	val books = mutableListOf<GBook>().observable()

	fun loadBooks() {
		books.setAll(
				inventory.getTitles()
						.map { title ->
							GBook( title,
									inventory.getCheckouts(title.isbn).toMutableMap() )
						}
		)
	}

	val checkouts = mutableListOf<Checkout>().observable()

	fun loadCheckouts() {
		checkouts.setAll( inventory.getCheckouts() )
	}

	fun checkout( book:Book ): Checkout {
		val checkout = inventory.checkout(book, LocalDate.now().plusDays(10))
		loadBooks()
		loadCheckouts()
		return checkout
	}

	fun checkin( checkout:Checkout ) {
		inventory.checkin(checkout.book)
		loadBooks()
		loadCheckouts()
	}

}