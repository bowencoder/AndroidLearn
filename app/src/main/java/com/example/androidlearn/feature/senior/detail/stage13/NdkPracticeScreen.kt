package com.example.androidlearn.feature.senior.detail.stage13

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val ndkPracticeData = NoteData(
    title = "NDK 实战应用",
    subtitle = "音视频处理，图像算法，加解密，跨平台逻辑复用",
    color = Color.parseColor("#546E7A"),
    chapters = listOf(
        ChapterItem("1",   "音视频处理"),
        ChapterItem("1.1", "FFmpeg 集成（解封装、解码、转码），MediaCodec 硬编解码，OpenSL ES 音频"),
        ChapterItem("2",   "图像算法"),
        ChapterItem("2.1", "OpenCV NDK 集成，图像滤镜、人脸检测、矩阵运算，NEON SIMD 指令加速"),
        ChapterItem("3",   "加解密"),
        ChapterItem("3.1", "OpenSSL/BoringSSL NDK 集成，AES/RSA 加解密，HMAC 签名，安全存储密钥"),
        ChapterItem("4",   "跨平台逻辑"),
        ChapterItem("4.1", "将业务逻辑用 C++ 实现，同时供 Android（JNI）和 iOS（OC++ 桥接）调用"),
        ChapterItem("5",   "性能调优"),
        ChapterItem("5.1", "NEON 向量化指令，多线程 pthread，内存对齐，避免 JNI 频繁切换开销"),
        ChapterItem("6",   "崩溃分析"),
        ChapterItem("6.1", "读取 tombstone 文件，用 ndk-stack 解析 Native 崩溃堆栈，addr2line 定位行号"),
    )
)
