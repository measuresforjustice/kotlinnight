package io.mfj.kotlinnight.library.web.javalin

import io.javalin.security.Role

data class User(val name:String, val roles:Set<Any> )

enum class LibraryRole:Role { Read, Checkout }
