package io.mfj.kotlinnight.library.web.javalin

import io.javalin.Context
import io.javalin.security.Role

data class User(val name:String, val roles:Set<Any> )

val Context.user:User?
	get() = sessionAttribute(User::class.java.canonicalName)


enum class LibraryRole:Role { Read, Checkout }
