package com.ktor.api.app

import com.ktor.api.model.USessions
import com.ktor.api.redirect
import com.ktor.api.repository.Repository
import io.ktor.application.call
import io.ktor.client.engine.callContext
import io.ktor.pebble.PebbleContent
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions


const val ABOUT = "/about"

fun Route.about(db: Repository) {
    get(ABOUT) {
        val about = "This is out true story"
        val user = call.sessions.get<USessions>()?.let { db.user(it.userId) }
        if(user == null) {
            call.redirect(Signin())
        }
        if (user != null) {
            call.respond(PebbleContent("about.html", mapOf("aboutus" to about, "user" to user.displayName)))
        }
    }
}