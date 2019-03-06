package io.mfj.kotlinnight.library.gui

import io.mfj.kotlinnight.library.*

import javafx.scene.Parent
import tornadofx.*

class TitlesView:View() {

	val controller:LibraryController by inject()

	override val root:Parent = tableview(controller.titles) {
		readonlyColumn("Title",Title::title)
		readonlyColumn("Author",Title::author)
		readonlyColumn("ISBN",Title::isbn)
	}

	override fun onDock() {
		super.onDock()
		runAsync {
			controller.loadTitles()
		}
	}

}