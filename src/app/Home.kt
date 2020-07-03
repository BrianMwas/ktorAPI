package com.ktor.api.app

import com.ktor.api.model.USessions
import com.ktor.api.redirect
import com.ktor.api.repository.Repository
import io.ktor.application.call
import io.ktor.pebble.PebbleContent
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions

const val HOME = "/"

fun Route.home(db: Repository) {
    get(HOME) {
        val messageOne = "This is the beginning of a very good start"
        val titleOne = "Welcome..."

        val titleTwo = "Get what you were looking for"
        val messageTwo = "All you have ever wanted..."

        val user = call.sessions.get<USessions>()?.let { db.user(it.userId) }
        if(user == null) {
            call.redirect(Signin())
        }
        call.respond(PebbleContent("start.html", mapOf("user" to user!!.displayName, "message" to messageOne, "title" to titleOne, "messageTwo" to messageTwo, "titleTwo" to titleTwo)))
    }
}
