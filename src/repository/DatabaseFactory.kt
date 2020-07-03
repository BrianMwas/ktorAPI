package com.ktor.api.repository

import com.ktor.api.model.Blogs
import com.ktor.api.model.User
import com.ktor.api.model.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import sun.plugin.com.Dispatcher

object DatabaseFactory {
    fun init() {
        Database.connect(hikari())

        transaction {
            SchemaUtils.create(Blogs)
            SchemaUtils.create(Users)
        }
    }
    private fun hikari() : HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"
        config.jdbcUrl = System.getenv("JDBC_DATABASE_URL")
        config.password = System.getenv("JDBC_DATABASE_PWD")
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(
        block: () -> T
    ): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }

}