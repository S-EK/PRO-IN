package com.jsseok.proin.data.repository

import com.jsseok.proin.data.model.News

interface NewsRepository {
    suspend fun list(path: String, completed: (List<News>) -> Unit)
}