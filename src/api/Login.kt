package com.ktor.api.api

import com.ktor.api.JWTService
import com.ktor.api.hashPass
import com.ktor.api.redirect
import com.ktor.api.repository.Repository

import io.ktor.application.call
import io.ktor.locations.Location
import io.ktor.locations.post
import io.ktor.request.receiveParameters
import io.ktor.response.respondText
import io.ktor.routing.Route
import java.util.Objects.hash

const val LOGIN = "/login"

@Location(LOGIN)
class Login

fun Route.login(db: Repository, jwtService: JWTService) {

    post<Login> {
        val params = call.receiveParameters()
        val userId = params["userId"] ?: return@post call.redirect(it)
        val password = params["password"] ?: return@post call.redirect(it)

        val user = db.user(userId, hashPass(password))

        if(user !== null) {
            val token = jwtService.generateToken(user)
            call.respondText(token)
        } else {
            call.respondText("Invalid user")
        }
    }
}