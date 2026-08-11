package com.example.androidlearn.feature.senior.detail.stage14

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val resourceMgrData = NoteData(
    title = "资源管理机制",
    subtitle = "AssetManager、resources.arsc 结构、资源 ID 生成规则与资源冲突解决",
    color = Color.parseColor("#3F51B5"),
    chapters = listOf(
        ChapterItem("1",   "资源 ID 格式"),
        ChapterItem("1.1", "0xPPTTEEEE，PP=包ID（0x7F=App，0x01=系统），TT=资源类型，EEEE=资源序号"),
        ChapterItem("2",   "resources.arsc"),
        ChapterItem("2.1", "二进制资源映射表，包含所有语言/屏幕密度的资源路径或直接值（如 String）"),
        ChapterItem("3",   "AssetManager（Native）"),
        ChapterItem("3.1", "根据资源 ID 和当前 Configuration（语言、屏幕密度等）查找最佳匹配资源"),
        ChapterItem("4",   "资源限定符优先级"),
        ChapterItem("4.1", "MCC > 语言 > 布局方向 > 屏幕宽高 > 屏幕密度 > 平台版本（取最优匹配）"),
        ChapterItem("5",   "多模块资源合并"),
        ChapterItem("5.1", "AGP 将所有 Module 的资源合并，PP 段统一为 0x7F，同名资源以主 Module 为准"),
        ChapterItem("6",   "运行时 Configuration 变更"),
        ChapterItem("6.1", "屏幕旋转/语言切换触发 onConfigurationChanged，AssetManager 重新匹配资源"),
    )
)
