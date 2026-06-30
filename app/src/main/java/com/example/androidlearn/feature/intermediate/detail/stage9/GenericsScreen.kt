package com.example.androidlearn.feature.intermediate.detail.stage9

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【泛型机制与类型擦除】专属学习页
//  stageIndex=8, topicIndex=0
//  阶段颜色：靛蓝 0xFF3F51B5（中级扩展 Stage 8）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "泛型机制与类型擦除",
    description = "泛型擦除、泛型桥方法、PCES 上下边界、星号投影",
    overview = "Java/Kotlin 泛型在编译期提供类型安全检查，但运行时会被擦除。理解类型擦除、桥方法和通配符上下界，是读懂框架源码、编写通用工具类的基础。",
    keyPoints = listOf(
        "类型擦除：编译后泛型信息消失，List<String> 和 List<Int> 运行时都是 List",
        "泛型桥方法：编译器为保持多态一致性自动生成的合成方法",
        "PCES 原则：Producer Extends（生产者用上界 out），Consumer Super（消费者用下界 in）",
        "上界 <? extends T>（Kotlin out T）：只读，不可写，协变",
        "下界 <? super T>（Kotlin in T）：可写，读取为 Object，逆变",
        "reified + inline：Kotlin 通过内联函数保留类型参数，可在运行时获取泛型实际类型"
    ),
    codeSnippet = """
// 类型擦除示例
fun <T> printList(list: List<T>) {
    // 运行时无法判断 list 是 List<String> 还是 List<Int>
    println(list::class)  // class java.util.ArrayList（没有泛型信息）
}

// PCES 原则
fun copy(src: List<out Number>, dst: MutableList<in Number>) {
    dst.addAll(src)   // src 只生产（读），dst 只消费（写）
}

// reified 保留泛型类型（Kotlin 独有）
inline fun <reified T> parseJson(json: String): T {
    return Gson().fromJson(json, T::class.java)  // T::class.java 在运行时可用
}

// 泛型上下界
fun <T : Comparable<T>> maxOf(a: T, b: T): T = if (a > b) a else b
    """.trimIndent(),
    tips = listOf(
        "读框架源码遇到通配符时，记住 PCES：Producer=out/extends，Consumer=in/super",
        "Kotlin 的 reified 本质是将泛型参数内联到调用点，避免了反射开销",
        "不能用 instanceof 判断泛型类型（擦除后是 Object），需要传递 Class<T> 参数"
    )
)

@Composable
fun GenericsScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF3F51B5),
        stageTitle = "泛型、注解与动态编程",
        onBack = onBack
    )
}
