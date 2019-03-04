package io.mfj.kotlinnight.library.web

import io.mfj.kotlinnight.library.*

import java.time.LocalDate

sealed class InventoryEvent
data class CheckinEvent(val checkout:Checkout): InventoryEvent()
data class CheckoutEvent(val checkout:Checkout): InventoryEvent()

typealias InventoryEventListener = (InventoryEvent)->Unit

interface Inventory {

	fun getTitles(): Collection<Title>

	fun getTitle(isbn:String):Title? = getTitles().firstOrNull { it.isbn == isbn }

	fun getBooks(): Collection<Book>

	fun getBooks(isbn:String): Collection<Book> = getBooks().filter { it.title.isbn == isbn }

	fun getBook(id:Int): Book? = getBooks().firstOrNull { it.id == id }

	fun getCheckouts(): Collection<Checkout>

	fun getCheckout(id:Int): Checkout? = getCheckouts().firstOrNull { it.book.id == id }

	fun getCheckouts(isbn:String): Map<Book, Checkout?> =
			getBooks(isbn)
					.map { book ->
						book to getCheckout(book.id)
					}
					.toMap()

	fun checkout( book:Book, due:LocalDate):Checkout

	fun checkin( book:Book)

	fun addListener( listener:InventoryEventListener)
}
