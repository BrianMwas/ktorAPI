package com.ktor.api

import io.ktor.util.hex
import java.nio.charset.Charset
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

const val MIN_USERID_LENGTH = 4
const val MIN_PWD_LENGTH = 6

val hashKey = hex(
    System.getenv("SECRET_KEY")
)

val hmacKey = SecretKeySpec(hashKey, "HmacSHA1")

fun hashPass(pwd: String): String {
   val hmac = Mac.getInstance("HmacSHA1")
    hmac.init(hmacKey)
    return hex(hmac.doFinal(pwd.toByteArray(Charsets.UTF_8)))
}

private val userIdPattern = "[a-zA-Z0-9_\\.]+".toRegex()

internal fun userNameValid(userId: String)  = userId.matches(userIdPattern)
