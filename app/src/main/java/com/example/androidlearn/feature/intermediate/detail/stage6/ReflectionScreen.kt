package com.example.androidlearn.feature.intermediate.detail.stage6

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * 反射机制
 * 官方文档：https://kotlinlang.org/docs/reflection.html
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  获取 Class 对象  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · obj::class.java：从实例获取 Class
 *  · ClassName::class.java：从类名获取 Class（编译期已知）
 *  · Class.forName("com.example.Foo")：动态加载类（运行时字符串）
 *
 *  val user = User("Alice", 25)
 *  println(user::class.java)           // class com.example.User
 *  println(User::class.java)           // class com.example.User
 *  val clazz = Class.forName("com.example.User")  // 动态加载
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  Field 操作  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · getDeclaredField()：获取字段（包括私有），getField() 只能获取 public
 *  · isAccessible = true：解除访问限制
 *  · field.get(obj) / field.set(obj, value)：读写字段值
 *
 *  // 访问私有字段（热修复 / 测试常用）
 *  fun setPrivateField(obj: Any, fieldName: String, value: Any?) {
 *      val field = obj::class.java.getDeclaredField(fieldName)
 *      field.isAccessible = true
 *      field.set(obj, value)
 *  }
 *
 *  // 读取私有字段
 *  fun getPrivateField(obj: Any, fieldName: String): Any? {
 *      val field = obj::class.java.getDeclaredField(fieldName)
 *      field.isAccessible = true
 *      return field.get(obj)
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  Method 调用  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · getDeclaredMethod(name, *paramTypes)：获取方法（包括私有）
 *  · method.invoke(obj, *args)：动态调用方法
 *
 *  fun invokePrivateMethod(obj: Any, methodName: String, vararg args: Any?): Any? {
 *      val paramTypes = args.map { it?.javaClass ?: Any::class.java }.toTypedArray()
 *      val method = obj::class.java.getDeclaredMethod(methodName, *paramTypes)
 *      method.isAccessible = true
 *      return method.invoke(obj, *args)
 *  }
 *
 *  // 调用示例
 *  class Calculator {
 *      private fun add(a: Int, b: Int) = a + b
 *  }
 *  val calc = Calculator()
 *  val result = invokePrivateMethod(calc, "add", 3, 4)  // 7
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  Constructor 反射创建对象  ★ 常用
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · getDeclaredConstructor(*paramTypes)：获取构造函数
 *  · constructor.newInstance(*args)：反射创建对象
 *
 *  // 调用私有构造函数（单例破坏 / 框架初始化）
 *  val constructor = MyClass::class.java.getDeclaredConstructor(String::class.java)
 *  constructor.isAccessible = true
 *  val instance = constructor.newInstance("param") as MyClass
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  Kotlin KClass API  ★ 常用
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · ::class 获取 KClass，比 Java Class 更 Kotlin 友好
 *  · memberProperties：所有成员属性（含继承）
 *  · declaredMemberProperties：仅当前类声明的属性
 *  · memberFunctions：所有成员函数
 *
 *  data class User(val name: String, private val age: Int)
 *
 *  val user = User("Alice", 25)
 *
 *  // 遍历所有属性（含私有）
 *  User::class.declaredMemberProperties.forEach { prop ->
 *      prop.isAccessible = true
 *      println("${prop.name} = ${prop.get(user)}")
 *  }
 *  // 输出：name = Alice
 *  //       age = 25
 *
 *  // 按名称获取属性
 *  val nameProp = User::class.memberProperties.first { it.name == "name" }
 *  println(nameProp.get(user))  // Alice
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  6  Android 9+ 灰名单限制  ★ 常用
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · Android 9（API 28）起，对非公开 SDK API 的反射访问受到限制
 *  · 分级：白名单（可用）/ 浅灰（警告）/ 深灰（targetSdk≥28 禁止）/ 黑名单（始终禁止）
 *  · 绕过方案：元反射（反射调用 setAccessible 本身）
 *
 *  // 元反射绕过灰名单（Android 9~11 有效）
 *  fun unseal(method: Method) {
 *      val forName = Class::class.java.getDeclaredMethod("forName", String::class.java)
 *      val getMethod = Class::class.java.getDeclaredMethod(
 *          "getDeclaredMethod", String::class.java, arrayOf<Class<*>>()::class.java
 *      )
 *      val vmRuntime = forName.invoke(null, "dalvik.system.VMRuntime")
 *      val getRuntime = (vmRuntime as Class<*>).getDeclaredMethod("getRuntime")
 *      getRuntime.isAccessible = true
 *      val runtime = getRuntime.invoke(null)
 *      val setHiddenApiExemptions = vmRuntime.getDeclaredMethod(
 *          "setHiddenApiExemptions", Array<String>::class.java
 *      )
 *      setHiddenApiExemptions.invoke(runtime, arrayOf("L"))  // 豁免所有
 *  }
 *
 *  · 更推荐：使用 FreeReflection / ReflectionHelper 等开源库处理兼容性
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  7  性能与最佳实践  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 反射性能比直接调用慢 10~50 倍，高频调用路径必须缓存 Field/Method 对象
 *  · isAccessible = true 有安全开销，避免每次调用都重复设置
 *
 *  // ✅ 正确：缓存 Field 对象
 *  private val nameField: Field = User::class.java
 *      .getDeclaredField("name").also { it.isAccessible = true }
 *
 *  fun getName(user: User): String = nameField.get(user) as String
 *
 *  // ❌ 错误：每次调用都重新获取 Field
 *  fun getName(user: User): String {
 *      val field = User::class.java.getDeclaredField("name")
 *      field.isAccessible = true
 *      return field.get(user) as String
 *  }
 *
 *  ✅ 应该做：
 *  · 单元测试中反射访问私有成员是合理用法
 *  · 框架/热修复场景中反射是必要手段，注意缓存
 *  · 优先用 KClass API（更 Kotlin 友好，支持 nullable 类型）
 *
 *  ❌ 不应该做：
 *  · 不要在业务代码中滥用反射（可读性差、性能差、破坏封装）
 *  · 不要在主线程高频调用反射（会造成卡顿）
 *  · Android 9+ 不要直接反射系统私有 API，优先查找官方替代方案
 */

val reflectionData = NoteData(
    title = "反射机制",
    subtitle = "Class · Field · Method · KClass · 灰名单",
    color = Color.parseColor("#3F51B5"),
    chapters = listOf(
        ChapterItem("1",   "获取 Class 对象  ★ 必学"),
        ChapterItem("2",   "Field 操作  ★ 必学"),
        ChapterItem("3",   "Method 调用  ★ 必学"),
        ChapterItem("4",   "Constructor 反射创建对象  ★ 常用"),
        ChapterItem("5",   "Kotlin KClass API  ★ 常用"),
        ChapterItem("6",   "Android 9+ 灰名单限制  ★ 常用"),
        ChapterItem("7",   "性能与最佳实践  ★ 必学"),
    )
)
