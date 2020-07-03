package com.ktor.api.app

import com.ktor.api.MIN_PWD_LENGTH
import com.ktor.api.MIN_USERID_LENGTH
import com.ktor.api.model.USessions
import com.ktor.api.redirect
import com.ktor.api.repository.Repository
import com.ktor.api.userNameValid
import io.ktor.application.call
import io.ktor.locations.*
import io.ktor.pebble.PebbleContent
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import io.ktor.util.url


const val SIGN_IN = "auth/signin"

@Location(SIGN_IN)
data class Signin(val userId: String = "", val error: String = "")

fun Route.signin(db: Repository, hashFunction: (String) -> String) {
    post<Signin> {
        val params = call.receiveParameters()
        val userId  =  params["userId"] ?: return@post call.redirect(it)
        val password = params["password"] ?: return@post call.redirect(it)

        val signinError = Signin(userId)
        val signin =  when {
            userId.length < MIN_USERID_LENGTH -> null
            password.length < MIN_PWD_LENGTH -> null
            !userNameValid(userId) -> null
            else -> db.user(userId, hashFunction(password))
        }

        if (signin == null) {
            call.redirect(signinError.copy(error = "Invalid username or password"))
        } else {
            call.sessions.set(USessions(signin.userId))
            call.redirect(Blog())
        }
    }

    get<Signin> {
        val title = "Sign Up"

        val user = call.sessions.get<USessions>()?.let { db.user(it.userId) }
        if(user != null) {
            call.redirect(Blog())
        } else {
            call.respond(PebbleContent("signin.html", mapOf("title" to title)))
        }
    }
}