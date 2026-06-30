package com.example.androidlearn.feature.senior.detail.stage15

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【富文本渲染】专属学习页
//  stageIndex=14, topicIndex=5
//  阶段颜色：橙红 0xFFFF5722（UI 进阶）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "富文本渲染",
    description = "Spannable 文本、StaticLayout 自定义排版、Compose AnnotatedString 与复杂图文混排",
    overview = "富文本渲染是内容型 App（新闻、笔记、社交）的核心能力。Android 提供了 Spannable/SpannableString 机制为文本片段添加样式，StaticLayout/DynamicLayout 实现自定义文本排版，Compose 则提供了 AnnotatedString 和 BasicText 的声明式富文本方案。",
    keyPoints = listOf(
        "SpannableString：不可变的带 Span 文本；SpannableStringBuilder：可变的带 Span 文本构建器",
        "常用 Span：ForegroundColorSpan（颜色）、StyleSpan（粗体/斜体）、URLSpan（可点击链接）、ImageSpan（图文混排）、RelativeSizeSpan（相对字号）",
        "ClickableSpan：实现文本中可点击区域（如 @用户名、#话题#），需配合 MovementMethod.LinkMovementMethod",
        "StaticLayout：手动控制文本换行和布局（用于 Canvas 绘制文本），比 TextView 更底层，可精确控制行高/缩进",
        "Compose AnnotatedString：声明式富文本，通过 withStyle 或 pushStringAnnotation 为文本段落添加样式和标注",
        "图文混排：ImageSpan（行内图片）；复杂图文可用 RecyclerView 分 Item 类型，或自定义 View 混合绘制"
    ),
    codeSnippet = """
// SpannableStringBuilder 基础用法
val ssb = SpannableStringBuilder("Hello, @Android 用户！")

// 添加颜色 Span
ssb.setSpan(
    ForegroundColorSpan(Color.BLUE),
    7, 14,  // "@Android" 的范围
    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
)

// 添加可点击 Span
ssb.setSpan(object : ClickableSpan() {
    override fun onClick(widget: View) {
        Toast.makeText(widget.context, "点击了 @Android", Toast.LENGTH_SHORT).show()
    }
    override fun updateDrawState(ds: TextPaint) {
        ds.color = Color.BLUE
        ds.isUnderlineText = false  // 去掉下划线
    }
}, 7, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

textView.text = ssb
textView.movementMethod = LinkMovementMethod.getInstance()  // 必须设置才能响应点击

// 图文混排（ImageSpan）
val drawable = ContextCompat.getDrawable(context, R.drawable.ic_emoji)!!
drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
val imageSpan = ImageSpan(drawable, ImageSpan.ALIGN_BASELINE)
val ssb2 = SpannableStringBuilder("喜欢[emoji]这里的内容")
ssb2.setSpan(imageSpan, 3, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

// StaticLayout（自定义 Canvas 绘制文本）
val paint = TextPaint().apply { textSize = 48f }
val layout = StaticLayout.Builder
    .obtain("这是一段很长的文字内容，需要换行处理...", 0, text.length, paint, 600)
    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
    .setLineSpacingExtra(8f)
    .setMaxLines(5)
    .setEllipsize(TextUtils.TruncateAt.END)
    .build()
// 在 onDraw 中绘制
override fun onDraw(canvas: Canvas) {
    canvas.save()
    canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())
    layout.draw(canvas)
    canvas.restore()
}

// Compose AnnotatedString（声明式富文本）
val annotatedText = buildAnnotatedString {
    withStyle(style = SpanStyle(color = Color.Black, fontSize = 16.sp)) {
        append("普通文字，")
    }
    pushStringAnnotation(tag = "user", annotation = "android_user")
    withStyle(style = SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold)) {
        append("@Android 用户")
    }
    pop()
}
ClickableText(
    text = annotatedText,
    onClick = { offset ->
        annotatedText.getStringAnnotations("user", offset, offset)
            .firstOrNull()?.let { annotation -> /* 处理点击 */ }
    }
)
    """.trimIndent(),
    tips = listOf(
        "Span 的 flags 参数（SPAN_EXCLUSIVE_EXCLUSIVE/INCLUSIVE_INCLUSIVE）控制文本插入时 Span 是否扩展，富文本编辑器中需格外注意",
        "大量 Span 会增加 TextView 的测量/绘制开销，超过数千个 Span 时考虑用 Canvas 自绘或分段渲染",
        "Compose 的 BasicTextField 和 AnnotatedString 组合可实现功能完整的富文本编辑器，参考 Twitter 的 Compose RTE 实现"
    )
)

@Composable
fun RichTextScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFFFF5722),
        stageTitle = "Android UI 进阶",
        onBack = onBack
    )
}
