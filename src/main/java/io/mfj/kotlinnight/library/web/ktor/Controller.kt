package io.mfj.kotlinnight.library.web.ktor

import io.mfj.kotlinnight.library.Checkout

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.features.NotFoundException
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineInterceptor

object Controller {

	val listCheckouts:PipelineInterceptor<Unit,ApplicationCall> = {
		call.respond( inventory.getCheckouts() )
	}

	val checkout:PipelineInterceptor<Unit,ApplicationCall> = {
		val checkoutRequest:Checkout = call.receive()
		call.respond( inventory.checkout(
				book = checkoutRequest.book,
				due = checkoutRequest.due
		) )
	}

	val checkin:PipelineInterceptor<Unit,ApplicationCall> = {
		val checkoutRequest:Checkout = call.receive()
		val checkout = inventory.getCheckout(checkoutRequest.book.id) ?: throw NotFoundException()
		inventory.checkin(checkout.book)
	}
}