package com.ktor.api.api

import com.ktor.api.API_VERSION
import com.ktor.api.model.Blog
import com.ktor.api.model.Request
import com.ktor.api.repository.Repository
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post

const val BLOGS_END_POINT = "${API_VERSION}/blogs"

fun Route.blogs(db: Repository) {
    authenticate("auth") {
        post(BLOGS_END_POINT) {
            val request = call.receive<Request>()
            val blog = db.add(request.title, request.summary, request.content)
            call.respond(HttpStatusCode.OK, blog)
        }
    }

}