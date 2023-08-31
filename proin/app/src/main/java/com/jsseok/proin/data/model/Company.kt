package com.jsseok.proin.data.model

import java.io.Serializable

// Company data 클래스
data class Company(
    val title: String,
    val url: String,
    val img: Int
) : Serializable {
    fun getName(): String {
        return title
    }

    fun getURL(): String {
        return url
    }

    fun getImgno(): Int {
        return img
    }

    companion object {
        val EMPTY = Company("", "",0)
    }
}