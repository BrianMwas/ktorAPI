package com.ktor.api.app

import com.ktor.api.model.Blog
import com.ktor.api.model.User
import com.ktor.api.repository.Repository
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.pebble.PebbleContent
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import java.lang.IllegalArgumentException

const val BLOGS = "/all-blogs"

fun Route.allBlogs(db: Repository) {
    authenticate("auth") {
        get(BLOGS) {
            val user = call.authentication.principal as User
            val blogs = db.blogs()
            call.respond(PebbleContent("blog.html", mapOf("blogs" to blogs, "displayName" to user.displayName)))
        }

        post(BLOGS) {
            val params = call.receiveParameters()
            val blogTitle = params["title"] ?: throw  IllegalArgumentException("Missing blog title")
            val blogSummary = params["summary"] ?: throw  IllegalArgumentException("Missing blog summary")
            val blogContent = params["content"] ?: throw  IllegalArgumentException("Missing blog content")

            db.add(blogTitle, blogSummary, blogContent)
            call.respondRedirect(BLOGS)
        }
    }

}