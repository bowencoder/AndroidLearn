package com.example.androidlearn.feature.senior.detail.stage16

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val serializationData = NoteData(
    title = "序列化框架",
    subtitle = "Serializable vs Parcelable vs Protobuf vs kotlinx.serialization 性能对比与选型",
    color = Color.parseColor("#00897B"),
    chapters = listOf(
        ChapterItem("1",   "Serializable（Java 标准）"),
        ChapterItem("1.1", "实现简单（implements Serializable），但基于反射，性能差（约是 Parcelable 10倍慢）"),
        ChapterItem("2",   "Parcelable（Android 专用）"),
        ChapterItem("2.1", "手动或注解生成序列化代码，性能极高（基于内存共享），适合 Intent/Binder 数据传递"),
        ChapterItem("3",   "Gson/Moshi/kotlinx.serialization"),
        ChapterItem("3.1", "JSON 序列化库，用于网络数据解析；kotlinx.serialization 支持 K2 编译器和 Multiplatform"),
        ChapterItem("4",   "Protobuf（Protocol Buffers）"),
        ChapterItem("4.1", "Google 开源的二进制序列化协议，体积小（比 JSON 小 3-10x）、速度快，适合大数据量场景"),
        ChapterItem("5",   "@Parcelize（Kotlin）"),
        ChapterItem("5.1", "Kotlin 编译器插件，自动生成 Parcelable 实现，一行注解替代数十行模板代码"),
        ChapterItem("6",   "DataStore（替代 SharedPreferences）"),
        ChapterItem("6.1", "基于 Protobuf 或 Preferences 的异步数据持久化，支持协程，线程安全"),
    )
)
