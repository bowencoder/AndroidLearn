package com.example.androidlearn.feature.senior.detail.stage15

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【图片加载优化】专属学习页
//  stageIndex=14, topicIndex=2
//  阶段颜色：橙红 0xFFFF5722（UI 进阶）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "图片加载优化",
    description = "Glide/Coil 架构、三级缓存、图片格式选型（WebP/AVIF）与列表图片加载策略",
    overview = "图片加载是 Android 性能优化的重要课题。主流图片库（Glide、Coil）提供了完善的三级缓存（内存 → 磁盘 → 网络）、生命周期绑定和 Bitmap 复用机制。了解其内部架构和最佳实践，能有效避免 OOM、列表卡顿和流量浪费。",
    keyPoints = listOf(
        "三级缓存：内存缓存（LruCache，访问最快）→ 磁盘缓存（DiskLruCache，跨进程持久化）→ 网络（最慢，消耗流量）",
        "Glide 架构：RequestManager（生命周期绑定）→ Engine（缓存+请求调度）→ DataFetcher（网络/本地）→ Decoder（Bitmap 解码）",
        "Coil：纯 Kotlin 图片库，基于协程，支持 Compose，默认使用 OkHttp，比 Glide 更轻量",
        "WebP 格式：同质量下比 PNG 小 26%，比 JPEG 小 25-34%，Android 4.0+ 无损支持，4.2.1+ 有损+透明支持",
        "AVIF：下一代图片格式，压缩率比 WebP 高 50%，Android 12+ 原生支持",
        "列表图片加载：在 RecyclerView 滑动时暂停大图加载（Glide.with(rv).pauseRequestsRecursive()），停止后恢复"
    ),
    codeSnippet = """
// Glide 基础用法（支持生命周期自动取消）
Glide.with(fragment)           // 绑定 Fragment 生命周期
    .load(url)
    .placeholder(R.drawable.loading)
    .error(R.drawable.error)
    .override(200, 200)        // 限制解码尺寸（避免大图加载到小 ImageView）
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .into(imageView)

// Coil（Compose 集成）
AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
        .data(url)
        .crossfade(true)
        .size(200, 200)
        .build(),
    contentDescription = "图片",
    contentScale = ContentScale.Crop,
    modifier = Modifier.size(100.dp)
)

// 预加载（用户即将看到时提前加载）
Glide.with(context).load(url).preload(200, 200)

// RecyclerView 滑动优化
recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            Glide.with(context).resumeRequests()
        } else {
            Glide.with(context).pauseRequests()
        }
    }
})

// Glide 自定义磁盘缓存目录和大小
val glide = Glide.get(context)
// 通过 AppGlideModule 配置
@GlideModule
class CustomGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setDiskCache(
            InternalCacheDiskCacheFactory(context, "image_cache", 250 * 1024 * 1024L)
        )
        builder.setMemoryCache(LruResourceCache(50 * 1024 * 1024L))
    }
}
    """.trimIndent(),
    tips = listOf(
        "始终使用 override() 限制加载尺寸，与 ImageView 实际显示大小匹配，避免加载超大 Bitmap 到小 View",
        "WebP 格式在 Android 端已足够普及，新项目建议将所有本地图片资源转换为 WebP，可节省 30%+ 包体积",
        "Glide 的 with(applicationContext) 不绑定生命周期，在 Service 或后台任务中使用，但需手动取消以防内存泄漏"
    )
)

@Composable
fun ImageOptScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFFFF5722),
        stageTitle = "Android UI 进阶",
        onBack = onBack
    )
}
