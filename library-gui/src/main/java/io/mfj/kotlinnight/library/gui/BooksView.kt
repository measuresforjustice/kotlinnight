package io.mfj.kotlinnight.library.gui

import io.mfj.kotlinnight.library.*

import javafx.beans.binding.Bindings
import javafx.scene.Parent
import javafx.scene.control.*

import tornadofx.*

class BooksView:View() {

	val controller:LibraryController by inject()

	var table:TableView<GBook> by singleAssign()
	var checkoutButton:Button by singleAssign()

	override val root:Parent = vbox {
		toolbar {
			checkoutButton = button("Checkout") {
				action {
					val book = table.selectionModel.selectedItem
							.checkouts
							.entries
							.first { (_,checkout) ->
								checkout == null
							}
							.key
					var checkout:Checkout? = null
					runAsync {
						checkout = controller.checkout( book )
					} ui {
						alert(
								type = Alert.AlertType.INFORMATION,
								title = "Checked out",
								header = "Checked out ${checkout!!.book.title.title} checked out, due ${checkout!!.due}."
						)
					}
				}
			}
		}
		table = tableview(controller.books) {
			readonlyColumn("Title",GBook::bookTitle)
			readonlyColumn("Author",GBook::author)
			column("Available",GBook::availableProperty)
		}
	}

	override fun onDock() {
		super.onDock()
		controller.loadBooks()
	}

	init {
		checkoutButton.disableProperty().bind(
				table.selectionModel.selectedItemProperty().isNull
						.or(
								Bindings.select<Int>(
										table.selectionModel.selectedItemProperty(),
										"available"
								).isEqualTo(0)
						)
		)
	}

}