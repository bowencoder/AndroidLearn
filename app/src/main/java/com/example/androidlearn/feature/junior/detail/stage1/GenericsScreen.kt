package com.example.androidlearn.feature.junior.detail.stage1

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.NoteChapter
import com.example.androidlearn.feature.shared.NoteDetailScaffold

/*
 * 泛型机制与类型擦除
 * 官方文档：https://kotlinlang.org/docs/generics.html
 *
 * ── 1  类型擦除  ★ 必学 ────────────────────────────────────────────────────────
 *
 *  · 编译后泛型信息消失，List<String> 和 List<Int> 运行时都是 List
 *  · 不能用 instanceof 判断泛型类型（擦除后是 Object），需要传递 Class<T> 参数
 *
 *  fun <T> printList(list: List<T>) {
 *      // 运行时无法判断 list 是 List<String> 还是 List<Int>
 *      println(list::class)  // class java.util.ArrayList（没有泛型信息）
 *  }
 *
 *  // 需要类型信息时，显式传入 Class<T>
 *  fun <T> parseJson(json: String, clazz: Class<T>): T {
 *      return Gson().fromJson(json, clazz)
 *  }
 *
 *
 * ── 2  泛型桥方法  ★ 了解 ──────────────────────────────────────────────────────
 *
 *  · 编译器为保持多态一致性自动生成的合成方法
 *  · 子类重写泛型父类方法时，编译器会生成桥方法保证类型安全
 *
 *  // 父类
 *  open class Box<T>(val value: T) {
 *      open fun get(): T = value
 *  }
 *  // 子类重写后，编译器自动生成桥方法：fun get(): Any = get()（强转为 String）
 *  class StringBox(value: String) : Box<String>(value) {
 *      override fun get(): String = value.uppercase()
 *  }
 *
 *
 * ── 3  PECS 原则  ★ 必学 ───────────────────────────────────────────────────────
 *
 * ── 3.1  协变（out / extends）────────────────────────────────────────────────
 *
 *  · 上界 <? extends T>（Kotlin out T）：只读，不可写，协变
 *  · Producer Extends：生产者用上界，只能读取数据
 *
 *  // Kotlin
 *  fun printNumbers(list: List<out Number>) {
 *      list.forEach { println(it) }  // 只读，安全
 *      // list.add(1)  ❌ 编译报错，out 不可写
 *  }
 *
 *  // Java 等价
 *  void printNumbers(List<? extends Number> list) { ... }
 *
 * ── 3.2  逆变（in / super）───────────────────────────────────────────────────
 *
 *  · 下界 <? super T>（Kotlin in T）：可写，读取为 Any?，逆变
 *  · Consumer Super：消费者用下界，只能写入数据
 *
 *  // Kotlin
 *  fun addNumbers(list: MutableList<in Int>) {
 *      list.add(1)   // 可写
 *      list.add(2)
 *      // val n: Int = list[0]  ❌ 读取只能得到 Any?
 *  }
 *
 * ── 3.3  PECS 综合示例 ────────────────────────────────────────────────────────
 *
 *  fun copy(src: List<out Number>, dst: MutableList<in Number>) {
 *      dst.addAll(src)   // src 只生产（读），dst 只消费（写）
 *  }
 *
 *  // 使用
 *  val ints = listOf(1, 2, 3)
 *  val nums = mutableListOf<Number>()
 *  copy(ints, nums)  // ✅ Int 是 Number 的子类，协变/逆变均满足
 *
 *
 * ── 4  reified + inline（Kotlin 独有）★ 必学 ──────────────────────────────────
 *
 *  · Kotlin 通过内联函数保留类型参数，可在运行时获取泛型实际类型
 *  · 本质是将泛型参数内联到调用点，避免了反射开销
 *  · 常见用途：JSON 解析、startActivity、ViewModel 获取
 *
 *  // 无 reified（需传 Class）
 *  fun <T> parseJson(json: String, clazz: Class<T>): T = Gson().fromJson(json, clazz)
 *  val user = parseJson(json, User::class.java)
 *
 *  // 有 reified（无需传 Class）
 *  inline fun <reified T> parseJson(json: String): T = Gson().fromJson(json, T::class.java)
 *  val user: User = parseJson(json)  // 更简洁
 *
 *  // startActivity 封装
 *  inline fun <reified T : Activity> Context.startActivity() {
 *      startActivity(Intent(this, T::class.java))
 *  }
 *  startActivity<MainActivity>()
 *
 *
 * ── 5  泛型上下界  ★ 常用 ──────────────────────────────────────────────────────
 *
 *  // 单上界
 *  fun <T : Comparable<T>> maxOf(a: T, b: T): T = if (a > b) a else b
 *  maxOf(3, 5)       // 5
 *  maxOf("a", "b")   // "b"
 *
 *  // 多上界（where 子句）
 *  fun <T> serialize(obj: T): String
 *      where T : Comparable<T>, T : Serializable {
 *      return obj.toString()
 *  }
 *
 *
 * ── 6  星号投影  ★ 常用 ────────────────────────────────────────────────────────
 *
 *  · List<*> 等价于 List<out Any?>，只读，类型未知
 *  · 适合不关心具体类型时使用，如打印任意 List
 *
 *  fun printAny(list: List<*>) {
 *      list.forEach { println(it) }  // it 类型为 Any?
 *  }
 *  printAny(listOf(1, "hello", true))  // ✅
 *
 *  // Map<*, *> 等价于 Map<out Any?, out Any?>
 *  fun printMap(map: Map<*, *>) {
 *      map.forEach { (k, v) -> println("$k -> $v") }
 *  }
 *
 *
 * ── 7  声明处型变（Declaration-site Variance）★ 了解 ──────────────────────────
 *
 *  · 在类/接口定义处声明 out/in，所有使用处自动协变/逆变
 *  · Kotlin 标准库中大量使用：List<out E>、Comparable<in T>
 *
 *  // 声明处协变（只读接口）
 *  interface Producer<out T> {
 *      fun produce(): T   // 只能返回 T，不能接收 T
 *  }
 *
 *  // 声明处逆变（只写接口）
 *  interface Consumer<in T> {
 *      fun consume(item: T)  // 只能接收 T，不能返回 T
 *  }
 *
 *  // 使用处型变（use-site variance）
 *  fun copy(from: Array<out Any>, to: Array<Any>) { ... }
 *
 *
 * ── 8  最佳实践  ★ 必学 ────────────────────────────────────────────────────────
 *
 *  ✅ 应该做：
 *  · 读框架源码遇到通配符时，记住 PECS：Producer=out/extends，Consumer=in/super
 *  · 需要运行时类型信息时，优先用 reified inline 函数，避免传 Class<T> 参数
 *  · 集合 API 参数用 List<out T> 而非 List<T>，提高调用灵活性
 *
 *  ❌ 不应该做：
 *  · 不要用 instanceof 判断泛型类型（擦除后无效），需要类型信息时传 Class<T>
 *  · 不要滥用星号投影（*），会丢失类型信息，尽量明确泛型参数
 *  · reified 只能用于 inline 函数，不能用于普通函数或类
 */

private val Green = Color(0xFF4CAF50)

private val chapters = listOf(
    NoteChapter("1",   "类型擦除  ★ 必学"),
    NoteChapter("2",   "泛型桥方法  ★ 了解"),
    NoteChapter("3",   "PECS 原则  ★ 必学"),
    NoteChapter("3.1", "协变（out / extends）"),
    NoteChapter("3.2", "逆变（in / super）"),
    NoteChapter("3.3", "PECS 综合示例"),
    NoteChapter("4",   "reified + inline（Kotlin 独有）★ 必学"),
    NoteChapter("5",   "泛型上下界  ★ 常用"),
    NoteChapter("6",   "星号投影  ★ 常用"),
    NoteChapter("7",   "声明处型变  ★ 了解"),
    NoteChapter("8",   "最佳实践  ★ 必学"),
)

@Composable
fun GenericsScreen(
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    NoteDetailScaffold(
        title = "泛型机制与类型擦除",
        subtitle = "类型擦除 · PECS · reified · 星号投影 · 声明处型变",
        color = Green,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
