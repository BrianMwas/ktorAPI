package com.ktor.api.repository

import com.ktor.api.model.Blog
import com.ktor.api.model.Blogs
import com.ktor.api.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.IllegalArgumentException

class BlogsRepository: Repository {
    override suspend fun add(titleValue: String, summaryValue: String, contentValue: String) {
        transaction {
            Blogs.insert {
                it[title] = titleValue
                it[summary] = summaryValue
                it[content] = contentValue
            }
        }
    }

    override suspend fun findBlog(id: Int): Blog? = dbQuery {
        Blogs.select {
            (Blogs.id eq id)
        }.mapNotNull { toBlog(it) }
            .singleOrNull()
    }

    override suspend fun findBlog(id: String): Blog? {
        return findBlog(id.toString())
    }

    override suspend fun blogs(): List<Blog> = dbQuery {
        Blogs.selectAll().map { toBlog(it) }
    }

    override suspend fun remove(id: Int): Boolean {
       if(findBlog(id) == null) {
           throw IllegalArgumentException("Blog by the id was not found")
       }
        return  dbQuery {
            Blogs.deleteWhere {
                Blogs.id eq id
            } > 0
        }
    }

    override suspend fun clear() {
        Blogs.deleteAll()
    }

    private fun toBlog(row: ResultRow): Blog {
        return Blog(id = row[Blogs.id].value, title = row[Blogs.title], summary = row[Blogs.summary], content = row[Blogs.content])
    }
}