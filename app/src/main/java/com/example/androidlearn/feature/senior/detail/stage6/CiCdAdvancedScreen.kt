package com.example.androidlearn.feature.senior.detail.stage6

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val ciCdAdvancedData = NoteData(
    title = "CI/CD 与发布工程",
    subtitle = "GitHub Actions，Fastlane，多渠道打包，Play Store API",
    color = Color.parseColor("#3F51B5"),
    chapters = listOf(
        ChapterItem("1",   "流水线阶段"),
        ChapterItem("1.1", "代码检查 → 单元测试 → 集成测试 → 打包 → 分发 → 上线"),
        ChapterItem("2",   "GitHub Actions"),
        ChapterItem("2.1", "YAML 定义 workflow，matrix 并行多 Android 版本测试"),
        ChapterItem("3",   "Fastlane"),
        ChapterItem("3.1", "Ruby 工具链，supply 上传 Play Store，截图自动化"),
        ChapterItem("4",   "多渠道打包"),
        ChapterItem("4.1", "productFlavors 定义渠道，Walle / VasDolly 写入渠道信息"),
        ChapterItem("5",   "Firebase App Distribution"),
        ChapterItem("5.1", "测试包快速分发，替代蒲公英"),
        ChapterItem("6",   "灰度发布"),
        ChapterItem("6.1", "Play Store 分阶段发布（1% → 10% → 全量）"),
    )
)
