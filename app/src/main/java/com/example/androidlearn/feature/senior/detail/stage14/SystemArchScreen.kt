package com.example.androidlearn.feature.senior.detail.stage14

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【Android 系统架构与启动】专属学习页
//  stageIndex=13, topicIndex=1
//  阶段颜色：深蓝紫 0xFF3F51B5（系统核心原理）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "Android 系统架构与启动",
    description = "Linux 内核→HAL→Android 运行时→系统服务→应用层的分层架构与开机启动链路",
    overview = "Android 是基于 Linux 内核的分层操作系统。理解从 Bootloader 到桌面启动的完整链路，以及 Zygote、SystemServer、AMS 等核心进程的职责，是深入理解 Android 运行机制的前提。",
    keyPoints = listOf(
        "Android 分层：Linux Kernel → HAL（硬件抽象层）→ Android Runtime（ART/Bionic）→ Native Libraries → Java Framework → Applications",
        "启动链路：Bootloader → Linux Kernel → init 进程（PID=1）→ Zygote → SystemServer → Launcher",
        "init 进程：解析 init.rc 脚本，启动 servicemanager、Zygote 等关键 Native 服务",
        "Zygote：第一个 Java 进程，预加载常用类和资源，通过 fork() 孵化所有 App 进程（写时复制优化内存）",
        "SystemServer：在 Zygote 的 fork 中启动，负责 AMS、PMS、WMS、IMS 等数百个系统服务",
        "Binder：Android 核心 IPC 机制，基于 Linux 内存映射，一次拷贝实现跨进程通信"
    ),
    codeSnippet = """
// Android 启动链路简图
// ┌─────────────────────────────────────────────┐
// │  Bootloader（u-boot）                        │
// │    └─ 加载 Linux Kernel，挂载 ramdisk         │
// ├─────────────────────────────────────────────┤
// │  Linux Kernel                               │
// │    └─ 启动 init 进程（PID=1）                 │
// ├─────────────────────────────────────────────┤
// │  init 进程                                   │
// │    ├─ 解析 /init.rc                          │
// │    ├─ 启动 servicemanager（Binder 的 DNS）    │
// │    └─ 启动 zygote                            │
// ├─────────────────────────────────────────────┤
// │  Zygote（app_process）                       │
// │    ├─ 预加载 framework classes & resources    │
// │    ├─ fork → SystemServer                    │
// │    └─ 等待 AMS 的 fork 请求 → App 进程        │
// ├─────────────────────────────────────────────┤
// │  SystemServer                               │
// │    ├─ ActivityManagerService (AMS)           │
// │    ├─ PackageManagerService (PMS)            │
// │    ├─ WindowManagerService (WMS)             │
// │    └─ 数百个其他系统服务...                   │
// └─────────────────────────────────────────────┘

// App 进程启动（Zygote fork）
// AMS 通过 Socket 向 Zygote 发送 fork 请求
// Zygote.forkAndSpecialize() → 子进程执行 ActivityThread.main()
// ActivityThread.main() → Looper.prepareMainLooper() → attach(bindApplication)

// 查看系统服务
// $ adb shell service list
// $ adb shell dumpsys activity          // AMS 信息
// $ adb shell dumpsys window            // WMS 信息
    """.trimIndent(),
    tips = listOf(
        "Zygote fork 利用 Linux 写时复制（COW），使所有 App 共享 Zygote 预加载的内存页，大幅降低启动内存消耗",
        "SystemServer 启动失败会触发 watchdog 重启，实际设备开机慢通常是 PMS 扫描包或 dex2oat 耗时导致",
        "adb shell cat /proc/1/cmdline 可确认 init 进程，/proc/$(pidof zygote)/maps 可查看 Zygote 内存映射"
    )
)

@Composable
fun SystemArchScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF3F51B5),
        stageTitle = "Android 系统核心原理",
        onBack = onBack
    )
}
