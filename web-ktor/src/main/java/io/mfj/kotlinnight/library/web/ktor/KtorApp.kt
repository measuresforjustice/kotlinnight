package io.mfj.kotlinnight.library.web.ktor

import io.mfj.kotlinnight.library.*

import java.time.LocalDate

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.content.*
import io.ktor.jackson.jackson
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.jetty.Jetty
import io.ktor.util.AttributeKey
import io.ktor.util.pipeline.PipelineContext

import org.slf4j.LoggerFactory

object KtorApp {

	@JvmStatic private val log = LoggerFactory.getLogger(KtorApp::class.java)

	@JvmStatic
	fun main(args:Array<String>) {

		val port = System.getenv()["PORT"]?.toInt() ?: 4567
		val host = System.getenv()["HOST"] ?: "127.0.0.1"

		embeddedServer(factory=Jetty,port=port,host=host) {
			install(ContentNegotiation) {
				jackson()
			}
			install(DefaultHeaders) {
				header( name = "X-Powered-By", value = "Kotlinnight Library" )
			}
			install(InventoryProvider)

			routing {

				static("/") {
					staticBasePackage = "static"
					defaultResource("index.html")
				}

				get("/title/") {
					call.respond( inventory.getTitles() )
				}
				get("/title/{isbn}") { ctx ->
					val isbn = call.parameters["isbn"]!!
					call.respond( inventory.getTitle(isbn) ?: throw NotFoundException() )
				}
				get("/title/:isbn/checkouts") { ctx ->
					val isbn = call.parameters["isbn"]!!
					call.respond( inventory.getCheckouts(isbn) )
				}
				get("/book/") { ctx ->
					call.respond( inventory.getBooks() )
				}
				get("/book/:id") { ctx ->
					val id = call.parameters["id"]!!.toInt()
					call.respond( inventory.getBook(id) ?: throw NotFoundException() )
				}
				get("/book/:id/checkout") { ctx ->
					val id = call.parameters["id"]!!.toInt()
					call.respond( inventory.getCheckout(id) ?: throw NotFoundException() )
				}
				post("/book/:id/checkout") { ctx ->
					val id = call.parameters["id"]!!.toInt()
					val book = inventory.getBook(id) ?: throw NotFoundException()
					call.respond( inventory.checkout(book, LocalDate.now().plusDays(10)))
				}
				post("/book/:id/checkin") { ctx ->
					val id = call.parameters["id"]!!.toInt()
					val book = inventory.getBook(id) ?: throw NotFoundException()
					inventory.checkin(book)
					call.respond(book)
				}

				// show using separate controller
				get("/checkout/", Controller.listCheckouts)
				put("/checkout", Controller.checkout)
				put("/checkin", Controller.checkin)
			}

		}.start(wait=true)

	}
}

class InventoryProvider(private val inventory:Inventory) {

	private fun intercept(call:ApplicationCall) {
		call.attributes.put(KEY,inventory)
	}

	companion object Feature : ApplicationFeature<Application,Unit,InventoryProvider> {
		internal val KEY = AttributeKey<Inventory>(Inventory::class.java.canonicalName)

		override val key = AttributeKey<InventoryProvider>("Inventory")

		override fun install(pipeline: Application, configure: Unit.() -> Unit): InventoryProvider {
			val feature = InventoryProvider(InventoryImpl)
			pipeline.intercept(ApplicationCallPipeline.Features) {
				feature.intercept(call)
			}
			return feature
		}
	}
}
val PipelineContext<Unit, ApplicationCall>.inventory:Inventory
	get() = this.call.attributes.get(InventoryProvider.KEY)
