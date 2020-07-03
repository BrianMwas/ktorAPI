package com.ktor.api.app

import com.ktor.api.model.USessions
import com.ktor.api.redirect
import com.ktor.api.repository.Repository
import com.ktor.api.securityCode
import com.ktor.api.verifyCode
import io.ktor.application.call
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.pebble.PebbleContent
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import java.lang.IllegalArgumentException

const val BLOG = "/all-blogs"

@Location(BLOG)
class Blog

fun Route.allBlogs(db: Repository, hashFunction: (String) -> String) {

    get<Blog> {
        val user = call.sessions.get<USessions>()?.let { db.user(it.userId) }
        if (user == null) {
            call.redirect(Signup())
        } else {
            val blog = db.blogs()
            val date = System.currentTimeMillis()
            val code = call.securityCode(date, user, hashFunction)
            call.respond(PebbleContent("blog.html", mapOf("blogs" to blog, "displayName" to user.displayName, "user" to user, "date" to date, "code" to code)))
        }
    }

    post<Blog> {
        val user = call.sessions.get<USessions>()?.let { db.user(it.userId) }

        val params = call.receiveParameters()
        val date = params["date"]?.toLongOrNull() ?: call.redirect(it)
        val code = params["code"].toString() ?: call.redirect(it)
        val blogTitle = params["title"] ?: throw  IllegalArgumentException("Missing blog title")
        val blogSummary = params["summary"] ?: throw  IllegalArgumentException("Missing blog summary")
        val blogContent = params["content"] ?: throw  IllegalArgumentException("Missing blog content")

//        val action = params["action"] ?: throw IllegalArgumentException("Missing parameter: action")
//        when(action) {
//            "delete" -> {
//                val id = params["id"] ?: throw IllegalArgumentException("Missing parameter: id")
//                db.remove(id.toInt())
//            }
//            "add" -> {
//
//
//
//            }
//        }
        if (user == null || call.verifyCode(date as Long, user, code.toString() , hashFunction)) {
            call.redirect(Signin())
        }

        db.add(user!!.userId, blogTitle, blogSummary, blogContent)
        call.respondRedirect(BLOG)
    }
}