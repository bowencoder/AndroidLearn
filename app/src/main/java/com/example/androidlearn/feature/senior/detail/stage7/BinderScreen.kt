package com.example.androidlearn.feature.senior.detail.stage7

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val binderData = NoteData(
    title = "Binder 机制深度解析",
    subtitle = "一次拷贝原理，ServiceManager，AIDL 全链路",
    color = Color.parseColor("#795548"),
    chapters = listOf(
        ChapterItem("1",   "内核驱动"),
        ChapterItem("1.1", "/dev/binder，通过 mmap 实现发送端→内核→接收端一次拷贝"),
        ChapterItem("2",   "ServiceManager"),
        ChapterItem("2.1", "Binder 的「DNS」，注册与查询系统服务"),
        ChapterItem("3",   "Stub / Proxy"),
        ChapterItem("3.1", "服务端实现 Stub，客户端调用 Proxy，框架自动序列化"),
        ChapterItem("4",   "线程池"),
        ChapterItem("4.1", "Binder 驱动默认为每个进程分配 15+1 个线程处理请求"),
        ChapterItem("5",   "AIDL"),
        ChapterItem("5.1", "Android Interface Definition Language，自动生成跨进程代码"),
        ChapterItem("6",   "linkToDeath"),
        ChapterItem("6.1", "监听远程服务进程死亡，及时重连"),
    )
)
