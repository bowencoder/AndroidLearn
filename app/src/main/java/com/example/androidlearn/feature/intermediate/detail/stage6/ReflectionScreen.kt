package com.example.androidlearn.feature.intermediate.detail.stage6

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * 反射机制
 * 官方文档：https://kotlinlang.org/docs/reflection.html
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  获取 Class 对象
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · obj::class.java：从实例获取 Class
 *  · ClassName::class.java：从类名获取 Class
 *  · Class.forName("com.example.Foo")：动态加载类
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  Field 操作
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · getDeclaredField()：获取字段（包括私有）
 *  · setAccessible(true)：解除访问限制
 *  · field.get(obj) / field.set(obj, value)：读写字段值
 *
 *  // 访问私有字段（热修复/测试常用）
 *  fun setPrivateField(obj: Any, fieldName: String, value: Any?) {
 *      val field = obj::class.java.getDeclaredField(fieldName)
 *      field.isAccessible = true
 *      field.set(obj, value)
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  Method 调用
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · getDeclaredMethod()：获取方法（包括私有）
 *  · method.invoke(obj, *args)：动态调用方法
 *
 *  fun invokeMethod(obj: Any, methodName: String, vararg args: Any?): Any? {
 *      val method = obj::class.java.getDeclaredMethod(
 *          methodName,
 *          *args.map { it?.javaClass ?: Any::class.java }.toTypedArray()
 *      )
 *      method.isAccessible = true
 *      return method.invoke(obj, *args)
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  Constructor 反射创建对象
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · getDeclaredConstructor()：获取构造函数
 *  · constructor.newInstance(*args)：反射创建对象
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  Kotlin KClass API
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · ::class 获取 KClass，比 Java Class 更 Kotlin 友好
 *  · memberProperties / memberFunctions：访问成员
 *
 *  data class User(val name: String, private val age: Int)
 *
 *  val user = User("Alice", 25)
 *  // 遍历所有属性
 *  User::class.memberProperties.forEach { prop ->
 *      println("${prop.name} = ${prop.get(user)}")
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  6  性能优化
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 反射性能比直接调用慢 10-50 倍，高频调用路径应缓存 Field/Method 对象
 *  · Method.setAccessible 有安全开销，避免重复调用
 *  · Android 9+ 对非公开 API 的反射有限制（灰名单），需要元反射绕过
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  7  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 反射性能比直接调用慢 10-50 倍，高频调用路径应缓存 Field/Method 对象
 *  · Android 9+ 对非公开 API 的反射有限制（灰名单），需要元反射绕过
 *  · 单元测试中反射访问私有成员是合理用法，生产代码尽量避免
 */

val reflectionData = NoteData(
    title = "反射机制",
    subtitle = "泛型、注解与动态编程 · Class · Field · Method · KClass",
    color = Color.parseColor("#3F51B5"),
    chapters = listOf(
        ChapterItem("1",   "获取 Class 对象"),
        ChapterItem("2",   "Field 操作"),
        ChapterItem("3",   "Method 调用"),
        ChapterItem("4",   "Constructor 反射创建对象"),
        ChapterItem("5",   "Kotlin KClass API"),
        ChapterItem("6",   "性能优化"),
        ChapterItem("7",   "最佳实践"),
    )
)
