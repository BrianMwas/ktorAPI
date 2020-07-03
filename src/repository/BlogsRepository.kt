package com.ktor.api.repository

import com.ktor.api.model.Blog
import com.ktor.api.model.Blogs
import com.ktor.api.model.User
import com.ktor.api.model.Users
import com.ktor.api.model.Users.id
import com.ktor.api.model.Users.passwordHash
import com.ktor.api.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.IllegalArgumentException

class BlogsRepository: Repository {
    override suspend fun add( userId: String, titleValue: String, summaryValue: String, contentValue: String) {
        transaction {
            Blogs.insert {
                it[user] = userId
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

    override suspend fun userById(userId: String) = dbQuery {
        Users.select { id.eq(userId) }
            .map {
                User(userId, displayName = it[Users.displayName], email = it[Users.email], passwordHash = it[passwordHash])
            }.singleOrNull()
    }

    override suspend fun clear() {
        Blogs.deleteAll()
    }

    override suspend fun user(userId: String, hash: String?): User? {
        val user = dbQuery {
            Users.select {
                (Users.id eq userId)
            }.mapNotNull { toUser(it) }
                .singleOrNull()
        }

        return when {
            user == null -> null
            hash == null -> user
            hash == user.passwordHash -> user
            else -> null
        }
    }

    override suspend fun userByEmail(email: String) =  dbQuery {
        Users.select { (Users.email eq email) }
            .map {
                User(
                    it[Users.id],
                    it[Users.email],
                    it[Users.passwordHash],
                    it[Users.displayName]
                )
            }.singleOrNull()

    }

    override suspend fun createUser(user: User) = dbQuery {
        Users.insert {
            it[id] = user.userId
            it[email] = user.email
            it[passwordHash] = user.passwordHash
            it[displayName] = user.displayName
        }
        Unit
    }

    private fun toBlog(row: ResultRow): Blog {
        return Blog(id = row[Blogs.id].value, title = row[Blogs.title], summary = row[Blogs.summary], content = row[Blogs.content], userId = row[Blogs.user])
    }

    private fun toUser(row: ResultRow) =
        User(
            userId = row[Users.id],
            email = row[Users.email],
            displayName = row[Users.displayName],
            passwordHash = row[Users.passwordHash]

        )
}