package com.example.androidlearn.feature.intermediate.detail.stage5

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * 屏幕适配与 Bitmap
 * 官方文档：https://developer.android.com/training/multiscreen/screendensities
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  屏幕适配基础
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  dp / px 换算 ─────────────────────────────────────────────────────────
 *
 *  · px = dp × (dpi / 160)
 *  · 设计稿通常以 360dp 或 375dp 为基准宽度
 *  · mdpi=160dpi，hdpi=240dpi，xhdpi=320dpi，xxhdpi=480dpi，xxxhdpi=640dpi
 *
 * ── 1.2  今日头条方案 ─────────────────────────────────────────────────────────
 *
 *  · 修改 DisplayMetrics.density，让 1dp = 设计稿 1px
 *  · 会影响系统控件大小，需做好兼容测试
 *
 *  // 在 Application 中设置
 *  val targetDensity = screenWidth / 360f  // 设计稿宽度 360dp
 *  resources.displayMetrics.density = targetDensity
 *  resources.displayMetrics.densityDpi = (targetDensity * 160).toInt()
 *
 * ── 1.3  smallestWidth 方案 ───────────────────────────────────────────────────
 *
 *  · 针对最小宽度提供不同 dimens.xml
 *  · values-sw360dp / values-sw384dp / values-sw411dp / values-sw480dp
 *  · 更稳定但需要维护多套 dimens 文件
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  Bitmap 内存管理
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  内存计算 ─────────────────────────────────────────────────────────────
 *
 *  · 内存 = 宽 × 高 × 每像素字节数
 *  · ARGB_8888：4 字节/像素（默认，质量最高）
 *  · RGB_565：2 字节/像素（无透明通道，内存减半）
 *
 * ── 2.2  inSampleSize 采样 ────────────────────────────────────────────────────
 *
 *  · 加载大图时按 2 的幂次缩小，减少内存占用
 *
 *  fun calculateInSampleSize(options: BitmapFactory.Options, reqW: Int, reqH: Int): Int {
 *      val (h, w) = options.run { outHeight to outWidth }
 *      var inSampleSize = 1
 *      if (h > reqH || w > reqW) {
 *          val halfH = h / 2; val halfW = w / 2
 *          while (halfH / inSampleSize >= reqH && halfW / inSampleSize >= reqW) {
 *              inSampleSize *= 2
 *          }
 *      }
 *      return inSampleSize
 *  }
 *
 *  // 使用
 *  val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
 *  BitmapFactory.decodeFile(path, options)
 *  options.inSampleSize = calculateInSampleSize(options, targetW, targetH)
 *  options.inJustDecodeBounds = false
 *  val bitmap = BitmapFactory.decodeFile(path, options)
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  Compose 适配
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · Compose 默认使用 dp 单位，天然适配不同密度屏幕
 *  · WindowSizeClass：判断手机/平板/折叠屏布局
 *
 *  val windowSizeClass = calculateWindowSizeClass(this)
 *  when (windowSizeClass.widthSizeClass) {
 *      WindowWidthSizeClass.Compact -> PhoneLayout()
 *      WindowWidthSizeClass.Medium -> TabletLayout()
 *      WindowWidthSizeClass.Expanded -> DesktopLayout()
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 今日头条方案会影响系统控件大小，需做好兼容测试
 *  · smallestWidth 方案更稳定但需要维护多套 dimens 文件
 *  · Compose 默认使用 dp 单位，天然适配不同密度屏幕
 */

val screenAdaptBitmapData = NoteData(
    title = "屏幕适配与 Bitmap",
    subtitle = "多媒体与系统能力 · dp/dpi · 今日头条方案 · inSampleSize",
    color = Color.parseColor("#00BCD4"),
    chapters = listOf(
        ChapterItem("1",   "屏幕适配基础"),
        ChapterItem("1.1", "dp / px 换算"),
        ChapterItem("1.2", "今日头条方案"),
        ChapterItem("1.3", "smallestWidth 方案"),
        ChapterItem("2",   "Bitmap 内存管理"),
        ChapterItem("2.1", "内存计算"),
        ChapterItem("2.2", "inSampleSize 采样"),
        ChapterItem("3",   "Compose 适配"),
        ChapterItem("4",   "最佳实践"),
    )
)
