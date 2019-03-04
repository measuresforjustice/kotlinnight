package io.mfj.kotlinnight.library.web

import io.mfj.kotlinnight.library.*

import kotlin.concurrent.thread

import java.time.LocalDate
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicInteger

import org.slf4j.LoggerFactory

object InventoryImpl:Inventory {

	@JvmStatic private val log = LoggerFactory.getLogger(InventoryImpl::class.java)

	private val listeners:MutableList<InventoryEventListener> = CopyOnWriteArrayList()

	private val titles:List<Title> =
			listOf(Title("978-1942878506", "Kotlin Apprentice", "raywenderlich.com Tutorial Team"),
					Title("978-0805092271", "Ordinary Injustice", "Amy Bach"),
					Title("978-0810955202", "The Stray Shopping Carts of Eastern North America",
							"Julian Montague")
			)

	private val books:List<Book>

	private val checkouts:MutableMap<Book, Checkout> = mutableMapOf()

	init {
		val idGenerator = AtomicInteger(0)
		books =
				titles
						.flatMap { title ->
							(1..2).map { // 2 copies of each title
								Book(idGenerator.incrementAndGet(), title)
							}
						}
	}

	override fun getTitles():Collection<Title> = titles

	override fun getBooks():Collection<Book> = books

	override fun getCheckouts():Collection<Checkout> = synchronized(
			checkouts) {
		checkouts.values.toList()
	}


	override fun checkout(book:Book,due:LocalDate):Checkout = synchronized(
			checkouts) {
		if ( checkouts[book] != null ) throw Exception("Already checked out.")
		val checkout = Checkout(book, LocalDate.now(), due)
		checkouts[book] = checkout
		fire(CheckoutEvent(checkout))
		checkout
	}

	override fun checkin(book:Book) = synchronized(checkouts) {
		val checkout = checkouts.remove(book)
		if ( checkout != null ) {
			fire(CheckinEvent(checkout))
		} else {
			throw Exception("Not checked out.")
		}
	}

	override fun addListener(listener:InventoryEventListener) {
		listeners.add(listener)
	}

	private fun fire(event:InventoryEvent) {
		thread {
			listeners.forEach { listener ->
				try {
					listener(event)
				} catch ( e:Exception ) {
					log.warn( "Listener error - ${e.message}", e )
				}
			}
		}
	}

}

