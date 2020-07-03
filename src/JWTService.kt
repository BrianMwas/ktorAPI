package com.ktor.api

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.ktor.api.model.User
import java.util.*

class JWTService {
    private val issuer = "blogs"
    private val JWT_SECRET = System.getenv("JWT_SECRET")
    private val algorithm = Algorithm.HMAC256(JWT_SECRET)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun generateToken(user: User) = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("id", user.userId)
        .withExpiresAt(expiresAt())
        .sign(algorithm)



    private fun expiresAt() = Date(System.currentTimeMillis() + 3_600_000 * 24)
}