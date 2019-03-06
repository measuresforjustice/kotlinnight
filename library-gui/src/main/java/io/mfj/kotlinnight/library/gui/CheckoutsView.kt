package io.mfj.kotlinnight.library.gui

import io.mfj.kotlinnight.library.*

import javafx.scene.Parent
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.TableView
import tornadofx.*

class CheckoutsView:View() {

	val controller:LibraryController by inject()

	var table:TableView<Checkout> by singleAssign()
	var checkinButton:Button by singleAssign()

	override fun onDock() {
		super.onDock()
		runAsync {
			controller.loadCheckouts()
		}
	}

	override val root:Parent = vbox {
		toolbar {
			checkinButton = button("Return") {
				action {
					val checkout = table.selectionModel.selectedItem
					runAsync {
						controller.checkin( checkout )
						controller.loadCheckouts()
					} ui {
						alert(
								type = Alert.AlertType.INFORMATION,
								title = "Checked in",
								header = "${checkout.book.title.title} checked in"
						)
					}
				}
			}
		}
		table = tableview(controller.checkouts) {
			readonlyColumn("Book",Checkout::book).cellFormat {
				text = it.title.title
			}
			readonlyColumn("Out",Checkout::out)
			readonlyColumn("Due",Checkout::due)
		}
	}

	init {
		checkinButton.disableProperty().bind(
				table.selectionModel.selectedItemProperty().isNull
		)
	}


}