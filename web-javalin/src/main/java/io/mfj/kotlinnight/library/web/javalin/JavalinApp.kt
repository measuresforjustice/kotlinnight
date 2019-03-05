package io.mfj.kotlinnight.library.web.javalin

import io.mfj.kotlinnight.library.*

import kotlin.reflect.KFunction1

import java.time.LocalDate

import io.javalin.Context
import io.javalin.Javalin
import io.javalin.NotFoundResponse
import io.javalin.apibuilder.ApiBuilder
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.security.Role

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
				.accessManager { handler, ctx, permittedRoles ->
					if ( permittedRoles.isEmpty() ) {
						handler.handle(ctx)
					} else {
						val roles = ctx.user?.roles ?: emptySet()
						val matchingRoles = permittedRoles.filter { permittedRole -> roles.contains( permittedRole ) }

						if ( matchingRoles.isNotEmpty() ) {
							log.debug( "User ${ctx.user?.name ?: "<none>"} has matching roles ${matchingRoles.joinToString(",")}" )
							handler.handle(ctx)
						} else {
							log.debug( "User ${ctx.user?.name ?: "<none>"} does not have any of ${permittedRoles.joinToString(",")}" )
							ctx.status(403)
									.result("Unauthorized")
						}
					}
				}
				.routes {

					before { ctx ->
						val username = ctx.queryParam("user")
						if ( username != null ) {
							val roles = setOf(LibraryRole.Read, LibraryRole.Checkout)
							val user = User(username,roles)
							ctx.sessionAttribute(User::class.java.canonicalName,user)
						}
					}

					get("/title/",LibraryRole.Read) { ctx ->
						ctx.json( ctx.inventory.getTitles() )
					}
					get("/title/:isbn",LibraryRole.Read) { ctx ->
						val isbn = ctx.pathParam("isbn")
						ctx.json( ctx.inventory.getTitle(isbn) ?: throw NotFoundResponse() )
					}
					get("/title/:isbn/checkouts",LibraryRole.Read) { ctx ->
						val isbn = ctx.pathParam("isbn")
						ctx.json( ctx.inventory.getCheckouts(isbn) )
					}
					get("/book/",LibraryRole.Read) { ctx ->
						ctx.json( ctx.inventory.getBooks() )
					}
					get("/book/:id",LibraryRole.Read) { ctx ->
						val id = ctx.pathParam("id").toInt()
						ctx.json( ctx.inventory.getBook(id) ?: throw NotFoundResponse() )
					}
					get("/book/:id/checkout",LibraryRole.Checkout) { ctx ->
						val id = ctx.pathParam("id").toInt()
						ctx.json( ctx.inventory.getCheckout(id) ?: throw NotFoundResponse() )
					}
					post("/book/:id/checkout",LibraryRole.Checkout) { ctx ->
						val id = ctx.pathParam("id").toInt()
						val book = ctx.inventory.getBook(id) ?: throw NotFoundResponse()
						ctx.json( ctx.inventory.checkout(book, LocalDate.now().plusDays(10)))
					}
					post("/book/:id/checkin",LibraryRole.Checkout) { ctx ->
						val id = ctx.pathParam("id").toInt()
						val book = ctx.inventory.getBook(id) ?: throw NotFoundResponse()
						ctx.inventory.checkin(book)
						ctx.json(book)
					}

					// show using separate controller
					get("/checkout/", Controller::listCheckouts, LibraryRole.Checkout )
					put("/checkout", Controller::checkout, LibraryRole.Checkout )
					put("/checkin", Controller::checkin, LibraryRole.Checkout )
				}
				.start()
	}

	private fun get( path:String, vararg permittedRoles:Role, handler:(Context)->Unit) = ApiBuilder.get( path, handler, permittedRoles.toSet() )
	private fun post( path:String, vararg permittedRoles:Role, handler:(Context)->Unit) = ApiBuilder.post( path, handler, permittedRoles.toSet() )

	private fun get( path:String, handler:KFunction1<Context,Unit>, vararg permittedRoles:Role ) = ApiBuilder.get( path, handler, permittedRoles.toSet() )
	private fun put( path:String, handler:KFunction1<Context,Unit>, vararg permittedRoles:Role ) = ApiBuilder.put( path, handler, permittedRoles.toSet() )
}

val Context.inventory:Inventory
	get() = appAttribute(Inventory::class.java)