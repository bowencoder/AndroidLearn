package com.example.androidlearn

import android.app.Application

class AndroidLearnApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // 全局初始化入口
        // 如需接入 Hilt、Timber、LeakCanary 等，在此处初始化
    }
}
