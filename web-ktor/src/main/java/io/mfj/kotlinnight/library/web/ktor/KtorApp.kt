package io.mfj.kotlinnight.library.web.ktor

import io.mfj.kotlinnight.library.*

import java.time.LocalDate

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.*
import io.ktor.jackson.jackson
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.jetty.Jetty
import io.ktor.util.AttributeKey
import io.ktor.util.pipeline.ContextDsl
import io.ktor.util.pipeline.PipelineContext
import io.ktor.util.pipeline.PipelineInterceptor

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

			install(Authentication) {
				val provider = object:AuthenticationProvider("query-params") {}
				provider.pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { ctx ->
					val username = ctx.call.request.queryParameters["user"]
					if ( username != null ) {
						val roles = setOf(Role.Read, Role.Checkout)
						val user = User(username,roles)
						ctx.principal(user)
					}
				}
				register( provider )
			}

			routing {

				static("/") {
					staticBasePackage = "static"
					defaultResource("index.html")
				}

				authenticate("query-params") {

					get("/title/",Role.Read) {
						call.respond( inventory.getTitles() )
					}
					get("/title/{isbn}",Role.Read) {
						val isbn = call.parameters["isbn"]!!
						call.respond( inventory.getTitle(isbn) ?: throw NotFoundException() )
					}
					get("/title/:isbn/checkouts",Role.Read) {
						val isbn = call.parameters["isbn"]!!
						call.respond( inventory.getCheckouts(isbn) )
					}
					get("/book/",Role.Read) {
						call.respond( inventory.getBooks() )
					}
					get("/book/:id",Role.Read) {
						val id = call.parameters["id"]!!.toInt()
						call.respond( inventory.getBook(id) ?: throw NotFoundException() )
					}
					get("/book/:id/checkout",Role.Checkout) {
						val id = call.parameters["id"]!!.toInt()
						call.respond( inventory.getCheckout(id) ?: throw NotFoundException() )
					}
					post("/book/:id/checkout",Role.Checkout) {
						val id = call.parameters["id"]!!.toInt()
						val book = inventory.getBook(id) ?: throw NotFoundException()
						call.respond( inventory.checkout(book, LocalDate.now().plusDays(10)))
					}
					post("/book/:id/checkin",Role.Checkout) {
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
			}

		}.start(wait=true)

	}

	private suspend fun PipelineContext<Unit,ApplicationCall>.checkRoles( permittedRoles:Set<Role> ):Boolean {
		if ( ! permittedRoles.isEmpty() ) {
			val roles:Set<Role> = user?.roles ?: emptySet()
			val matchingRoles = permittedRoles.filter { permittedRole -> roles.contains( permittedRole ) }

			if ( matchingRoles.isNotEmpty() ) {
				log.info( "User ${user?.name ?: "<none>"} has matching roles ${matchingRoles.joinToString(",")}" )
			} else {
				log.info( "User ${user?.name ?: "<none>"} does not have any of ${permittedRoles.joinToString(",")}" )
				call.respond(HttpStatusCode.Forbidden)
			}
		}
		return true
	}

	@ContextDsl
	fun Route.get(path: String, permittedRole:Role, body: PipelineInterceptor<Unit, ApplicationCall>): Route =
			get(path,setOf(permittedRole),body)

	@ContextDsl
	fun Route.get(path: String, permittedRoles:Set<Role>, body: PipelineInterceptor<Unit, ApplicationCall>): Route =
			get(path) {
				if ( checkRoles(permittedRoles) ) {
					body(this,Unit)
				}
			}

	@ContextDsl
	fun Route.post(path: String, permittedRole:Role, body: PipelineInterceptor<Unit, ApplicationCall>): Route =
			post(path,setOf(permittedRole),body)

	@ContextDsl
	fun Route.post(path: String, permittedRoles:Set<Role>, body: PipelineInterceptor<Unit, ApplicationCall>): Route =
			post(path) {
				if ( checkRoles(permittedRoles) ) {
					body(this,Unit)
				}
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
