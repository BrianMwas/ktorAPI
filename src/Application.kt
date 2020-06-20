package com.ktor.api

import com.ktor.api.api.blogs
import com.ktor.api.app.about
import com.ktor.api.app.allBlogs
import com.ktor.api.app.home
import com.ktor.api.model.User
import com.ktor.api.repository.BlogsRepository
import com.ktor.api.repository.DatabaseFactory
import com.mitchellbosecke.pebble.loader.ClasspathLoader
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.features.*
import io.ktor.auth.*
import io.ktor.gson.*
import io.ktor.pebble.Pebble

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Locations) {
    }

    install(Pebble) {
        loader(ClasspathLoader().apply {
            prefix = "templates"
        })
    }

    install(DefaultHeaders)

    install(StatusPages) {
        exception<Throwable> { e ->
            call.respondText(e.localizedMessage, ContentType.Text.Plain, HttpStatusCode.InternalServerError)
        }
    }

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("MyCustomHeader")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    install(PartialContent) {
        // Maximum number of ranges that will be accepted from a HTTP request.
        // If the HTTP request specifies more ranges, they will all be merged into a single range.
        maxRangeCount = 10
    }

    install(Authentication) {
        basic("auth") {
            realm = "Ktor Server"
            validate { credentials ->
                if(credentials.password == "${ credentials.name }123") User(credentials.name) else null
            }
        }
    }

    install(ContentNegotiation) {
        gson {
        }
    }
    DatabaseFactory.init()

    val db = BlogsRepository()

    routing {
        static("/static") {
            resources("static/ui")
        }
        home()
        about()
        allBlogs(db)
        blogs(db)
//        get("/html-freemarker") {
//            call.respond(FreeMarkerContent("index.ftl", mapOf("data" to IndexData(listOf(1, 2, 3))), ""))
//        }
//
//        // Static feature. Try to access `/static/ktor_logo.svg`
//        static("/static") {
//            resources("static")
//        }

//        get<MyLocation> {
//            call.respondText("Location: name=${it.name}, arg1=${it.arg1}, arg2=${it.arg2}")
//        }
//        // Register nested routes
//        get<Type.Edit> {
//            call.respondText("Inside $it")
//        }
//        get<Type.List> {
//            call.respondText("Inside $it")
//        }
//
//        install(StatusPages) {
//            exception<AuthenticationException> { cause ->
//                call.respond(HttpStatusCode.Unauthorized)
//            }
//            exception<AuthorizationException> { cause ->
//                call.respond(HttpStatusCode.Forbidden)
//            }
//
//        }
//
//        authenticate("myBasicAuth") {
//            get("/protected/route/basic") {
//                val principal = call.principal<UserIdPrincipal>()!!
//                call.respondText("Hello ${principal.name}")
//            }
//        }
//
//        get("/json/gson") {
//            call.respond(mapOf("hello" to "world"))
//        }
    }
}

const val API_VERSION = "/api/v1"

//data class IndexData(val items: List<Int>)
//
//@Location("/location/{name}")
//class MyLocation(val name: String, val arg1: Int = 42, val arg2: String = "default")
//
//@Location("/type/{name}") data class Type(val name: String) {
//    @Location("/edit")
//    data class Edit(val type: Type)
//
//    @Location("/list/{page}")
//    data class List(val type: Type, val page: Int)
//}
//
//class AuthenticationException : RuntimeException()
//class AuthorizationException : RuntimeException()

