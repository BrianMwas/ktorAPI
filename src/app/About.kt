package com.ktor.api.app

import io.ktor.application.call
import io.ktor.client.engine.callContext
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get


const val ABOUT = "/about"

fun Route.about() {
    get(ABOUT) {
        call.respond("About page")
    }
}