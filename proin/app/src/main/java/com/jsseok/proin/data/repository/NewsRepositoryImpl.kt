package com.jsseok.proin.data.repository

import com.jsseok.proin.data.model.News
import com.jsseok.proin.data.network.HttpClient
import org.json.JSONObject
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: HttpClient    // 의존성
): NewsRepository {
    override suspend fun list(path: String, completed: (List<News>) -> Unit) {
        remoteDataSource.getData(
            path
        ) { result ->
            result.onSuccess {
                completed(parseContentList(JSONObject(it)))
            }
        }
    }

    // News API에서 읽어온 Data를 리스트에 할당
    private fun parseContentList(jsonObject: JSONObject): List<News> {
        val articleArray = jsonObject.getJSONArray("articles")
        val mNewsList = mutableListOf<News>()

        for (n in 0 until articleArray.length()) {
            val obj: JSONObject = articleArray.getJSONObject(n)
            val news = News(
                obj.getString("title"),
                obj.getString("description"),
                obj.getString("url"),
                obj.getString("urlToImage")
            )
            mNewsList.add(news)
        }

        return mNewsList
    }
}