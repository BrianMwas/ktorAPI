package com.ktor.api.repository

import com.ktor.api.model.Blog

interface Repository {
    suspend fun add(blog: Blog): Blog
    suspend fun findBlog(id: Int): Blog?
    suspend fun findBlog(id: String): Blog?
    suspend fun blogs() : ArrayList<Blog>
    suspend fun remove(blog: Blog): Boolean
    suspend fun remove(id: Int): Boolean
}