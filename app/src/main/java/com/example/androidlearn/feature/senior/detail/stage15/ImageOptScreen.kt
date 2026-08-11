package com.example.androidlearn.feature.senior.detail.stage15

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val imageOptData = NoteData(
    title = "图片加载优化",
    subtitle = "Glide/Coil 架构、三级缓存、图片格式选型（WebP/AVIF）与列表图片加载策略",
    color = Color.parseColor("#FF5722"),
    chapters = listOf(
        ChapterItem("1",   "三级缓存"),
        ChapterItem("1.1", "内存缓存（LruCache，访问最快）→ 磁盘缓存（DiskLruCache）→ 网络（最慢，消耗流量）"),
        ChapterItem("2",   "Glide 架构"),
        ChapterItem("2.1", "RequestManager（生命周期绑定）→ Engine（缓存+请求调度）→ DataFetcher → Decoder"),
        ChapterItem("3",   "Coil"),
        ChapterItem("3.1", "纯 Kotlin 图片库，基于协程，支持 Compose，默认使用 OkHttp，比 Glide 更轻量"),
        ChapterItem("4",   "WebP 格式"),
        ChapterItem("4.1", "同质量下比 PNG 小 26%，比 JPEG 小 25-34%，Android 4.0+ 无损支持"),
        ChapterItem("5",   "AVIF"),
        ChapterItem("5.1", "下一代图片格式，压缩率比 WebP 高 50%，Android 12+ 原生支持"),
        ChapterItem("6",   "列表图片加载"),
        ChapterItem("6.1", "在 RecyclerView 滑动时暂停大图加载（Glide.with(rv).pauseRequestsRecursive()），停止后恢复"),
    )
)
