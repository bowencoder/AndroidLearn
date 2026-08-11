package com.example.androidlearn.feature.senior.detail.stage10

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val nioData = NoteData(
    title = "高效 IO 与序列化",
    subtitle = "NIO 内核机制、epoll、零拷贝、Java 序列化原理与性能对比",
    color = Color.parseColor("#E91E63"),
    chapters = listOf(
        ChapterItem("1",   "BIO vs NIO"),
        ChapterItem("1.1", "BIO 阻塞等待（一线程一连接），NIO 非阻塞（Selector 多路复用）"),
        ChapterItem("2",   "Selector + Channel"),
        ChapterItem("2.1", "一个 Selector 监听多个 Channel 事件，适合高并发连接"),
        ChapterItem("3",   "epoll（Linux）"),
        ChapterItem("3.1", "事件驱动，只通知就绪的 FD，O(1) 复杂度，Android/OkHttp 底层"),
        ChapterItem("4",   "零拷贝"),
        ChapterItem("4.1", "transferTo() 直接 DMA 传输，避免内核态↔用户态数据复制，减少 CPU 负担"),
        ChapterItem("5",   "序列化对比"),
        ChapterItem("5.1", "Java 序列化（慢/大）< JSON（易读）< Protobuf（快/小）< FlatBuffers（零解析）"),
        ChapterItem("6",   "Android 序列化"),
        ChapterItem("6.1", "Parcelable（IPC/内存高效）> Serializable（简单，频繁 GC）"),
    )
)
