package io.mfj.kotlinnight.library

import java.time.LocalDate

data class Title( val isbn:String, val title:String, val author:String )

data class Book( val id:Int, val title:Title)

data class Checkout( val book:Book, val out:LocalDate, val due:LocalDate )

