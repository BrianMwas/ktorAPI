package com.ktor.api

import com.auth0.jwt.JWT
import com.ktor.api.api.blogs
import com.ktor.api.api.login
import com.ktor.api.app.*
import com.ktor.api.model.Blogs.user
import com.ktor.api.model.USessions
import com.ktor.api.model.User
import com.ktor.api.repository.BlogsRepository
import com.ktor.api.repository.DatabaseFactory
import com.mitchellbosecke.pebble.loader.ClasspathLoader
import io.ktor.application.*
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.jwt
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.pebble.Pebble
import io.ktor.request.header
import io.ktor.request.host
import io.ktor.sessions.SessionTransportTransformerMessageAuthentication
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import java.net.URI
import java.util.concurrent.TimeUnit

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
    }

    install(PartialContent) {
        // Maximum number of ranges that will be accepted from a HTTP request.
        // If the HTTP request specifies more ranges, they will all be merged into a single range.
        maxRangeCount = 10
    }

    install(ContentNegotiation) {
        gson {
        }
    }

    install(Sessions) {
        cookie<USessions>("SESSION") {
            transform(SessionTransportTransformerMessageAuthentication(hashKey))
        }
    }

    DatabaseFactory.init()

    val db = BlogsRepository()

    val jwt = JWTService()

    install(Authentication) {
        jwt("jwt") {
            verifier(jwt.verifier)
            realm = "Blogs App"
            validate {
                val payload = it.payload
                val claim = payload.getClaim("id")
                val claimString = claim.asString()
                val claimUser = db.userById(claimString)
                claimUser
            }
        }
    }

    val hashFunction = { s: String -> hashPass(s) }

    routing {
        static("/static") {
            resources("static/ui")
        }
        home(db)
        about(db)
        allBlogs(db, hashFunction)
        blogs(db)
        signin(db, hashFunction)
        signout()
        signup(db, hashFunction)
        login(db, jwtService = JWTService())
    }
}

const val API_VERSION = "/api/v1"

suspend fun ApplicationCall.redirect(location: Any) {
    respondRedirect(application.locations.href(location))
}

fun ApplicationCall.refererhost() = request.header(HttpHeaders.Referrer)?.let { URI.create(it).host }

fun ApplicationCall.securityCode(date: Long, user: User, hashFuntion: (String) -> String) =
    hashFuntion("$date : ${user.userId} : ${request.host()} : ${refererhost()}")

fun ApplicationCall.verifyCode(date: Long, user: User, code: String, hashFuntion: (String) -> String) =
    securityCode(date, user, hashFuntion) == code && (System.currentTimeMillis() - date).let { it > 0 && it < TimeUnit.MILLISECONDS.convert(2, TimeUnit.HOURS) }


