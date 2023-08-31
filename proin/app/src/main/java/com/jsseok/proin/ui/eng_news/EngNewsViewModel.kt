package com.jsseok.proin.ui.eng_news

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsseok.proin.data.model.News
import com.jsseok.proin.data.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// EngNewsViewModel
class EngNewsViewModel @ViewModelInject constructor(
    private val newsRepository: NewsRepository
): ViewModel() {
    private val _newsListLiveData: MutableLiveData<List<News>> = MutableLiveData()
    val newsListLiveData: LiveData<List<News>> = _newsListLiveData

    // 'us' -> 미국 뉴스 데이터
    fun list() = viewModelScope.launch(Dispatchers.IO) {
        newsRepository.list("us") { newsList ->
            _newsListLiveData.postValue(newsList)
        }
    }
}