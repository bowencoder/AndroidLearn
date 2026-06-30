package com.example.androidlearn.feature.intermediate.detail.stage8

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【AOP 面向切面编程】专属学习页
//  stageIndex=7, topicIndex=0
//  阶段颜色：青色 0xFF00BCD4（中级扩展 Stage 7）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "AOP 面向切面编程",
    description = "字节码插桩、AspectJ、ASM Transform、动态代理",
    overview = "AOP（Aspect-Oriented Programming）允许在不修改原有代码的情况下，统一插入横切逻辑（日志、性能监控、权限检查等），是大型项目工程化的核心手段。",
    keyPoints = listOf(
        "AspectJ：编译期字节码织入，@Aspect 定义切面，@Around/@Before/@After 定义通知",
        "ASM + AGP Transform：轻量字节码操作框架，在编译期对 .class 文件插桩",
        "AsmClassVisitorFactory：AGP 7+ 推荐的字节码变换 API，替代旧 Transform",
        "动态代理（Proxy/InvocationHandler）：运行期拦截接口方法，常用于网络层",
        "Kotlin 委托：by 关键字实现接口委托，轻量 AOP 替代方案",
        "应用场景：方法耗时统计、无埋点行为采集、登录态拦截、性能监控"
    ),
    codeSnippet = """
// AspectJ 切面示例：统计所有 @TrackTime 方法耗时
@Aspect
class TimingAspect {

    @Around("@annotation(com.example.TrackTime)")
    @Throws(Throwable::class)
    fun aroundTrackTime(joinPoint: ProceedingJoinPoint): Any? {
        val start = SystemClock.elapsedRealtime()
        val result = joinPoint.proceed()   // 执行原方法
        val cost = SystemClock.elapsedRealtime() - start
        Log.d("AOP", "${'$'}{joinPoint.signature.name} 耗时: ${'$'}{cost}ms")
        return result
    }
}

// 使用：只需加注解，无需改动方法本身
@TrackTime
fun loadUserData() {
    // 正常业务代码...
}

// ASM 字节码插桩核心思路
class MethodTraceVisitor(cv: ClassVisitor) : ClassVisitor(ASM9, cv) {
    override fun visitMethod(
        access: Int, name: String, desc: String,
        signature: String?, exceptions: Array<String>?
    ): MethodVisitor {
        val mv = super.visitMethod(access, name, desc, signature, exceptions)
        return object : AdviceAdapter(ASM9, mv, access, name, desc) {
            override fun onMethodEnter() {
                // 在方法入口插入：long start = SystemClock.elapsedRealtime()
                visitMethodInsn(INVOKESTATIC,
                    "android/os/SystemClock", "elapsedRealtime", "()J", false)
            }
            override fun onMethodExit(opcode: Int) {
                // 在方法出口插入耗时计算和上报代码
            }
        }
    }
}
    """.trimIndent(),
    tips = listOf(
        "AspectJ 适合快速验证，ASM Transform 更灵活，大厂生产环境首选 ASM",
        "Transform 会增加编译时间，建议只在 Release 或专用 variant 启用",
        "动态代理只能代理接口，若需代理类要用 cglib 或字节码方案"
    )
)

@Composable
fun AopScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF00BCD4),
        stageTitle = "事件与通信机制",
        onBack = onBack
    )
}
