package com.example.androidlearn.feature.senior.detail.stage10

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val synchronizedData = NoteData(
    title = "synchronized 锁机制",
    subtitle = "CPU 物理核架构、Monitor 对象、锁升级（偏向→轻量→重量）、临界区",
    color = Color.parseColor("#E91E63"),
    chapters = listOf(
        ChapterItem("1",   "CPU 物理核架构"),
        ChapterItem("1.1", "多核共享 L3 缓存，缓存一致性协议（MESI）保证数据同步"),
        ChapterItem("2",   "Monitor 对象"),
        ChapterItem("2.1", "每个 Java 对象关联一个 Monitor，monitorenter/monitorexit 字节码"),
        ChapterItem("3",   "偏向锁"),
        ChapterItem("3.1", "无竞争时记录线程 ID 到 markword，无需 CAS，效率最高"),
        ChapterItem("4",   "轻量级锁"),
        ChapterItem("4.1", "有竞争时升级，CAS 将锁记录写入 markword，失败则再升级"),
        ChapterItem("5",   "重量级锁"),
        ChapterItem("5.1", "竞争激烈时升级，阻塞等待，操作系统级别的 mutex"),
        ChapterItem("6",   "锁升级规则"),
        ChapterItem("6.1", "锁只能升级不能降级（JVM 默认），偏向锁撤销有一定开销"),
    )
)
