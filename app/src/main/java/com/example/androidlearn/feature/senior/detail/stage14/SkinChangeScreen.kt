package com.example.androidlearn.feature.senior.detail.stage14

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val skinChangeData = NoteData(
    title = "换肤与资源隔离",
    subtitle = "AssetManager 替换方案、Resources 代理、皮肤包加载与 Dark Mode 适配",
    color = Color.parseColor("#3F51B5"),
    chapters = listOf(
        ChapterItem("1",   "皮肤包方案"),
        ChapterItem("1.1", "将皮肤资源打包为独立 APK（不含代码），运行时用反射创建 AssetManager 加载皮肤包"),
        ChapterItem("2",   "资源代理方案"),
        ChapterItem("2.1", "替换 Activity 的 Resources 对象，拦截资源获取请求并返回皮肤资源"),
        ChapterItem("3",   "LayoutInflater.Factory2"),
        ChapterItem("3.1", "Hook 布局加载，在 View 创建时记录需要换肤的 View 和属性，换肤时统一刷新"),
        ChapterItem("4",   "Dark Mode（Android 10+）"),
        ChapterItem("4.1", "通过 AppCompatDelegate.setDefaultNightMode() 切换，结合 -night 资源限定符"),
        ChapterItem("5",   "资源隔离（多包）"),
        ChapterItem("5.1", "利用 AssetManager 的 addAssetPath 支持多路径，实现基包资源 + 皮肤包资源叠加"),
        ChapterItem("6",   "字体换肤"),
        ChapterItem("6.1", "通过 Typeface.createFromAsset() 动态加载皮肤包内的 .ttf 文件"),
    )
)
