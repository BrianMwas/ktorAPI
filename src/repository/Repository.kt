package com.ktor.api.repository

import com.ktor.api.model.Blog

interface Repository {
    suspend fun add(titleValue: String, summaryValue: String, contentValue: String)
    suspend fun findBlog(id: Int): Blog?
    suspend fun findBlog(id: String): Blog?
    suspend fun blogs() : List<Blog>
    suspend fun clear()
    suspend fun remove(id: Int): Boolean
}