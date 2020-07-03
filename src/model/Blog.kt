package com.ktor.api.model

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import java.io.Serializable

data class Blog (val id: Int, val title: String, val summary: String, val content: String, val userId: String) : Serializable

object Blogs : IntIdTable() {
    val user: Column<String> = varchar("user_id", 20).index()
    val title : Column<String> = varchar("title", 255)
    val summary: Column<String> = varchar("summary", 255)
    val content: Column<String> = varchar("content", 255)
}