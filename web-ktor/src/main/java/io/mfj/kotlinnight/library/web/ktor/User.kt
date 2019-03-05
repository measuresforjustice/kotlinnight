package io.mfj.kotlinnight.library.web.ktor

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.Principal
import io.ktor.auth.principal
import io.ktor.util.pipeline.PipelineContext

class User(val name:String,val roles:Set<Role>):Principal

val PipelineContext<Unit, ApplicationCall>.user:User?
	get() = this.call.principal()

enum class Role { Read, Checkout }
