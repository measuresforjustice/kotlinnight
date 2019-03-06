package io.mfj.kotlinnight.library.gui

import javafx.scene.control.Alert

import tornadofx.*

class LibraryView:View() {

	val taskStatus:TaskStatus by inject()

	init {
		title = "Library"
	}

	override val root = borderpane {
		prefWidth = 800.0
		top = vbox {
			menubar {
				menu("File") {
					item("Quit") {
						action {
							System.exit(0)
						}
					}
				}
				menu("Help") {
					item("About") {
						action {
							alert(
									type = Alert.AlertType.INFORMATION,
									title = "About Library GUI",
									header = "Library GUI v1" )
						}
					}
				}
			}
		}
		center = tabpane {
			tab("Titles") {
				isClosable = false
				add<TitlesView>()
			}
			tab("Books") {
				isClosable = false
				add<BooksView>()
			}
			tab("Checkouts") {
				isClosable = false
				add<CheckoutsView>()
			}
		}
		bottom = stackpane {
			visibleWhen { taskStatus.running }
			progressbar { taskStatus.progress }
			useMaxWidth = true
		}
		label(taskStatus.message) {
			useMaxWidth = true
			paddingLeft = 5
		}
	}

}
