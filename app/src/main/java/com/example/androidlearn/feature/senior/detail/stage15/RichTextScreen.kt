package com.example.androidlearn.feature.senior.detail.stage15

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val richTextData = NoteData(
    title = "富文本渲染",
    subtitle = "Spannable 文本、StaticLayout 自定义排版、Compose AnnotatedString 与复杂图文混排",
    color = Color.parseColor("#FF5722"),
    chapters = listOf(
        ChapterItem("1",   "SpannableString"),
        ChapterItem("1.1", "不可变的带 Span 文本；SpannableStringBuilder：可变的带 Span 文本构建器"),
        ChapterItem("2",   "常用 Span"),
        ChapterItem("2.1", "ForegroundColorSpan（颜色）、StyleSpan（粗体/斜体）、URLSpan（可点击链接）、ImageSpan（图文混排）"),
        ChapterItem("3",   "ClickableSpan"),
        ChapterItem("3.1", "实现文本中可点击区域（如 @用户名、#话题#），需配合 MovementMethod.LinkMovementMethod"),
        ChapterItem("4",   "StaticLayout"),
        ChapterItem("4.1", "手动控制文本换行和布局（用于 Canvas 绘制文本），比 TextView 更底层，可精确控制行高/缩进"),
        ChapterItem("5",   "Compose AnnotatedString"),
        ChapterItem("5.1", "声明式富文本，通过 withStyle 或 pushStringAnnotation 为文本段落添加样式和标注"),
        ChapterItem("6",   "图文混排"),
        ChapterItem("6.1", "ImageSpan（行内图片）；复杂图文可用 RecyclerView 分 Item 类型，或自定义 View 混合绘制"),
    )
)
