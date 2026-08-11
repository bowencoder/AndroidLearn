package com.example.androidlearn.feature.senior.detail.stage6

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val cleanArchData = NoteData(
    title = "Clean Architecture 设计",
    subtitle = "分层架构，用例 UseCase，依赖规则，可测试性",
    color = Color.parseColor("#3F51B5"),
    chapters = listOf(
        ChapterItem("1",   "三层结构"),
        ChapterItem("1.1", "Presentation（UI/ViewModel）→ Domain（UseCase/Model）→ Data（Repository/Source）"),
        ChapterItem("2",   "依赖规则"),
        ChapterItem("2.1", "内层不知道外层，Domain 层零框架依赖"),
        ChapterItem("3",   "UseCase（Interactor）"),
        ChapterItem("3.1", "封装单一业务用例，ViewModel 调用 UseCase"),
        ChapterItem("4",   "Repository 接口"),
        ChapterItem("4.1", "Domain 层定义接口，Data 层提供实现"),
        ChapterItem("5",   "数据映射"),
        ChapterItem("5.1", "Dto → Entity → DomainModel 各层有独立模型"),
        ChapterItem("6",   "测试优势"),
        ChapterItem("6.1", "Domain 层纯 Kotlin，JUnit 直接测试，无 Android 依赖"),
    )
)
