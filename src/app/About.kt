package com.ktor.api.app

import io.ktor.application.call
import io.ktor.client.engine.callContext
import io.ktor.pebble.PebbleContent
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get


const val ABOUT = "/about"

fun Route.about() {
    get(ABOUT) {
        val about = "This is out true story"
        call.respond(PebbleContent("about.html", mapOf("aboutus" to about)))
    }
}