package com.example.androidlearn.feature.senior.detail.stage15

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val bitmapData = NoteData(
    title = "Bitmap 内存管理",
    subtitle = "Bitmap 内存计算、采样压缩、inBitmap 复用、Bitmap 内存区域演变与 LruCache",
    color = Color.parseColor("#FF5722"),
    chapters = listOf(
        ChapterItem("1",   "内存计算"),
        ChapterItem("1.1", "Bitmap 大小 = 宽 × 高 × 每像素字节数（ARGB_8888=4B，RGB_565=2B，ALPHA_8=1B）"),
        ChapterItem("2",   "inSampleSize 采样"),
        ChapterItem("2.1", "BitmapFactory.Options.inSampleSize=2 则宽高各缩小一半，内存降为 1/4"),
        ChapterItem("3",   "inBitmap 复用"),
        ChapterItem("3.1", "Android 3.0+ 支持，需要目标 Bitmap 尺寸 ≥ 候选 Bitmap，避免重复申请 Native 内存"),
        ChapterItem("4",   "内存区域演变"),
        ChapterItem("4.1", "Android 2.3- 像素数据在 Native Heap；3.0-7.1 移至 Java Heap；8.0+ 再次移回 Native Heap"),
        ChapterItem("5",   "LruCache"),
        ChapterItem("5.1", "基于 LinkedHashMap 的 LRU 缓存，以 Bitmap 字节大小为 size，设置 maxSize 为可用内存 1/8"),
        ChapterItem("6",   "Bitmap 格式选择"),
        ChapterItem("6.1", "照片用 ARGB_8888；图标/简单图用 RGB_565（无透明通道）节省一半内存"),
    )
)
