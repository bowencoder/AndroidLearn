package com.example.androidlearn.feature.junior.detail.stage3

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "图片加载",
    description = "Coil（Compose）/ Glide（View 体系）",
    overview = "图片加载库处理缓存、占位图、圆角、网络加载等复杂逻辑，Compose 推荐 Coil，View 体系推荐 Glide。",
    keyPoints = listOf(
        "Coil：Kotlin 优先，协程原生支持，Compose 扩展 AsyncImage",
        "Glide：成熟稳定，功能丰富，支持自定义转换",
        "缓存策略：内存缓存 + 磁盘缓存，减少重复网络请求",
        "占位图：placeholder / error / fallback",
        "圆角/圆形：CircleCropTransformation、RoundedCornersTransformation",
        "GIF / WebP / SVG：各库支持程度不同，按需选择"
    ),
    codeSnippet = """
// Compose + Coil
AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
        .data("https://example.com/image.jpg")
        .crossfade(true)
        .build(),
    contentDescription = "图片",
    placeholder = painterResource(R.drawable.placeholder),
    modifier = Modifier.size(64.dp).clip(CircleShape)
)
    """.trimIndent(),
    tips = listOf(
        "Compose 项目优先选 Coil，与 Kotlin 生态一致",
        "设置合理的 diskCacheSize，避免占用过多存储空间",
        "列表中图片指定固定尺寸，避免布局抖动"
    )
)

@Composable
fun ImageLoadingScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF2196F3),
        stageTitle = "核心UI与数据组件",
        onBack = onBack
    )
}
