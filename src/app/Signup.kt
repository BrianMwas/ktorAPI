package com.ktor.api.app


import com.ktor.api.MIN_PWD_LENGTH
import com.ktor.api.MIN_USERID_LENGTH
import com.ktor.api.model.USessions
import com.ktor.api.model.User
import com.ktor.api.redirect
import com.ktor.api.repository.Repository
import com.ktor.api.userNameValid
import io.ktor.application.application
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.http.Parameters
import io.ktor.locations.*
import io.ktor.pebble.PebbleContent
import io.ktor.request.receive
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import io.ktor.util.error
import kotlin.math.sign


const val SIGN_UP = "auth/signup"

@Location(SIGN_UP)
data class Signup (val userId: String = "", val displayName: String = "", val email: String = "", val error: String = "")

fun Route.signup(db: Repository, hashFunction: (String) ->  String) {
    post<Signup> {
        val user = call.sessions.get<USessions>()?.let { db.user(it.userId) }
        if (user !== null) return@post call.redirect(Blog())

        val params = call.receiveParameters()

        application.log.debug(params.toString())

        val userId = params["username"] ?: return@post call.redirect(it)
        val pwd = params["password"]  ?: return@post call.redirect(it)
        val dName = params["displayName"]  ?: return@post call.redirect(it)
        val email = params["email"]  ?: return@post call.redirect(it)

        val signUpError = Signup(userId, dName, email)
        application.log.debug(signUpError.toString())

        when {
            pwd.length < MIN_PWD_LENGTH ->
                call.redirect ( signUpError.copy(error = "The password should be at least $MIN_PWD_LENGTH characters long...") )
            userId.length < MIN_USERID_LENGTH ->
                call.redirect( signUpError.copy(error = "Username should be at least $MIN_USERID_LENGTH") )
            !userNameValid(userId) ->
                call.redirect( signUpError.copy(error = "Username should consists of digits, letters and dots") )
            db.user(userId) !== null ->
                call.redirect( signUpError.copy(error = "User with the username has already been registered.") )
            else -> {
                val hash = hashFunction(pwd)
                val newUser = User(email = email, passwordHash = hash, displayName = dName, userId = userId)
                try {
                    db.createUser(newUser)
                } catch (e: Throwable) {
                    application.log.error(e)
                    when {
                        db.user(userId) != null ->
                            call.redirect(signUpError.copy(error = "User with the username is already registered"))
                        db.userByEmail(email) != null ->
                            call.redirect(signUpError.copy(error = "User with the $email is already registered"))
                        else -> {
                            application.log.error("Failed to register user", e)
                            call.redirect(
                                signUpError.copy(error = "Failed to register user")
                            )
                        }
                    }
                }
                call.sessions.set(USessions(newUser.userId))
                call.redirect(Blog())
            }
        }
    }

    get<Signup> {
        val title = "Sign up"
        val user = call.sessions.get<USessions>()?.let { db.user(it.userId) }
        if(user != null) {
            call.redirect(Blog())
        } else {
            call.respond(PebbleContent("signup.html", mapOf("title" to title, "error" to it.error)))
        }
    }
}