package com.jsseok.proin

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// App Class : Application
// 공동으로 멤버 변수, 메소드 사용 : 공유 클래스
// 의존성 주입
@HiltAndroidApp
class App : Application() {
    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}