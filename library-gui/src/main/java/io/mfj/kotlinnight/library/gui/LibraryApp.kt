package io.mfj.kotlinnight.library.gui

import tornadofx.*

class LibraryApp:App(LibraryView::class) {

	companion object {
		@JvmStatic
		fun main(args:Array<String>) {
			launch<LibraryApp>(args)
		}
	}

}