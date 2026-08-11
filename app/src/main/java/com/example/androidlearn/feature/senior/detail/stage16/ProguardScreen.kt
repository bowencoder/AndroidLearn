package com.example.androidlearn.feature.senior.detail.stage16

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val proguardData = NoteData(
    title = "代码混淆与裁剪",
    subtitle = "R8/ProGuard 工作原理、Keep 规则、资源裁剪与混淆调试（mapping.txt）",
    color = Color.parseColor("#00897B"),
    chapters = listOf(
        ChapterItem("1",   "R8 三大功能"),
        ChapterItem("1.1", "Shrinking（删除未使用代码/资源）+ Obfuscation（重命名类/方法/字段）+ Optimization（方法内联）"),
        ChapterItem("2",   "-keep 规则"),
        ChapterItem("2.1", "-keep class 保留类及成员；-keepclassmembers 只保留成员不保留类名；-keepnames 保留名称但允许裁剪"),
        ChapterItem("3",   "mapping.txt"),
        ChapterItem("3.1", "R8 生成的符号映射文件，用于还原混淆后的崩溃堆栈（retrace）。发布时必须保留此文件"),
        ChapterItem("4",   "资源裁剪（shrinkResources=true）"),
        ChapterItem("4.1", "配合 minifyEnabled 使用，删除代码未引用的资源文件和字符串"),
        ChapterItem("5",   "反射使用的类/方法需要 Keep"),
        ChapterItem("5.1", "通过反射访问的类名在 R8 混淆后会找不到，导致 ClassNotFoundException"),
        ChapterItem("6",   "第三方库 ProGuard 规则"),
        ChapterItem("6.1", "主流库（Retrofit、Gson、Room 等）通常在 AAR 中内置 consumer-rules.pro，自动应用"),
    )
)
