package com.example.androidlearn.feature.intermediate.detail.stage6

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * 泛型机制与类型擦除
 * 官方文档：https://kotlinlang.org/docs/generics.html
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  类型擦除
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 编译后泛型信息消失，List<String> 和 List<Int> 运行时都是 List
 *  · 不能用 instanceof 判断泛型类型（擦除后是 Object），需要传递 Class<T> 参数
 *
 *  fun <T> printList(list: List<T>) {
 *      // 运行时无法判断 list 是 List<String> 还是 List<Int>
 *      println(list::class)  // class java.util.ArrayList（没有泛型信息）
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  泛型桥方法
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 编译器为保持多态一致性自动生成的合成方法
 *  · 子类重写泛型父类方法时，编译器会生成桥方法保证类型安全
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  PECS 原则
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 3.1  协变（out / extends）────────────────────────────────────────────────
 *
 *  · 上界 <? extends T>（Kotlin out T）：只读，不可写，协变
 *  · Producer Extends：生产者用上界，只能读取数据
 *
 * ── 3.2  逆变（in / super）───────────────────────────────────────────────────
 *
 *  · 下界 <? super T>（Kotlin in T）：可写，读取为 Object，逆变
 *  · Consumer Super：消费者用下界，只能写入数据
 *
 * ── 3.3  示例 ─────────────────────────────────────────────────────────────────
 *
 *  fun copy(src: List<out Number>, dst: MutableList<in Number>) {
 *      dst.addAll(src)   // src 只生产（读），dst 只消费（写）
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  reified + inline（Kotlin 独有）
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · Kotlin 通过内联函数保留类型参数，可在运行时获取泛型实际类型
 *  · 本质是将泛型参数内联到调用点，避免了反射开销
 *
 *  inline fun <reified T> parseJson(json: String): T {
 *      return Gson().fromJson(json, T::class.java)  // T::class.java 在运行时可用
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  泛型上下界
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  fun <T : Comparable<T>> maxOf(a: T, b: T): T = if (a > b) a else b
 *
 *  · 上界约束：T 必须实现 Comparable<T>
 *  · 多个上界：<T> where T : Comparable<T>, T : Serializable
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  6  星号投影
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · List<*> 等价于 List<out Any?>，只读，类型未知
 *  · 适合不关心具体类型时使用，如打印任意 List
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  7  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 读框架源码遇到通配符时，记住 PECS：Producer=out/extends，Consumer=in/super
 *  · Kotlin 的 reified 本质是将泛型参数内联到调用点，避免了反射开销
 *  · 不能用 instanceof 判断泛型类型（擦除后是 Object），需要传递 Class<T> 参数
 */

val genericsData = NoteData(
    title = "泛型机制与类型擦除",
    subtitle = "泛型、注解与动态编程 · PECS · reified · 星号投影",
    color = Color.parseColor("#3F51B5"),
    chapters = listOf(
        ChapterItem("1",   "类型擦除"),
        ChapterItem("2",   "泛型桥方法"),
        ChapterItem("3",   "PECS 原则"),
        ChapterItem("3.1", "协变（out / extends）"),
        ChapterItem("3.2", "逆变（in / super）"),
        ChapterItem("3.3", "示例"),
        ChapterItem("4",   "reified + inline（Kotlin 独有）"),
        ChapterItem("5",   "泛型上下界"),
        ChapterItem("6",   "星号投影"),
        ChapterItem("7",   "最佳实践"),
    )
)
