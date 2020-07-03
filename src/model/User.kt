package com.ktor.api.model

import io.ktor.auth.Principal
import org.jetbrains.exposed.sql.Table
import java.io.Serializable

data class User(
    val userId: String,
    val email: String,
    val passwordHash: String,
    val displayName: String): Serializable, Principal

object Users : Table() {
    override val primaryKey = PrimaryKey(name = "id")
    val id = varchar("id", 20).primaryKey()

    val email = varchar("email", 128).uniqueIndex()
    val displayName = varchar("displayName", 256)
    val passwordHash = varchar("password_hash", 64)
}