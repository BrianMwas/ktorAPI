package com.ktor.api.model

data class Blog (val title: String, val summary: String, val content: String) {
    var id: Int? = null
}