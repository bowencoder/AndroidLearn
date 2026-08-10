package com.example.androidlearn.feature.junior.detail.stage3

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/*
 * 图片加载笔记
 * Coil 文档：https://coil-kt.github.io/coil/
 * Glide 文档：https://bumptech.github.io/glide/
 *
 * ── 1  图片加载库选型 ─────────────────────────────────────────────────────────
 *
 *  Coil（推荐 Compose 项目）：
 *  · Kotlin 优先，协程原生支持，体积小（~2MB）
 *  · 原生支持 Compose：AsyncImage / rememberAsyncImagePainter
 *  · 支持 GIF、SVG、视频帧
 *
 *  Glide（推荐 View 体系项目）：
 *  · 成熟稳定，社区生态丰富，功能全面
 *  · 支持自定义 Transformation、RequestListener
 *  · 支持 GIF、WebP
 *
 *  // 依赖（build.gradle.kts）
 *  // Coil
 *  implementation("io.coil-kt:coil-compose:2.6.0")
 *  implementation("io.coil-kt:coil-gif:2.6.0")      // GIF 支持
 *  implementation("io.coil-kt:coil-svg:2.6.0")      // SVG 支持
 *
 *  // Glide
 *  implementation("com.github.bumptech.glide:glide:4.16.0")
 *  implementation("com.github.bumptech.glide:compose:1.0.0-beta01")  // Compose 扩展
 *
 *
 * ── 2  Coil 基础用法（Compose）────────────────────────────────────────────────
 *
 *  // 最简用法
 *  AsyncImage(
 *      model = "https://example.com/image.jpg",
 *      contentDescription = "图片描述",
 *      modifier = Modifier.size(120.dp)
 *  )
 *
 *  // 完整配置
 *  AsyncImage(
 *      model = ImageRequest.Builder(LocalContext.current)
 *          .data("https://example.com/image.jpg")
 *          .crossfade(true)                    // 淡入动画
 *          .crossfade(300)                     // 自定义淡入时长（ms）
 *          .memoryCachePolicy(CachePolicy.ENABLED)
 *          .diskCachePolicy(CachePolicy.ENABLED)
 *          .build(),
 *      contentDescription = "图片",
 *      placeholder = painterResource(R.drawable.ic_placeholder),  // 加载中占位
 *      error = painterResource(R.drawable.ic_error),              // 加载失败
 *      fallback = painterResource(R.drawable.ic_fallback),        // data 为 null
 *      contentScale = ContentScale.Crop,
 *      modifier = Modifier
 *          .size(120.dp)
 *          .clip(CircleShape)                  // 圆形裁剪
 *  )
 *
 *  // 作为 Painter 使用（更灵活）
 *  val painter = rememberAsyncImagePainter(
 *      model = "https://example.com/image.jpg"
 *  )
 *  Image(painter = painter, contentDescription = null)
 *
 *  // 监听加载状态
 *  val painter = rememberAsyncImagePainter(
 *      model = "https://example.com/image.jpg",
 *      onSuccess = { /* 加载成功 */ },
 *      onError = { /* 加载失败 */ }
 *  )
 *
 *
 * ── 3  Glide 基础用法（View 体系）────────────────────────────────────────────
 *
 *  // 基础加载
 *  Glide.with(context)
 *      .load("https://example.com/image.jpg")
 *      .into(imageView)
 *
 *  // 完整配置
 *  Glide.with(context)
 *      .load(url)
 *      .placeholder(R.drawable.ic_placeholder)
 *      .error(R.drawable.ic_error)
 *      .centerCrop()
 *      .override(300, 300)                     // 指定目标尺寸，节省内存
 *      .diskCacheStrategy(DiskCacheStrategy.ALL)
 *      .into(imageView)
 *
 *  // 圆形裁剪
 *  Glide.with(context).load(url).circleCrop().into(imageView)
 *
 *  // 圆角
 *  Glide.with(context)
 *      .load(url)
 *      .transform(RoundedCorners(16))
 *      .into(imageView)
 *
 *  // 加载 GIF
 *  Glide.with(context).asGif().load(url).into(imageView)
 *
 *  // 监听加载结果
 *  Glide.with(context)
 *      .load(url)
 *      .listener(object : RequestListener<Drawable> {
 *          override fun onLoadFailed(e: GlideException?, ...) = false
 *          override fun onResourceReady(resource: Drawable, ...) = false
 *      })
 *      .into(imageView)
 *
 *
 * ── 4  缓存策略 ───────────────────────────────────────────────────────────────
 *
 *  Coil 缓存层级：
 *  · 内存缓存（MemoryCache）：LRU，默认 25% 可用内存
 *  · 磁盘缓存（DiskCache）：默认 250MB
 *
 *  // 自定义 Coil 全局配置（Application 中）
 *  val imageLoader = ImageLoader.Builder(context)
 *      .memoryCache {
 *          MemoryCache.Builder(context).maxSizePercent(0.25).build()
 *      }
 *      .diskCache {
 *          DiskCache.Builder()
 *              .directory(context.cacheDir.resolve("image_cache"))
 *              .maxSizeBytes(100 * 1024 * 1024)  // 100MB
 *              .build()
 *      }
 *      .build()
 *  Coil.setImageLoader(imageLoader)
 *
 *  Glide 缓存策略（DiskCacheStrategy）：
 *  · NONE：不缓存
 *  · DATA：只缓存原始数据
 *  · RESOURCE：只缓存处理后的图片
 *  · ALL：缓存原始数据和处理后的图片（默认）
 *  · AUTOMATIC：根据数据源自动选择
 *
 *
 * ── 5  图片变换（Transformation）─────────────────────────────────────────────
 *
 *  Coil（Compose 中用 Modifier）：
 *  // 圆形
 *  Modifier.clip(CircleShape)
 *  // 圆角
 *  Modifier.clip(RoundedCornerShape(12.dp))
 *  // 模糊（需 coil-transformations 库）
 *  ImageRequest.Builder(context).transformations(BlurTransformation(radius = 10f))
 *
 *  Glide（RequestOptions）：
 *  // 多重变换
 *  Glide.with(context)
 *      .load(url)
 *      .transform(MultiTransformation(CenterCrop(), RoundedCorners(16)))
 *      .into(imageView)
 *
 *
 * ── 6  列表中的图片优化 ───────────────────────────────────────────────────────
 *
 *  · 指定固定尺寸（override / Modifier.size），避免布局抖动
 *  · 使用 contentScale = ContentScale.Crop 而非 FillBounds
 *  · RecyclerView 滚动时暂停加载（Glide 自动处理，Coil 无需手动）
 *  · 列表 item 中避免加载超大原图，用 override() 降采样
 *
 *  // Glide 与 RecyclerView 集成（自动暂停/恢复）
 *  Glide.with(recyclerView.context).load(url).into(imageView)
 *  // Glide 会自动监听 RecyclerView 滚动状态
 *
 *  // Coil 在 LazyColumn 中
 *  LazyColumn {
 *      items(urls) { url ->
 *          AsyncImage(
 *              model = url,
 *              contentDescription = null,
 *              modifier = Modifier.fillMaxWidth().height(200.dp),  // 固定高度
 *              contentScale = ContentScale.Crop
 *          )
 *      }
 *  }
 *
 *
 * ── 7  最佳实践 ───────────────────────────────────────────────────────────────
 *
 *  · Compose 项目优先选 Coil，与 Kotlin/协程生态一致
 *  · View 体系项目用 Glide，生态成熟，文档完善
 *  · 列表中图片指定固定尺寸，避免布局抖动和内存浪费
 *  · 合理设置磁盘缓存大小，避免占用过多存储空间
 *  · 不要在主线程手动 decode Bitmap，交给图片库处理
 *  · 大图加载用 override() 降采样，防止 OOM
 *  · 敏感图片（头像等）设置 memoryCachePolicy(DISABLED) 避免缓存泄露
 */

private val Teal = Color(0xFF009688)

private data class ImageChapter(val num: String, val title: String)

private val chapters = listOf(
    ImageChapter("1", "图片加载库选型"),
    ImageChapter("2", "Coil 基础用法（Compose）"),
    ImageChapter("3", "Glide 基础用法（View 体系）"),
    ImageChapter("4", "缓存策略"),
    ImageChapter("5", "图片变换（Transformation）"),
    ImageChapter("6", "列表中的图片优化"),
    ImageChapter("7", "最佳实践"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageLoadingScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("图片加载", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        Text(
                            "Coil · Glide · 缓存策略 · 变换",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "返回", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Teal,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(chapters.size) { i -> ChapterRowImage(chapters[i]) }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun ChapterRowImage(chapter: ImageChapter) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Teal.copy(alpha = 0.12f)
            ) {
                Text(
                    chapter.num,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Teal
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(chapter.title, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}
