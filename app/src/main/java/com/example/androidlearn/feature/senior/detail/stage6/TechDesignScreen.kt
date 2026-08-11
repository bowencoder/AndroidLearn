package com.example.androidlearn.feature.senior.detail.stage6

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val techDesignData = NoteData(
    title = "技术方案与架构评审",
    subtitle = "需求拆解、方案评估、Trade-off 决策、技术文档",
    color = Color.parseColor("#3F51B5"),
    chapters = listOf(
        ChapterItem("1",   "需求拆解"),
        ChapterItem("1.1", "从产品需求推导技术需求，识别技术风险和不确定性"),
        ChapterItem("2",   "方案评估维度"),
        ChapterItem("2.1", "可行性、可维护性、性能、安全、开发成本"),
        ChapterItem("3",   "Trade-off 决策"),
        ChapterItem("3.1", "没有最好的方案，只有最合适当前阶段的方案"),
        ChapterItem("4",   "RFC 文档"),
        ChapterItem("4.1", "Request For Comments，描述背景/方案/替代方案/影响"),
        ChapterItem("5",   "架构评审"),
        ChapterItem("5.1", "Checklist 驱动，覆盖数据流、错误处理、降级策略"),
        ChapterItem("6",   "技术债务管理"),
        ChapterItem("6.1", "量化债务，制定还债计划，纳入迭代排期"),
    )
)
