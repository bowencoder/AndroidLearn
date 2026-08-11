package com.example.androidlearn.feature.senior.detail.stage15

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val screenAdaptData = NoteData(
    title = "屏幕适配",
    subtitle = "dp/px/dpi 关系、今日头条方案、smallestWidth 方案与折叠屏/大屏适配",
    color = Color.parseColor("#FF5722"),
    chapters = listOf(
        ChapterItem("1",   "px/dp/dpi 关系"),
        ChapterItem("1.1", "px = dp × (dpi / 160)；DisplayMetrics.density = dpi / 160（如 xxhdpi density=3）"),
        ChapterItem("2",   "sp"),
        ChapterItem("2.1", "与 dp 类似，但会随用户字体大小设置缩放（scaledDensity），应始终用于文字尺寸"),
        ChapterItem("3",   "smallestWidth 方案"),
        ChapterItem("3.1", "为不同 sw（最小宽度）生成对应 values-swXXXdp/dimens.xml，覆盖主流机型"),
        ChapterItem("4",   "今日头条方案"),
        ChapterItem("4.1", "修改 DisplayMetrics.density = 设计稿宽度 / 屏幕实际宽度px，使 1dp = 1设计稿px"),
        ChapterItem("5",   "折叠屏适配"),
        ChapterItem("5.1", "使用 WindowSizeClass 区分 Compact/Medium/Expanded，配合 SlidingPaneLayout 或 NavRail 布局"),
        ChapterItem("6",   "刘海屏/挖孔屏"),
        ChapterItem("6.1", "使用 WindowInsets 处理状态栏/导航栏/刘海区域，避免内容被遮挡"),
    )
)
