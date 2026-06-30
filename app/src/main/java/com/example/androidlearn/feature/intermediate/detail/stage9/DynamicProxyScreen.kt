package com.example.androidlearn.feature.intermediate.detail.stage9

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【动态代理与编译时处理】专属学习页
//  stageIndex=8, topicIndex=3
//  阶段颜色：靛蓝 0xFF3F51B5（中级扩展 Stage 8）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "动态代理与编译时处理",
    description = "JDK 动态代理、Proxy/InvocationHandler、编译时代码生成",
    overview = "动态代理是 AOP 和框架设计的核心机制，Retrofit 正是用动态代理将接口方法转换为 HTTP 请求；编译时代码生成（kapt/KSP）则将运行时成本转移到编译期。",
    keyPoints = listOf(
        "JDK 动态代理：只能代理接口，Proxy.newProxyInstance() 运行时生成代理类",
        "InvocationHandler：代理所有方法调用的拦截器，method.invoke() 执行原逻辑",
        "CGLIB 代理：字节码子类化，可代理普通类（Android 不内置，需引入依赖）",
        "Retrofit 原理：interface 方法 → ServiceMethod → Call，全程动态代理",
        "编译时代码生成：APT/KSP 在编译期生成源码，运行时无反射开销",
        "Kotlin Poet / JavaPoet：编程式生成类型安全的 Kotlin/Java 代码文件"
    ),
    codeSnippet = """
// JDK 动态代理示例
interface ApiService {
    fun getUser(id: Int): User
}

val proxy = Proxy.newProxyInstance(
    ApiService::class.java.classLoader,
    arrayOf(ApiService::class.java)
) { _, method, args ->
    // 拦截所有方法调用
    println("调用方法: ${'$'}{method.name}，参数: ${'$'}{args?.toList()}")
    // 实际中这里发起网络请求
    User(id = args!![0] as Int, name = "Mock User")
} as ApiService

val user = proxy.getUser(42)  // 触发 InvocationHandler

// Retrofit 核心原理（简化）
class Retrofit {
    fun <T> create(service: Class<T>): T {
        return Proxy.newProxyInstance(
            service.classLoader,
            arrayOf(service)
        ) { _, method, args ->
            // 1. 解析方法上的 @GET/@POST 注解
            val annotation = method.getAnnotation(GET::class.java)
            val url = baseUrl + annotation.value
            // 2. 构建请求并执行
            okHttpClient.newCall(buildRequest(url, method, args)).execute()
        } as T
    }
}
    """.trimIndent(),
    tips = listOf(
        "动态代理只能代理接口，代理普通类需要 CGLIB 或 AspectJ 字节码方案",
        "Retrofit 每次 create() 都用动态代理，相同接口只需 create 一次并缓存",
        "KSP 生成代码比运行时反射快很多，新框架优先选择编译时方案"
    )
)

@Composable
fun DynamicProxyScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF3F51B5),
        stageTitle = "泛型、注解与动态编程",
        onBack = onBack
    )
}
