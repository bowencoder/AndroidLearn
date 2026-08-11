package com.example.androidlearn.feature.senior.detail.stage11

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val objectStructureData = NoteData(
    title = "类与对象内存结构",
    subtitle = "klass 内存分布、markword 数据分析、对象分配过程、逃逸分析",
    color = Color.parseColor("#009688"),
    chapters = listOf(
        ChapterItem("1",   "对象头（Object Header）"),
        ChapterItem("1.1", "markword（8字节）+ klass pointer（4/8字节）"),
        ChapterItem("2",   "markword"),
        ChapterItem("2.1", "存储锁状态/GC年龄/hashCode等，64位系统占 8 字节，随锁状态变化"),
        ChapterItem("3",   "klass pointer"),
        ChapterItem("3.1", "指向方法区中的类元数据（Class 对象），描述类型信息"),
        ChapterItem("4",   "方法表（vtable）"),
        ChapterItem("4.1", "类的虚方法表，实现多态，子类方法覆盖父类对应槽位"),
        ChapterItem("5",   "对象分配"),
        ChapterItem("5.1", "TLAB 无锁快速分配 → 失败则 CAS → 大对象直接进 Old 区"),
        ChapterItem("6",   "逃逸分析"),
        ChapterItem("6.1", "JVM 判断对象是否逃出方法范围，未逃逸则栈上分配（避免 GC）"),
    )
)
