package com.ktor.api.app

import com.ktor.api.model.USessions
import io.ktor.application.call
import io.ktor.locations.*
import io.ktor.response.respondRedirect
import io.ktor.routing.*
import io.ktor.sessions.clear
import io.ktor.sessions.sessions

const val SIGN_OUT = "/signout"

@Location(SIGN_OUT)
class Signout

fun Route.signout() {
    get<Signout> {
        call.sessions.clear<USessions>()
        call.respondRedirect("/auth/signin")
    }
}