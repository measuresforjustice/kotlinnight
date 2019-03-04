package io.mfj.kotlinnight.library.web.javalin

import io.mfj.kotlinnight.library.web.Inventory
import io.mfj.kotlinnight.library.web.InventoryImpl

import java.time.LocalDate

import io.javalin.Context
import io.javalin.Javalin
import io.javalin.NotFoundResponse
import io.javalin.apibuilder.ApiBuilder.*

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.util.thread.QueuedThreadPool

import org.slf4j.LoggerFactory

object JavalinApp {

	@JvmStatic private val log = LoggerFactory.getLogger(JavalinApp::class.java)

	@JvmStatic private val requestLog = LoggerFactory.getLogger("request-log")

	@JvmStatic fun main(args:Array<String>) {

		val port = System.getenv()["PORT"]?.toInt() ?: 4567
		val host = System.getenv()["HOST"] ?: "127.0.0.1"

		Javalin
				.create()
				.enableCaseSensitiveUrls()
				.server { // this call can be omitted to use the default host/port
					Server(QueuedThreadPool(250,8,60000))
							.apply {
								addConnector(
										ServerConnector(this)
												.apply {
													this.port = port
													this.host = host
												}
								)
							}
				}
				.requestLogger { ctx, time ->
					requestLog.info( "[${time}] ${ctx.ip()} ${ctx.req.method} ${ctx.url()} -> ${ctx.status()}" )
				}
				.enableStaticFiles("/static")
				.apply {
					this.attribute(Inventory::class.java, InventoryImpl)
				}
				.routes {

					get("/title/") { ctx ->
						ctx.json( ctx.inventory.getTitles() )
					}
					get("/title/:isbn") { ctx ->
						val isbn = ctx.pathParam("isbn")
						ctx.json( ctx.inventory.getTitle(isbn) ?: throw NotFoundResponse() )
					}
					get("/title/:isbn/checkouts") { ctx ->
						val isbn = ctx.pathParam("isbn")
						ctx.json( ctx.inventory.getCheckouts(isbn) )
					}
					get("/book/") { ctx ->
						ctx.json( ctx.inventory.getBooks() )
					}
					get("/book/:id") { ctx ->
						val id = ctx.pathParam("id").toInt()
						ctx.json( ctx.inventory.getBook(id) ?: throw NotFoundResponse() )
					}
					get("/book/:id/checkout") { ctx ->
						val id = ctx.pathParam("id").toInt()
						ctx.json( ctx.inventory.getCheckout(id) ?: throw NotFoundResponse() )
					}
					post("/book/:id/checkout") { ctx ->
						val id = ctx.pathParam("id").toInt()
						val book = ctx.inventory.getBook(id) ?: throw NotFoundResponse()
						ctx.json( ctx.inventory.checkout(book, LocalDate.now().plusDays(10)))
					}
					post("/book/:id/checkin") { ctx ->
						val id = ctx.pathParam("id").toInt()
						val book = ctx.inventory.getBook(id) ?: throw NotFoundResponse()
						ctx.inventory.checkin(book)
						ctx.json(book)
					}

					// show using separate controller
					get("/checkout/", Controller::listCheckouts)
					put("/checkout", Controller::checkout)
					put("/checkin", Controller::checkin)
				}
				.start()
	}

}

val Context.inventory:Inventory
	get() = appAttribute(Inventory::class.java)