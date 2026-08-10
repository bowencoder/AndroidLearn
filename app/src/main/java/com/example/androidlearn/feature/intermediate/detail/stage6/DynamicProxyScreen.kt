package com.example.androidlearn.feature.intermediate.detail.stage6

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * 动态代理与编译时处理
 * 官方文档：https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Proxy.html
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  JDK 动态代理
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  核心 API ─────────────────────────────────────────────────────────────
 *
 *  · Proxy.newProxyInstance()：运行时生成代理类，只能代理接口
 *  · InvocationHandler：代理所有方法调用的拦截器
 *  · method.invoke()：执行原逻辑
 *
 * ── 1.2  代码示例 ─────────────────────────────────────────────────────────────
 *
 *  interface ApiService {
 *      fun getUser(id: Int): User
 *  }
 *
 *  val proxy = Proxy.newProxyInstance(
 *      ApiService::class.java.classLoader,
 *      arrayOf(ApiService::class.java)
 *  ) { _, method, args ->
 *      // 拦截所有方法调用
 *      println("调用方法: ${method.name}，参数: ${args?.toList()}")
 *      // 实际中这里发起网络请求
 *      User(id = args!![0] as Int, name = "Mock User")
 *  } as ApiService
 *
 *  val user = proxy.getUser(42)  // 触发 InvocationHandler
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  Retrofit 动态代理原理
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  // Retrofit 核心原理（简化）
 *  class Retrofit {
 *      fun <T> create(service: Class<T>): T {
 *          return Proxy.newProxyInstance(
 *              service.classLoader,
 *              arrayOf(service)
 *          ) { _, method, args ->
 *              // 1. 解析方法上的 @GET/@POST 注解
 *              val annotation = method.getAnnotation(GET::class.java)
 *              val url = baseUrl + annotation.value
 *              // 2. 构建请求并执行
 *              okHttpClient.newCall(buildRequest(url, method, args)).execute()
 *          } as T
 *      }
 *  }
 *
 *  · interface 方法 → ServiceMethod → Call，全程动态代理
 *  · 相同接口只需 create 一次并缓存，避免重复创建代理
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  CGLIB 代理
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 字节码子类化，可代理普通类（不限于接口）
 *  · Android 不内置，需引入依赖
 *  · Spring AOP 默认使用 CGLIB 代理
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  编译时代码生成
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · APT/KSP 在编译期生成源码，运行时无反射开销
 *  · KotlinPoet / JavaPoet：编程式生成类型安全的 Kotlin/Java 代码文件
 *  · 生成的代码在 build/generated/ 目录，可直接查看
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 动态代理只能代理接口，代理普通类需要 CGLIB 或 AspectJ 字节码方案
 *  · Retrofit 每次 create() 都用动态代理，相同接口只需 create 一次并缓存
 *  · KSP 生成代码比运行时反射快很多，新框架优先选择编译时方案
 */

val dynamicProxyData = NoteData(
    title = "动态代理与编译时处理",
    subtitle = "泛型、注解与动态编程 · Proxy · InvocationHandler · KotlinPoet",
    color = Color.parseColor("#3F51B5"),
    chapters = listOf(
        ChapterItem("1",   "JDK 动态代理"),
        ChapterItem("1.1", "核心 API"),
        ChapterItem("1.2", "代码示例"),
        ChapterItem("2",   "Retrofit 动态代理原理"),
        ChapterItem("3",   "CGLIB 代理"),
        ChapterItem("4",   "编译时代码生成"),
        ChapterItem("5",   "最佳实践"),
    )
)
