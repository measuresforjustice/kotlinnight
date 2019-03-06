package io.mfj.kotlinnight.library.fuel

import io.mfj.kotlinnight.library.*

import java.time.LocalDate

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.jackson.*

import org.slf4j.LoggerFactory

class FuelInventory(url:String = "http://127.0.0.1:4567/",private val username:String="john.doe"):Inventory {

	companion object {
		val mapper = configuredObjectMapper()
		@JvmStatic private val log = LoggerFactory.getLogger(FuelInventory::class.java)
	}

	init {
		FuelManager.instance.basePath = url
		FuelManager.instance.baseParams = listOf( "user" to username )
	}

	override fun getTitles():Collection<Title> =
			"title"
					.httpGet()
					.responseObject<Collection<Title>>(mapper)
					.third
					.get()

	override fun getBooks():Collection<Book> =
			"book"
					.httpGet()
					.responseObject<Collection<Book>>(mapper)
					.third
					.get()

	override fun getCheckouts():Collection<Checkout> =
			"checkout"
					.httpGet()
					.responseObject<Collection<Checkout>>(mapper)
					.third
					.get()

	override fun checkout(book:Book,due:LocalDate):Checkout =
			"book/${book.id}/checkout?user=${username}"
					.httpPost(
							listOf(
									"due" to dateFormatter.format(due)
							)
					)
					.responseObject<Checkout>(mapper)
					.third
					.get()

	override fun checkin(book:Book):Unit =
			"book/${book.id}/checkin?user=${username}"
					.httpPost()
					.responseObject<Book>(mapper)
					.third
					.get()
					.let {
						log.info( "checked in ${it}")
					}

	override fun addListener(listener:InventoryEventListener) = TODO()

}