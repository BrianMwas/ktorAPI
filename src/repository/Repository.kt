package com.ktor.api.repository

import com.ktor.api.model.Blog
import com.ktor.api.model.User

interface Repository {
    suspend fun add(userId: String, titleValue: String, summaryValue: String, contentValue: String)
    suspend fun findBlog(id: Int): Blog?
    suspend fun findBlog(id: String): Blog?
    suspend fun blogs() : List<Blog>
    suspend fun clear()
    suspend fun remove(id: Int): Boolean
    suspend fun userById (userId: String): User?
    suspend fun user(userId: String, hash: String? = null): User?
    suspend fun userByEmail(email: String): User?
    suspend fun createUser(user: User)
}