package com.example.androidlearn.feature.senior.detail.stage14

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val systemArchData = NoteData(
    title = "Android 系统架构与启动",
    subtitle = "Linux 内核→HAL→Android 运行时→系统服务→应用层的分层架构与开机启动链路",
    color = Color.parseColor("#3F51B5"),
    chapters = listOf(
        ChapterItem("1",   "Android 分层"),
        ChapterItem("1.1", "Linux Kernel → HAL → Android Runtime（ART/Bionic）→ Native Libraries → Java Framework → Applications"),
        ChapterItem("2",   "启动链路"),
        ChapterItem("2.1", "Bootloader → Linux Kernel → init 进程（PID=1）→ Zygote → SystemServer → Launcher"),
        ChapterItem("3",   "init 进程"),
        ChapterItem("3.1", "解析 init.rc 脚本，启动 servicemanager、Zygote 等关键 Native 服务"),
        ChapterItem("4",   "Zygote"),
        ChapterItem("4.1", "第一个 Java 进程，预加载常用类和资源，通过 fork() 孵化所有 App 进程"),
        ChapterItem("5",   "SystemServer"),
        ChapterItem("5.1", "在 Zygote 的 fork 中启动，负责 AMS、PMS、WMS、IMS 等数百个系统服务"),
        ChapterItem("6",   "Binder"),
        ChapterItem("6.1", "Android 核心 IPC 机制，基于 Linux 内存映射，一次拷贝实现跨进程通信"),
    )
)
