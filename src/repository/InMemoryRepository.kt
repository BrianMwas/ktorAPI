package com.ktor.api.repository

import com.ktor.api.model.Blog
import java.lang.IllegalArgumentException
import java.util.concurrent.atomic.AtomicInteger

class InMemoryRepository: Repository {
    private val idCounter = AtomicInteger()
    private val blogs = ArrayList<Blog>()

    override suspend fun add(blog: Blog): Blog {
        if(blogs.contains(blog)) {
            return blogs.find { b -> b.id == blog.id }!!
        }
        blog.id = idCounter.incrementAndGet()
        blogs.add(blog)
        return blog
    }

    override suspend fun findBlog(id: Int) = findBlog(id.toString())

    override suspend fun findBlog(id: String) = blogs.find { it.id.toString() == id } ?: throw IllegalArgumentException("No blog by the id found")

    override suspend fun blogs() = blogs

    override suspend fun remove(blog: Blog): Boolean {
       if (!blogs.contains(blog)) {
           throw IllegalArgumentException("Sorry the blog was not found")
       }
        return blogs.remove(blog)
    }

    override suspend fun remove(id: Int) = remove(findBlog(id))
}