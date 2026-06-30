package com.example.androidlearn.feature.intermediate.detail.stage5

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "屏幕适配与 Bitmap",
    description = "dp/dpi 换算 · 今日头条方案 · smallestWidth · inSampleSize 采样",
    overview = "Android 设备屏幕尺寸和分辨率碎片化严重，良好的屏幕适配是保证用户体验一致性的关键。同时，Bitmap 的内存管理直接影响 App 的稳定性。",
    keyPoints = listOf(
        "dp/px 换算：px = dp × (dpi / 160)，设计稿通常以 360dp 或 375dp 为基准",
        "今日头条方案：修改 DisplayMetrics.density，让 1dp = 设计稿 1px",
        "smallestWidth 方案：针对最小宽度提供不同 dimens.xml（values-sw360dp 等）",
        "Bitmap 内存：宽×高×每像素字节数，ARGB_8888=4B/px，RGB_565=2B/px",
        "inSampleSize 采样：加载大图时按 2 的幂次缩小，减少内存占用",
        "Compose 适配：使用 WindowSizeClass 判断手机/平板/折叠屏布局"
    ),
    codeSnippet = """
// 计算 inSampleSize
fun calculateInSampleSize(options: BitmapFactory.Options, reqW: Int, reqH: Int): Int {
    val (h, w) = options.run { outHeight to outWidth }
    var inSampleSize = 1
    if (h > reqH || w > reqW) {
        val halfH = h / 2
        val halfW = w / 2
        while (halfH / inSampleSize >= reqH && halfW / inSampleSize >= reqW) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}

// 使用
val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
BitmapFactory.decodeFile(path, options)
options.inSampleSize = calculateInSampleSize(options, targetW, targetH)
options.inJustDecodeBounds = false
val bitmap = BitmapFactory.decodeFile(path, options)

// 今日头条方案（在 Application 中）
val targetDensity = screenWidth / 360f  // 设计稿宽度 360dp
resources.displayMetrics.density = targetDensity
resources.displayMetrics.densityDpi = (targetDensity * 160).toInt()
    """.trimIndent(),
    tips = listOf(
        "今日头条方案会影响系统控件大小，需做好兼容测试",
        "smallestWidth 方案更稳定但需要维护多套 dimens 文件",
        "Compose 默认使用 dp 单位，天然适配不同密度屏幕"
    )
)

@Composable
fun ScreenAdaptBitmapScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF00BCD4),
        stageTitle = "多媒体与系统能力",
        onBack = onBack
    )
}
