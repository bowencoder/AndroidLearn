package com.example.androidlearn.feature.intermediate.detail.stage9

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【反射机制】专属学习页
//  stageIndex=8, topicIndex=2
//  阶段颜色：靛蓝 0xFF3F51B5（中级扩展 Stage 8）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "反射机制",
    description = "Class/Field/Method 反射 API、访问私有成员、性能优化",
    overview = "反射允许程序在运行时检查和操作类的结构，是框架、测试工具和热修复的基础。Kotlin 对 Java 反射进行了封装，提供更简洁的 KClass API。",
    keyPoints = listOf(
        "Class 对象：类型信息的载体，obj::class.java 或 Class.forName() 获取",
        "Field 操作：getDeclaredField() + setAccessible(true) 访问私有字段",
        "Method 调用：getDeclaredMethod() + invoke() 动态调用方法",
        "Constructor：newInstance() 反射创建对象",
        "Kotlin KClass：::class 获取 KClass，memberProperties/memberFunctions 访问成员",
        "性能优化：缓存 Field/Method 对象，避免重复查找；Method.setAccessible 有安全开销"
    ),
    codeSnippet = """
// Java 反射访问私有字段（热修复/测试常用）
fun setPrivateField(obj: Any, fieldName: String, value: Any?) {
    val field = obj::class.java.getDeclaredField(fieldName)
    field.isAccessible = true
    field.set(obj, value)
}

// 动态调用方法
fun invokeMethod(obj: Any, methodName: String, vararg args: Any?): Any? {
    val method = obj::class.java.getDeclaredMethod(
        methodName,
        *args.map { it?.javaClass ?: Any::class.java }.toTypedArray()
    )
    method.isAccessible = true
    return method.invoke(obj, *args)
}

// Kotlin 反射访问属性
data class User(val name: String, private val age: Int)

val user = User("Alice", 25)
val ageProp = User::class.memberProperties
    .first { it.name == "age" }
// 遍历所有属性
User::class.memberProperties.forEach { prop ->
    println("${'$'}{prop.name} = ${'$'}{prop.get(user)}")
}
    """.trimIndent(),
    tips = listOf(
        "反射性能比直接调用慢 10-50 倍，高频调用路径应缓存 Field/Method 对象",
        "Android 9+ 对非公开 API 的反射有限制（灰名单），需要元反射绕过",
        "单元测试中反射访问私有成员是合理用法，生产代码尽量避免"
    )
)

@Composable
fun ReflectionScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF3F51B5),
        stageTitle = "泛型、注解与动态编程",
        onBack = onBack
    )
}
