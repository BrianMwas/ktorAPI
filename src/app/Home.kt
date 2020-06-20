package com.ktor.api.app

import io.ktor.application.call
import io.ktor.pebble.PebbleContent
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get

const val HOME = "/"

fun Route.home() {
    get(HOME) {
        val messageOne = "This is the beginning of a very good start"
        val titleOne = "Welcome..."

        val titleTwo = "Get what you were looking for"
        val messageTwo = "All you have ever wanted..."

        call.respond(PebbleContent("start.html", mapOf("message" to messageOne, "title" to titleOne, "messageTwo" to messageTwo, "titleTwo" to titleTwo)))
    }
}
