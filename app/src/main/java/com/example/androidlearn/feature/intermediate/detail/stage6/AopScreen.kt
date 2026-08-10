package com.example.androidlearn.feature.intermediate.detail.stage6

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * AOP 面向切面编程
 * 官方文档：https://developer.android.com/build/extend-agp
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  AspectJ
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  核心概念 ─────────────────────────────────────────────────────────────
 *
 *  · @Aspect：定义切面类
 *  · @Around：环绕通知，可在方法前后插入逻辑
 *  · @Before / @After：方法执行前/后通知
 *  · Pointcut：切入点表达式，指定拦截哪些方法
 *
 * ── 1.2  示例：统计方法耗时 ───────────────────────────────────────────────────
 *
 *  @Aspect
 *  class TimingAspect {
 *
 *      @Around("@annotation(com.example.TrackTime)")
 *      @Throws(Throwable::class)
 *      fun aroundTrackTime(joinPoint: ProceedingJoinPoint): Any? {
 *          val start = SystemClock.elapsedRealtime()
 *          val result = joinPoint.proceed()   // 执行原方法
 *          val cost = SystemClock.elapsedRealtime() - start
 *          Log.d("AOP", "${joinPoint.signature.name} 耗时: ${cost}ms")
 *          return result
 *      }
 *  }
 *
 *  // 使用：只需加注解，无需改动方法本身
 *  @TrackTime
 *  fun loadUserData() { ... }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  ASM 字节码插桩
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  核心思路 ─────────────────────────────────────────────────────────────
 *
 *  · 轻量字节码操作框架，在编译期对 .class 文件插桩
 *  · AsmClassVisitorFactory：AGP 7+ 推荐的字节码变换 API
 *
 * ── 2.2  MethodVisitor 示例 ───────────────────────────────────────────────────
 *
 *  class MethodTraceVisitor(cv: ClassVisitor) : ClassVisitor(ASM9, cv) {
 *      override fun visitMethod(
 *          access: Int, name: String, desc: String,
 *          signature: String?, exceptions: Array<String>?
 *      ): MethodVisitor {
 *          val mv = super.visitMethod(access, name, desc, signature, exceptions)
 *          return object : AdviceAdapter(ASM9, mv, access, name, desc) {
 *              override fun onMethodEnter() {
 *                  // 在方法入口插入代码
 *              }
 *              override fun onMethodExit(opcode: Int) {
 *                  // 在方法出口插入代码
 *              }
 *          }
 *      }
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  动态代理（运行时 AOP）
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · Proxy / InvocationHandler：运行期拦截接口方法
 *  · 只能代理接口，若需代理类要用 cglib 或字节码方案
 *  · 常用于网络层（Retrofit 原理）
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  Kotlin 委托（轻量 AOP）
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · by 关键字实现接口委托，轻量 AOP 替代方案
 *
 *  class LoggingList<T>(private val inner: MutableList<T>) : MutableList<T> by inner {
 *      override fun add(element: T): Boolean {
 *          Log.d("List", "add: $element")
 *          return inner.add(element)
 *      }
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  应用场景
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 方法耗时统计
 *  · 无埋点行为采集
 *  · 登录态拦截（未登录跳转登录页）
 *  · 性能监控
 *  · 权限检查
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  6  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · AspectJ 适合快速验证，ASM Transform 更灵活，大厂生产环境首选 ASM
 *  · Transform 会增加编译时间，建议只在 Release 或专用 variant 启用
 *  · 动态代理只能代理接口，若需代理类要用 cglib 或字节码方案
 */

val aopData = NoteData(
    title = "AOP 面向切面编程",
    subtitle = "事件机制与动态编程 · AspectJ · ASM · 动态代理",
    color = Color.parseColor("#3F51B5"),
    chapters = listOf(
        ChapterItem("1",   "AspectJ"),
        ChapterItem("1.1", "核心概念"),
        ChapterItem("1.2", "示例：统计方法耗时"),
        ChapterItem("2",   "ASM 字节码插桩"),
        ChapterItem("2.1", "核心思路"),
        ChapterItem("2.2", "MethodVisitor 示例"),
        ChapterItem("3",   "动态代理（运行时 AOP）"),
        ChapterItem("4",   "Kotlin 委托（轻量 AOP）"),
        ChapterItem("5",   "应用场景"),
        ChapterItem("6",   "最佳实践"),
    )
)
