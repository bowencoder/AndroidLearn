package com.example.androidlearn.feature.senior.detail.stage11

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val dalvikArtData = NoteData(
    title = "Android 虚拟机指令",
    subtitle = "Dalvik/ART 指令集解读、深入内存管理、字节码动态替换方案",
    color = Color.parseColor("#009688"),
    chapters = listOf(
        ChapterItem("1",   "Dalvik vs ART"),
        ChapterItem("1.1", "Dalvik JIT 即时编译；ART AOT 预编译 + JIT（Android 7+），启动更快"),
        ChapterItem("2",   "DEX 格式"),
        ChapterItem("2.1", "Dalvik Executable，专为移动端优化的字节码格式，多个类共享常量池"),
        ChapterItem("3",   "Dalvik 指令集"),
        ChapterItem("3.1", "基于寄存器（非 JVM 的栈式），指令更少、执行效率更高"),
        ChapterItem("4",   "smali/baksmali"),
        ChapterItem("4.1", "DEX 的汇编/反汇编工具，用于逆向和热修复分析"),
        ChapterItem("5",   "字节码动态替换"),
        ChapterItem("5.1", "通过替换 ArtMethod 指针实现方法级热修复（Robust 方案）"),
        ChapterItem("6",   "类加载时机"),
        ChapterItem("6.1", "第一次访问时懒加载，ClassLoader 双亲委派保证核心类不被篡改"),
    )
)
