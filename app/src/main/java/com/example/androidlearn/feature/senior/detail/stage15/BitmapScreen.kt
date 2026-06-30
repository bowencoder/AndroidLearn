package com.example.androidlearn.feature.senior.detail.stage15

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【Bitmap 内存管理】专属学习页
//  stageIndex=14, topicIndex=1
//  阶段颜色：橙红 0xFFFF5722（UI 进阶）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "Bitmap 内存管理",
    description = "Bitmap 内存计算、采样压缩、inBitmap 复用、Bitmap 内存区域演变与 LruCache",
    overview = "Bitmap 是 Android 内存 OOM 的头号元凶。一张 1080×2340 的屏幕截图（ARGB_8888）占用约 9.8MB 内存。合理的 Bitmap 管理策略包括：按需采样加载、复用 Bitmap 内存、及时回收、使用 LruCache 缓存。",
    keyPoints = listOf(
        "内存计算：Bitmap 大小 = 宽 × 高 × 每像素字节数（ARGB_8888=4B，RGB_565=2B，ALPHA_8=1B）",
        "inSampleSize 采样：BitmapFactory.Options.inSampleSize=2 则宽高各缩小一半，内存降为 1/4",
        "inBitmap 复用：Android 3.0+ 支持，需要目标 Bitmap 尺寸 ≥ 候选 Bitmap，避免重复申请 Native 内存",
        "内存区域演变：Android 2.3- 像素数据在 Native Heap；3.0-7.1 移至 Java Heap；8.0+ 再次移回 Native Heap（NativeAllocationRegistry 管理）",
        "LruCache：基于 LinkedHashMap 的 LRU 缓存，以 Bitmap 字节大小为 size，设置 maxSize 为可用内存 1/8",
        "Bitmap 格式选择：照片用 ARGB_8888；图标/简单图用 RGB_565（无透明通道）节省一半内存"
    ),
    codeSnippet = """
// 按需采样加载（避免 OOM）
fun decodeSampledBitmap(res: Resources, resId: Int, reqWidth: Int, reqHeight: Int): Bitmap {
    val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeResource(res, resId, options)  // 只读尺寸，不加载像素
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeResource(res, resId, options)
}

fun calculateInSampleSize(options: BitmapFactory.Options, reqW: Int, reqH: Int): Int {
    val (height, width) = options.run { outHeight to outWidth }
    var inSampleSize = 1
    while (height / inSampleSize > reqH || width / inSampleSize > reqW) {
        inSampleSize *= 2
    }
    return inSampleSize
}

// inBitmap 复用（Android 4.4+ 放开尺寸限制）
val options = BitmapFactory.Options().apply {
    inMutable = true         // 允许被复用
    inBitmap = reusableBitmap  // 提供可复用的 Bitmap
}
val newBitmap = BitmapFactory.decodeFile(path, options)

// LruCache 实现图片内存缓存
val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
val cacheSize = maxMemory / 8
val bitmapCache = object : LruCache<String, Bitmap>(cacheSize) {
    override fun sizeOf(key: String, value: Bitmap) = value.byteCount / 1024
}
bitmapCache.put(key, bitmap)
val cached = bitmapCache.get(key)

// Bitmap 内存计算示例
// 1920 x 1080 图片，ARGB_8888 格式
// 内存 = 1920 × 1080 × 4 = 8,294,400 B ≈ 7.9 MB
// 若 inSampleSize=2：960 × 540 × 4 = 2,073,600 B ≈ 1.98 MB（节省 75%）

// 强制回收（不推荐，现代 GC 自动管理）
if (!bitmap.isRecycled) {
    bitmap.recycle()
}
    """.trimIndent(),
    tips = listOf(
        "Android 8.0+ Bitmap 像素数据在 Native Heap，不会触发 Java OOM，但 Native OOM 同样会崩溃，需用 HardwareCanvas 或 NativeAllocationRegistry 追踪",
        "Glide/Coil 等图片库内置了 Bitmap 池和 LruCache，实际开发中应优先使用成熟库而非手动管理",
        "使用 Bitmap.createScaledBitmap 时注意内存峰值：原图和缩放图会同时存在于内存，应先采样后再缩放"
    )
)

@Composable
fun BitmapScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFFFF5722),
        stageTitle = "Android UI 进阶",
        onBack = onBack
    )
}
