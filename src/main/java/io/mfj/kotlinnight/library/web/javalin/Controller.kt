package io.mfj.kotlinnight.library.web.javalin

import io.javalin.Context
import io.javalin.NotFoundResponse
import io.mfj.kotlinnight.library.Checkout

object Controller {

	fun listCheckouts(ctx:Context) {
		ctx.json( ctx.inventory.getCheckouts() )
	}

	fun checkout(ctx:Context) {
		val checkoutRequest = ctx.body<Checkout>()
		ctx.inventory.checkout(
				book = checkoutRequest.book,
				due = checkoutRequest.due
		)
	}

	fun checkin(ctx:Context) {
		val checkoutRequest = ctx.body<Checkout>()
		val checkout = ctx.inventory.getCheckout(checkoutRequest.book.id) ?: throw NotFoundResponse()
		ctx.inventory.checkin(checkout.book)
	}

}