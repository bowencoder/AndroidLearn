package com.example.androidlearn.feature.junior.detail.stage1

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.NoteChapter
import com.example.androidlearn.feature.shared.NoteDetailScaffold

/*
 * Kotlin 核心语法笔记
 * 参考：Kotlin 官方 Tour https://kotlinlang.org/docs/kotlin-tour-welcome.html
 *
 * ── 1  变量（Variables）────────────────────────────────────────────────────────
 *
 *  · val：只读变量，赋值后不可修改
 *  · var：可变变量，可重新赋值；优先使用 val
 *  · 类型推断：编译器从初始值推断，也可先声明后赋值
 *  · 字符串模板：$ 引用变量，${} 嵌入表达式
 *
 *  // Kotlin                                    // Swift
 *  val popcorn = 5                              // let popcorn = 5
 *  var customers = 10                           // var customers = 10
 *  customers = 8                                // customers = 8
 *
 *  val name = "Mary"; val age = 20
 *  println("$name is $age years old")          // print("\(name) is \(age) years old")
 *  println("There are ${customers + 1} left")  // print("There are \(customers + 1) left")
 *
 *  val d: Int                                   // ❌ Swift let 必须声明时赋值
 *  d = 3                                        // Kotlin 可先声明后赋值
 *
 *
 * ── 2  基本类型（Basic Types）─────────────────────────────────────────────────
 *
 *  · 整数：Byte / Short / Int / Long（字面量：100，100L，100_000_000）
 *  · 无符号：UByte / UShort / UInt / ULong（字面量：100u）
 *  · 浮点：Float / Double（字面量：24.5f，19.99）
 *  · Boolean / Char（单引号 'A'，不能当数字用）/ String（不可变）
 *  · 无隐式类型转换，必须显式调用 .toXxx()
 *
 *  // Kotlin                                    // Swift
 *  val year: Int = 2020                         // let year: Int = 2020
 *  val amount: Long = 350_000_000L              // let amount: Int64 = 350_000_000
 *  val score: UInt = 100u                       // let score: UInt = 100
 *  val temp: Float = 24.5f                      // let temp: Float = 24.5
 *  val price: Double = 19.99                    // let price: Double = 19.99
 *  val isEnabled: Boolean = true                // let isEnabled: Bool = true
 *  val sep: Char = ','                          // let sep: Character = ","
 *  val msg: String = "Hello"                    // let msg: String = "Hello"
 *
 *  val l = 100.toLong()                         // let l = Int64(100)
 *  var x = 10; x += 7; x *= 2; x /= 3         // var x = 10; x += 7; x *= 2; x /= 3
 *
 *
 * ── 3  集合（Collections）────────────────────────────────────────────────────
 *
 *  · List：有序，允许重复；listOf() 只读 / mutableListOf() 可变
 *  · Set：无序，唯一；setOf() / mutableSetOf()
 *  · Map：键值对；mapOf("k" to v) / mutableMapOf()
 *  · 只读/可变是两个接口（List/MutableList），Swift 用 let/var 控制
 *
 *  // Kotlin                                    // Swift
 *  val shapes = listOf("a", "b", "c")          // let shapes = ["a", "b", "c"]
 *  var mShapes = mutableListOf("a", "b")       // var mShapes = ["a", "b"]
 *  mShapes.add("c")                            // mShapes.append("c")
 *  mShapes.remove("a")                         // mShapes.removeFirst()
 *  shapes[0]                                   // shapes[0]
 *  shapes.first()                              // shapes.first
 *  shapes.count()                              // shapes.count
 *  "c" in shapes                               // shapes.contains("c")
 *
 *  val fruit = setOf("apple", "apple", "kiwi") // {apple, kiwi}（自动去重）
 *                                              // var fruit: Set = ["apple", "apple", "kiwi"]
 *
 *  val menu = mapOf("apple" to 100, "kiwi" to 190)  // let menu = ["apple": 100, "kiwi": 190]
 *  menu["apple"]                               // menu["apple"]
 *  menu["grape"]                               // null        // nil
 *  menu.keys                                   // menu.keys（Swift 返回 Keys 视图）
 *
 *
 * ── 4  控制流程与循环（Control Flow & Loops）─────────────────────────────────
 *
 *  · if 是表达式，有返回值（Swift if 不是表达式，需三元运算符）
 *  · when ≈ switch，但可作为表达式，支持范围/类型匹配，无需 break
 *  · Range：1..4 闭区间，1..<4 半开区间，downTo 倒序，step 步长
 *  · for-in 遍历范围/集合；while / do-while；标签跳出多层循环
 *
 *  // Kotlin                                    // Swift
 *  val max = if (a > b) a else b               // let max = a > b ? a : b
 *
 *  val result = when (obj) {                   // switch obj {
 *      "Hello" -> "Greeting"                   // case "Hello": "Greeting"
 *      else    -> "Unknown"                    // default:      "Unknown"
 *  }                                           // }（Swift switch 不能直接赋值）
 *
 *  when (x) {
 *      1        -> "one"
 *      2, 3     -> "two or three"              // case 2, 3:
 *      in 4..10 -> "four to ten"              // case 4...10:
 *      is String -> "string"                  // case let s as String:
 *      else     -> "other"                    // default:
 *  }
 *
 *  for (i in 1..5) print(i)                   // for i in 1...5 { print(i) }
 *  for (i in 1..<5) print(i)                  // for i in 1..<5 { print(i) }
 *  for (i in 5 downTo 1 step 2) print(i)      // for i in stride(from:5, through:1, by:-2)
 *  for (item in list) println(item)            // for item in list { print(item) }
 *  for ((idx, v) in list.withIndex())          // for (idx, v) in list.enumerated()
 *
 *  var n = 0; while (n < 3) { n++ }           // var n = 0; while n < 3 { n += 1 }
 *  do { m++ } while (m < 3)                   // repeat { m += 1 } while m < 3
 *
 *  outer@ for (i in 1..3) {                   // outerLoop: for i in 1...3 {
 *      for (j in 1..3) {                      //     for j in 1...3 {
 *          if (j == 2) break@outer            //         if j == 2 { break outerLoop }
 *      }                                      //     }
 *  }                                          // }
 *
 *
 * ── 5  函数与 Lambda（Functions & Lambdas）───────────────────────────────────
 *
 *  · fun 关键字；单表达式函数可省略 {} 和 return
 *  · 无返回值：Unit（可省略），≈ Swift Void
 *  · 默认参数；命名参数（Kotlin 用 =，Swift 用 :）
 *  · Lambda：{ 参数 -> 函数体 }；单参数用 it，≈ Swift $0
 *  · 尾随 lambda；高阶函数：filter / map / fold
 *
 *  // Kotlin                                    // Swift
 *  fun sum(x: Int, y: Int): Int = x + y        // func sum(x: Int, y: Int) -> Int { x + y }
 *  fun greet(name: String = "World") = "Hi $name"  // func greet(name: String = "World") -> String
 *  greet(name = "Kotlin")                       // greet(name: "Kotlin")
 *
 *  val upper: (String) -> String = { it.uppercase() }  // let upper: (String) -> String = { $0.uppercased() }
 *  val add: (Int, Int) -> Int = { a, b -> a + b }      // let add: (Int, Int) -> Int = { $0 + $1 }
 *
 *  listOf(1,-2,3).filter { it > 0 }            // [1,-2,3].filter { $0 > 0 }
 *  listOf(1,2,3).map { it * 2 }                // [1,2,3].map { $0 * 2 }
 *  listOf(1,2,3).fold(0) { acc, x -> acc + x } // [1,2,3].reduce(0) { $0 + $1 }
 *
 *
 * ── 6  类（Classes）──────────────────────────────────────────────────────────
 *
 *  · 主构造函数直接在类头；无需 new 关键字
 *  · data class：自动生成 toString / equals(按值) / copy / 解构
 *  · data class ≈ Swift struct（但仍是引用类型，== 按值比较）
 *
 *  // Kotlin                                    // Swift
 *  class Contact(                               // class Contact {
 *      val id: Int,                             //     let id: Int
 *      var email: String = "a@b.com"           //     var email: String
 *  ) {                                          //     init(id: Int, email: String = "a@b.com") { ... }
 *      fun printId() { println(id) }           //     func printId() { print(id) }
 *  }                                            // }
 *  val c = Contact(1, "mary@gmail.com")         // let c = Contact(id: 1, email: "mary@gmail.com")
 *  c.email = "jane@gmail.com"                   // c.email = "jane@gmail.com"
 *
 *  data class User(val name: String, val id: Int)   // struct User: Equatable, CustomStringConvertible
 *  val u1 = User("Alex", 1)                    // let u1 = User(name: "Alex", id: 1)
 *  val u2 = User("Alex", 1)
 *  println(u1 == u2)                           // true  // u1 == u2（struct 自动 Equatable）
 *  println(u1)                                 // User(name=Alex, id=1)  // 需手动实现 description
 *  val u3 = u1.copy(id = 3)                    // var u3 = u1; u3.id = 3（struct 赋值即复制）
 *  val (name, id) = u1                         // ❌ Swift 无解构语法
 *
 *
 * ── 7  空安全（Null Safety）──────────────────────────────────────────────────
 *
 *  · 非空类型默认不允许 null；加 ? 表示可空
 *  · ?. 安全调用；?: Elvis（null 时取默认值）；!! 非空断言（危险）
 *  · is 检查后智能转换；as? 安全转换；as 强制转换
 *  · filterNotNull() / mapNotNull { } 处理集合中的 null
 *
 *  // Kotlin                                    // Swift
 *  var s: String = "hi"                         // var s: String = "hi"
 *  // s = null  ❌ 编译报错                     // s = nil  ❌ 编译报错
 *  var ns: String? = null                       // var ns: String? = nil
 *
 *  ns?.length                                   // ns?.count          → nil
 *  ns?.length ?: 0                              // ns?.count ?? 0     → 0
 *  ns!!.length                                  // ns!.count          → 崩溃（若为 nil）
 *
 *  if (ns != null) { println(ns.length) }       // if let s = ns { print(s.count) }
 *  val x = ns ?: return                         // guard let x = ns else { return }
 *
 *  // 智能转换
 *  if (obj is String) { println(obj.length) }   // if let s = obj as? String { print(s.count) }
 *  val b = obj as? String                       // let b = obj as? String
 *  val c = obj as String                        // let c = obj as! String  （强制，失败崩溃）
 *
 *  // 集合空值处理
 *  emails.filterNotNull()                       // emails.compactMap { $0 }
 *  users.mapNotNull { it.name }                 // users.compactMap { $0.name }
 *
 *  // Elvis + 提前返回
 *  val user = users[id] ?: return -1           // guard let user = users[id] else { return -1 }
 *
 *
 * ── 8  继承（Inheritance）────────────────────────────────────────────────────
 *
 *  · 类默认 final，需加 open 才能被继承（Swift 默认可继承，需 final 禁止）
 *  · 方法默认 final，需加 open 才能被重写；override 关键字两者相同
 *  · abstract class：不能实例化，抽象成员自动 open
 *  · 所有类继承自 Any（≈ Swift AnyObject）
 *
 *  // Kotlin                                    // Swift
 *  open class Vehicle(val make: String) {       // class Vehicle {
 *      open fun info() = println(make)          //     var make: String
 *  }                                            //     func info() { print(make) }
 *                                               // }
 *  class Car(make: String, val doors: Int)      // class Car: Vehicle {
 *      : Vehicle(make) {                        //     let doors: Int
 *      override fun info() = println("$make $doors doors")  //     override func info() { ... }
 *  }                                            // }
 *
 *  abstract class Shape {                       // ❌ Swift 无 abstract，用 protocol 代替
 *      abstract fun area(): Double
 *      fun describe() = "Area: ${area()}"
 *  }
 *  class Circle(val r: Double) : Shape() {
 *      override fun area() = Math.PI * r * r
 *  }
 *
 *
 * ── 9  接口（Interfaces）─────────────────────────────────────────────────────
 *
 *  · interface ≈ Swift protocol；可有默认实现（Swift 用 protocol extension）
 *  · 接口属性无 backing field；一个类可实现多个接口
 *  · by 委托：将接口实现委托给另一个对象（Swift 无直接等价）
 *
 *  // Kotlin                                    // Swift
 *  interface Drawable {                         // protocol Drawable {
 *      fun draw()                               //     func draw()
 *      fun describe() = "I am drawable"        // }
 *  }                                            // extension Drawable {
 *                                               //     func describe() -> String { "I am drawable" }
 *                                               // }
 *
 *  class Circle : Drawable {                   // class Circle: Drawable {
 *      override fun draw() = println("○")      //     func draw() { print("○") }
 *  }                                            // }
 *
 *  // 多接口实现
 *  class Widget : Drawable, Clickable { ... }  // class Widget: Drawable, Clickable { ... }
 *
 *  // by 委托（Swift 无等价，需手动转发）
 *  class LoggingList<T>(inner: MutableList<T>) : MutableList<T> by inner
 *
 *
 * ── 10  扩展（Extensions）────────────────────────────────────────────────────
 *
 *  · 给已有类添加方法/属性，无需继承；this ≈ Swift self
 *  · 扩展函数静态分发；成员函数优先于扩展函数
 *  · 扩展属性无 backing field，只能计算
 *  · ⚠️ Swift extension 可添加 protocol 遵循，Kotlin 扩展不能
 *  · ⚠️ Kotlin 可扩展可空类型（fun String?.xxx()），Swift 不能
 *
 *  // Kotlin                                    // Swift
 *  fun String.bold() = "<b>$this</b>"          // extension String {
 *                                               //     func bold() -> String { "<b>\(self)</b>" }
 *                                               // }
 *  fun String.isPalindrome() = this == this.reversed()  // func isPalindrome() -> Bool { self == String(self.reversed()) }
 *
 *  val String.wordCount get() = split(" ").size // var wordCount: Int { split(separator: " ").count }
 *
 *  fun String?.orEmpty() = this ?: ""          // ❌ Swift 无法直接扩展 Optional<String>
 *
 *  val Double.asMiles get() = this * 0.621371  // var asMiles: Double { self * 0.621371 }
 *
 *
 * ── 11  数据类与密封类 ────────────────────────────────────────────────────────
 *
 *  · data class：自动生成 equals/hashCode/toString/copy/解构（componentN）
 *  · sealed class ≈ Swift enum with associated values；when 可穷举无需 else
 *  · ⚠️ data class 是引用类型（== 按值），Swift struct 是真正值类型
 *
 *  // Kotlin                                    // Swift
 *  data class User(val name: String, val age: Int)  // struct User: Equatable { let name: String; let age: Int }
 *  val u1 = User("Alice", 30)
 *  val u2 = u1.copy(age = 31)                  // var u2 = u1; u2.age = 31（struct 赋值即复制）
 *  val (name, age) = u1                        // ❌ Swift 无解构语法
 *  println(u1)                                 // User(name=Alice, age=30)  // 需实现 CustomStringConvertible
 *
 *  sealed class Result<out T> {                // enum Result<T> {
 *      data class Success<T>(val data: T) : Result<T>()  //     case success(T)
 *      data class Error(val msg: String) : Result<Nothing>()  //  case error(String)
 *      object Loading : Result<Nothing>()      //     case loading
 *  }                                           // }
 *  fun handle(r: Result<String>) = when (r) { // func handle(_ r: Result<String>) {
 *      is Result.Success -> println(r.data)   //     switch r {
 *      is Result.Error   -> println(r.msg)    //     case .success(let d): print(d)
 *      Result.Loading    -> println("loading")//     case .error(let m):   print(m)
 *  }                                           //     case .loading:        print("loading") } }
 *
 *
 * ── 12  泛型（Generics）──────────────────────────────────────────────────────
 *
 *  · out（协变，只读）/ in（逆变，只写）；reified 保留运行时类型信息
 *  · ⚠️ Swift 无类型擦除，不需要 reified；out/in 用 some/any 代替
 *
 *  // Kotlin                                    // Swift
 *  fun <T> wrap(item: T): List<T> = listOf(item)  // func wrap<T>(_ item: T) -> [T] { [item] }
 *
 *  fun <T : Comparable<T>> max(a: T, b: T) = if (a > b) a else b
 *                                               // func max<T: Comparable>(_ a: T, _ b: T) -> T { a > b ? a : b }
 *
 *  fun printAll(list: List<out Number>) { ... } // func printAll(_ list: [any Numeric]) { ... }
 *
 *  inline fun <reified T> isType(v: Any) = v is T  // ❌ Swift 不需要，直接 v is T
 *  isType<String>("hello")                      // "hello" is String
 *
 *
 * ── 13  枚举类（Enum Classes）────────────────────────────────────────────────
 *
 *  · 每个枚举值是枚举类的实例；可有属性/方法；when 可穷举无需 else
 *  · ⚠️ Kotlin 枚举常量大写；Swift 枚举 case 小写
 *  · ⚠️ 带关联值用 sealed class（Kotlin）vs enum with associated values（Swift）
 *
 *  // Kotlin                                    // Swift
 *  enum class Direction { NORTH, SOUTH, EAST, WEST }  // enum Direction { case north, south, east, west }
 *  Direction.NORTH.name                         // ❌ Swift 无内置 name（需 CustomStringConvertible）
 *  Direction.NORTH.ordinal                      // ❌ Swift 无内置 ordinal
 *
 *  enum class Color(val rgb: Int) {             // enum Color: Int {
 *      RED(0xFF0000), GREEN(0x00FF00);          //     case red = 0xFF0000, green = 0x00FF00
 *      fun containsRed() = rgb and 0xFF0000 != 0  //     func containsRed() -> Bool { rawValue & 0xFF0000 != 0 }
 *  }                                            // }
 *  Color.RED.containsRed()                      // Color.red.containsRed()
 *
 *  val msg = when (direction) {                 // let msg: String
 *      Direction.NORTH -> "Go north"            // switch direction {
 *      Direction.SOUTH -> "Go south"            // case .north: msg = "Go north"
 *      else -> "Other"                          // ... }
 *  }
 *
 *
 * ── 14  对象（Objects）───────────────────────────────────────────────────────
 *
 *  · object：单例，懒加载，线程安全；可实现接口，不能有构造函数
 *  · companion object：类的静态成员，≈ Swift static；可实现接口（Swift static 不能）
 *  · 对象表达式（匿名对象）：临时实现接口（Swift 无直接等价，用闭包代替）
 *
 *  // Kotlin                                    // Swift
 *  object AppConfig {                           // class AppConfig {
 *      var name = "My App"                      //     static let shared = AppConfig()
 *  }                                            //     var name = "My App"
 *  AppConfig.name                               // }
 *                                               // AppConfig.shared.name
 *
 *  class Temperature(val celsius: Double) {     // class Temperature {
 *      companion object {                       //     let celsius: Double
 *          fun fromF(f: Double) =               //     static func fromF(_ f: Double) -> Temperature {
 *              Temperature((f - 32) * 5 / 9)   //         Temperature(celsius: (f - 32) * 5 / 9)
 *      }                                        //     }
 *  }                                            // }
 *  Temperature.fromF(90.0)                      // Temperature.fromF(90.0)
 *
 *  // 匿名对象
 *  val listener = object : ClickListener {      // ❌ Swift 无匿名对象，用闭包代替：
 *      override fun onClick() = println("!")    // let onClick: () -> Void = { print("!") }
 *  }
 *
 *
 * ── 15  作用域函数（Scope Functions）─────────────────────────────────────────
 *
 *  · let（it，返回结果）/ apply（this，返回自身）/ run（this，返回结果）
 *  · also（it，返回自身）/ with（this，返回结果）
 *  · Swift 无内置等价，用 if let / 临时变量 / 链式调用模拟
 *
 *  // Kotlin                                    // Swift
 *  address?.let { sendNotification(it) }        // if let addr = address { sendNotification(addr) }
 *
 *  val client = Client().apply {                // let client = Client()
 *      token = "abc"                            // client.token = "abc"
 *      connect()                                // client.connect()
 *  }                                            // （Swift 无 apply，需逐行赋值）
 *
 *  val result = client.run {                    // let result: String = {
 *      connect(); authenticate(); getData()     //     client.connect(); client.authenticate()
 *  }                                            //     return client.getData() }()
 *
 *  list.also { println(it) }.filter { ... }    // （Swift 无 also，需拆成两行）
 *
 *  with(canvas) { draw(); fill() }             // canvas.draw(); canvas.fill()
 *
 *
 * ── 16  带接收者的 Lambda（Lambda with Receiver）──────────────────────────────
 *
 *  · 函数类型：ReceiverType.() -> ReturnType
 *  · lambda 内部可直接访问接收者成员（无需 this.）
 *  · DSL 基础：buildString / buildList / Jetpack Compose
 *  · ≈ Swift @resultBuilder（SwiftUI DSL），但机制不同
 *
 *  // Kotlin                                    // Swift
 *  val greet: String.() -> String = { "Hello, $this!" }
 *  "Kotlin".greet()                             // "Hello, Kotlin!"
 *                                               // ❌ Swift 无带接收者 lambda，需用 extension
 *                                               // extension String { func greet() -> String { "Hello, \(self)!" } }
 *
 *  fun render(block: Canvas.() -> Unit) {       // func render(_ block: (Canvas) -> Void) {
 *      val c = Canvas(); c.block()              //     let c = Canvas(); block(c)
 *  }                                            // }
 *  render { drawCircle(); drawSquare() }        // render { c in c.drawCircle(); c.drawSquare() }
 *
 *  val s = buildString {                        // var s = ""
 *      append("Hello"); append(", World")       // s += "Hello"; s += ", World"
 *  }                                            // （Swift 无 buildString）
 *
 *  // Jetpack Compose DSL                       // SwiftUI DSL
 *  Column { Text("Hello"); Text("World") }      // VStack { Text("Hello"); Text("World") }
 *
 *
 * ── 17  属性进阶（Properties）────────────────────────────────────────────────
 *
 *  · backing field：通过 field 关键字在 getter/setter 中访问（Swift 直接用属性名）
 *  · lazy：首次访问时初始化，线程安全（Swift lazy var，非线程安全）
 *  · observable ≈ Swift didSet/willSet；vetoable：Swift 无直接等价
 *
 *  // Kotlin                                    // Swift
 *  class Person {
 *      var name: String = ""
 *          set(value) {                         // var name: String = "" {
 *              field = value.uppercase()        //     didSet { name = name.uppercased() }
 *          }                                    // }
 *  }
 *
 *  val heavy by lazy { HeavyObject() }          // lazy var heavy = HeavyObject()
 *  // 线程安全                                  // ⚠️ 非线程安全
 *
 *  var temp by observable(20.0) { _, old, new ->  // var temp: Double = 20.0 {
 *      println("$old -> $new")                  //     didSet { print("\(oldValue) -> \(temp)") }
 *  }                                            // }
 *
 *  var age by Delegates.vetoable(0) { _, _, new -> new >= 0 }
 *  age = -1  // 被拒绝，age 仍为 0             // ❌ Swift 无 vetoable，需在 willSet 手动还原
 *
 *
 * ── 18  库与 API（Libraries & APIs）──────────────────────────────────────────
 *
 *  · kotlin 包下内容无需 import；其他包需显式 import
 *  · 标准库：集合 / 序列 / 字符串 / 时间 / 数学
 *  · 实验性 API 需 @OptIn(ExperimentalXxx::class)
 *
 *  // Kotlin                                    // Swift
 *  import kotlin.time.Duration.Companion.minutes  // import Foundation
 *  import kotlin.math.*                         // import Darwin（或直接用 Foundation）
 *
 *  val text = "emosewa si niltoK"
 *  text.reversed()                              // String(text.reversed())
 *
 *  val t: Duration = 30.minutes                // let t: TimeInterval = 30 * 60
 *
 *  val elapsed = measureTime { doWork() }       // let start = CFAbsoluteTimeGetCurrent()
 *                                               // doWork()
 *                                               // let elapsed = CFAbsoluteTimeGetCurrent() - start
 *
 *  kotlin.math.PI                               // Double.pi
 *  (1 + r / n).pow(n * t)                      // pow(1 + r / n, Double(n * t))
 *
 *  @OptIn(ExperimentalUnsignedTypes::class)     // @available(*, deprecated)  /  @_spi(...)
 *  fun useUInt() { val arr = uintArrayOf(1u, 2u) }
 */

private val Green = Color(0xFF4CAF50)

private val chapters = listOf(
    NoteChapter("1",  "变量"),
    NoteChapter("2",  "基本类型"),
    NoteChapter("3",  "集合"),
    NoteChapter("4",  "控制流程与循环"),
    NoteChapter("5",  "函数与 Lambda"),
    NoteChapter("6",  "类"),
    NoteChapter("7",  "空安全"),
    NoteChapter("8",  "继承"),
    NoteChapter("9",  "接口"),
    NoteChapter("10", "扩展"),
    NoteChapter("11", "数据类与密封类"),
    NoteChapter("12", "泛型"),
    NoteChapter("13", "枚举类"),
    NoteChapter("14", "对象"),
    NoteChapter("15", "作用域函数"),
    NoteChapter("16", "带接收者的 Lambda"),
    NoteChapter("17", "属性进阶"),
    NoteChapter("18", "库与 API"),
)

@Composable
fun KotlinSyntaxScreen(
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    NoteDetailScaffold(
        title = "Kotlin 核心语法",
        subtitle = "官方 Tour · 变量 → 库与 API（共 18 章）",
        color = Green,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
