package com.jsseok.proin.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

// Http 요청
class HttpClientImpl @Inject constructor() : HttpClient {
    override suspend fun getData(path: String, completed: (Result<String>) -> Unit) {
        withContext(Dispatchers.IO) {
            // News API url
            val baseUrl = "https://newsapi.org/v2/top-headlines?category=technology&apiKey=60d2184af7b64c828128621fdd5bf03b&country="
            // path -> 'kr' or 'us' 국가 설정
            val url = URL(baseUrl + path)
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0")

            val streamReader = InputStreamReader(urlConnection.inputStream, "UTF-8")
            val buffered = BufferedReader(streamReader)

            val content = StringBuilder()
            while (true) {
                val data = buffered.readLine() ?: break
                content.append(data)
            }

            buffered.close()
            urlConnection.disconnect()

            completed(Result.success(content.toString()))
        }
    }
}