package io.mfj.kotlinnight.library.gui

import io.mfj.kotlinnight.library.*
import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import tornadofx.*

class GBook( val title:Title, val checkouts:MutableMap<Book,Checkout?> ) {

	val bookTitle = title.title
	val author = title.author

	var availableProperty:IntegerProperty = calcAvailable().toProperty()
	val available by availableProperty

	fun updateCheckouts( checkouts:Map<Book,Checkout?> ) {
		this.checkouts.putAll(checkouts)
		availableProperty.value = calcAvailable()
	}

	private fun calcAvailable():Int =
			checkouts.count { (_,checkout) -> checkout == null }

}