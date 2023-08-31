package com.jsseok.proin.di

import com.jsseok.proin.data.network.HttpClient
import com.jsseok.proin.data.network.HttpClientImpl
import com.jsseok.proin.data.repository.NewsRepository
import com.jsseok.proin.data.repository.NewsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {   // 의존성 주입을 위한 클래스
    @Provides
    @Singleton
    fun provideNewsRepository(newsRepository: NewsRepositoryImpl): NewsRepository =
        newsRepository

    @Provides
    @Singleton
    fun provideHttpClient(httpClient: HttpClientImpl): HttpClient =
        httpClient
}