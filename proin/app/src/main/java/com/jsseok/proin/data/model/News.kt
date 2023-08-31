package com.jsseok.proin.data.model

import java.io.Serializable

// News data 클래스
data class News(
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String
) : Serializable {
    fun getURL(): String {
        return url
    }

    companion object {
        val EMPTY = News("", "", "", "")
    }
}
