package com.jsseok.proin.data.network

interface HttpClient {
    suspend fun getData(path: String, completed: (Result<String>) -> Unit)
}