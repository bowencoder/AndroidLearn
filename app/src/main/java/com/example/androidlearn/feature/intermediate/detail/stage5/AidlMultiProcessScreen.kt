package com.example.androidlearn.feature.intermediate.detail.stage5

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * 多进程与 AIDL
 * 官方文档：https://developer.android.com/guide/components/aidl
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  多进程基础
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  多进程配置 ───────────────────────────────────────────────────────────
 *
 *  · AndroidManifest 中 android:process=":remote" 创建独立进程
 *  · 以 ":" 开头的进程名是私有进程，其他 App 无法访问
 *
 *  <service
 *      android:name=".CalcService"
 *      android:process=":remote" />
 *
 * ── 1.2  多进程问题 ───────────────────────────────────────────────────────────
 *
 *  · Application 多次初始化：每个进程都会创建 Application 实例
 *  · 单例失效：不同进程的单例是独立的
 *  · SharedPreferences 不安全：多进程读写会导致数据丢失
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  AIDL 进程间通信
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  定义 AIDL 接口 ───────────────────────────────────────────────────────
 *
 *  // ICalculator.aidl
 *  interface ICalculator {
 *      int add(int a, int b);
 *      List<String> getHistory();
 *  }
 *
 * ── 2.2  Service 端实现 ───────────────────────────────────────────────────────
 *
 *  class CalcService : Service() {
 *      private val binder = object : ICalculator.Stub() {
 *          override fun add(a: Int, b: Int) = a + b
 *          override fun getHistory() = history
 *      }
 *      override fun onBind(intent: Intent) = binder
 *  }
 *
 * ── 2.3  Client 端绑定 ────────────────────────────────────────────────────────
 *
 *  val conn = object : ServiceConnection {
 *      override fun onServiceConnected(name: ComponentName, service: IBinder) {
 *          val calc = ICalculator.Stub.asInterface(service)
 *          val result = calc.add(3, 5)
 *      }
 *      override fun onServiceDisconnected(name: ComponentName) {}
 *  }
 *  bindService(intent, conn, Context.BIND_AUTO_CREATE)
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  Messenger（简单 IPC）
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 基于 AIDL 的简单封装，适合单线程消息传递
 *  · 消息串行处理，不支持并发
 *  · 简单场景用 Messenger，复杂高频接口用 AIDL
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  Parcelable
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · AIDL 中传递自定义对象必须实现 Parcelable 接口
 *  · @Parcelize（Kotlin）：自动生成 Parcelable 实现
 *
 *  @Parcelize
 *  data class User(val id: Int, val name: String) : Parcelable
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · AIDL 方法运行在 Binder 线程池，服务端需处理线程安全问题
 *  · 简单场景用 Messenger，复杂高频接口用 AIDL
 *  · 跨进程传递大数据考虑 Parcelable + ashmem（匿名共享内存）
 */

val aidlMultiProcessData = NoteData(
    title = "多进程与 AIDL",
    subtitle = "多媒体与系统能力 · IPC · AIDL · Messenger",
    color = Color.parseColor("#00BCD4"),
    chapters = listOf(
        ChapterItem("1",   "多进程基础"),
        ChapterItem("1.1", "多进程配置"),
        ChapterItem("1.2", "多进程问题"),
        ChapterItem("2",   "AIDL 进程间通信"),
        ChapterItem("2.1", "定义 AIDL 接口"),
        ChapterItem("2.2", "Service 端实现"),
        ChapterItem("2.3", "Client 端绑定"),
        ChapterItem("3",   "Messenger（简单 IPC）"),
        ChapterItem("4",   "Parcelable"),
        ChapterItem("5",   "最佳实践"),
    )
)
