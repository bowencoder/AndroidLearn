package com.example.androidlearn.feature.intermediate.detail.stage6

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * 注解与 APT / KSP
 * 官方文档：https://developer.android.com/studio/build/dependencies#annotation_processor
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  注解基础  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  注解的本质  ★ 必学 ────────────────────────────────────────────────────
 *
 *  · 注解是附加在代码元素上的元数据，本身不执行任何逻辑
 *  · 注解的处理方式决定了它的作用：
 *    - 编译期处理（APT/KSP）：生成代码（Room、Hilt、Retrofit）
 *    - 运行时处理（反射）：读取注解信息动态执行逻辑
 *    - 编译器检查（SOURCE）：仅用于 IDE 提示，如 @Nullable、@Override
 *
 * ── 1.2  定义注解  ★ 必学 ──────────────────────────────────────────────────────
 *
 *  · @Target：注解可以作用的目标
 *  · @Retention：注解的保留策略（决定注解在哪个阶段可见）
 *  · @Repeatable：允许同一元素上多次使用同一注解
 *  · @MustBeDocumented：注解会出现在 KDoc 文档中
 *
 *  // Kotlin 定义注解
 *  @Target(
 *      AnnotationTarget.CLASS,       // 类/接口/对象
 *      AnnotationTarget.FUNCTION,    // 函数
 *      AnnotationTarget.PROPERTY,    // 属性
 *      AnnotationTarget.FIELD,       // 字段（Java 字段）
 *      AnnotationTarget.VALUE_PARAMETER  // 函数参数
 *  )
 *  @Retention(AnnotationRetention.RUNTIME)  // 运行时可通过反射读取
 *  annotation class Route(val path: String)
 *
 * ── 1.3  @Retention 三种策略  ★ 必学 ──────────────────────────────────────────
 *
 *  ┌──────────────────┬──────────────────────────────────────────────────────┐
 *  │    策略           │                    说明                              │
 *  ├──────────────────┼──────────────────────────────────────────────────────┤
 *  │ SOURCE           │ 仅保留在源码，编译后丢弃（APT 处理后不存在于 .class）   │
 *  │                  │ 用途：@SuppressWarnings、IDE 提示类注解                │
 *  ├──────────────────┼──────────────────────────────────────────────────────┤
 *  │ BINARY（CLASS）  │ 保留在 .class 文件，但运行时不可见（默认值）            │
 *  │                  │ 用途：字节码插桩工具读取                                │
 *  ├──────────────────┼──────────────────────────────────────────────────────┤
 *  │ RUNTIME          │ 保留到运行时，可通过反射读取                            │
 *  │                  │ 用途：@Route、@GET（Retrofit）、自定义运行时注解         │
 *  └──────────────────┴──────────────────────────────────────────────────────┘
 *
 *  // ⚠️ 常见错误：想用反射读取注解，却用了 SOURCE/BINARY
 *  @Retention(AnnotationRetention.SOURCE)  // ❌ 运行时读不到
 *  annotation class MyAnnotation
 *
 *  @Retention(AnnotationRetention.RUNTIME)  // ✅ 运行时可读
 *  annotation class MyAnnotation
 *
 * ── 1.4  运行时读取注解（反射）★ 常用 ─────────────────────────────────────────
 *
 *  @Retention(AnnotationRetention.RUNTIME)
 *  @Target(AnnotationTarget.FUNCTION)
 *  annotation class LogTime
 *
 *  class UserService {
 *      @LogTime
 *      fun getUser(id: Int): User { /* ... */ }
 *  }
 *
 *  // 运行时读取
 *  val method = UserService::class.java.getMethod("getUser", Int::class.java)
 *  if (method.isAnnotationPresent(LogTime::class.java)) {
 *      val start = System.currentTimeMillis()
 *      method.invoke(service, 1)
 *      println("耗时：${System.currentTimeMillis() - start}ms")
 *  }
 *
 *  // Kotlin 反射（更简洁）
 *  UserService::getUser.findAnnotation<LogTime>()?.let {
 *      println("方法 ${UserService::getUser.name} 需要计时")
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  APT（Annotation Processing Tool）★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  APT 工作原理  ★ 必学 ─────────────────────────────────────────────────
 *
 *  · APT 在编译期扫描注解，生成 Java/Kotlin 源码文件，再一起编译
 *  · 生成的代码在 build/generated/source/kapt/ 目录，可直接查看
 *  · 处理流程：
 *    源码 → javac/kotlinc 编译 → APT 扫描注解 → 生成代码 → 再次编译 → .class
 *
 *  · 在 build.gradle.kts 中配置：
 *  dependencies {
 *      kapt("com.google.dagger:hilt-compiler:2.50")   // Hilt APT
 *      kapt("androidx.room:room-compiler:2.6.0")      // Room APT
 *  }
 *
 * ── 2.2  自定义 APT 处理器  ★ 常用 ────────────────────────────────────────────
 *
 *  · 继承 AbstractProcessor，实现 process() 方法
 *  · 用 JavaPoet（Java）或 KotlinPoet（Kotlin）生成代码文件
 *
 *  // 1. 定义注解
 *  @Target(AnnotationTarget.CLASS)
 *  @Retention(AnnotationRetention.SOURCE)
 *  annotation class AutoFactory
 *
 *  // 2. 实现处理器
 *  @SupportedAnnotationTypes("com.example.AutoFactory")
 *  @SupportedSourceVersion(SourceVersion.RELEASE_17)
 *  class AutoFactoryProcessor : AbstractProcessor() {
 *
 *      override fun process(
 *          annotations: Set<TypeElement>,
 *          roundEnv: RoundEnvironment
 *      ): Boolean {
 *          roundEnv.getElementsAnnotatedWith(AutoFactory::class.java)
 *              .filterIsInstance<TypeElement>()
 *              .forEach { classElement ->
 *                  generateFactory(classElement)
 *              }
 *          return true
 *      }
 *
 *      private fun generateFactory(element: TypeElement) {
 *          val className = element.simpleName.toString()
 *          val packageName = processingEnv.elementUtils
 *              .getPackageOf(element).qualifiedName.toString()
 *
 *          // 用 KotlinPoet 生成 ${className}Factory.kt
 *          val fileSpec = FileSpec.builder(packageName, "${className}Factory")
 *              .addType(
 *                  TypeSpec.objectBuilder("${className}Factory")
 *                      .addFunction(
 *                          FunSpec.builder("create")
 *                              .returns(ClassName(packageName, className))
 *                              .addStatement("return %T()", ClassName(packageName, className))
 *                              .build()
 *                      ).build()
 *              ).build()
 *
 *          fileSpec.writeTo(processingEnv.filer)
 *      }
 *  }
 *
 *  // 3. 注册处理器（resources/META-INF/services/javax.annotation.processing.Processor）
 *  // com.example.AutoFactoryProcessor
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  KSP（Kotlin Symbol Processing）★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 3.1  KSP vs kapt（APT）对比  ★ 必学 ──────────────────────────────────────
 *
 *  ┌──────────────────┬──────────────────────────┬──────────────────────────┐
 *  │                  │         kapt（APT）       │          KSP             │
 *  ├──────────────────┼──────────────────────────┼──────────────────────────┤
 *  │ 编译速度          │ 慢（需生成 Java stub）    │ 快 2x（直接处理 Kotlin）  │
 *  │ Kotlin 支持       │ 通过 Java stub 间接支持   │ 原生支持 Kotlin 语法      │
 *  │ 多平台（KMP）     │ 不支持                   │ 支持                      │
 *  │ 增量编译          │ 有限支持                  │ 更好的增量支持             │
 *  │ 主流库支持        │ Room/Hilt/Retrofit 均支持 │ Room/Hilt 已支持，持续迁移 │
 *  └──────────────────┴──────────────────────────┴──────────────────────────┘
 *
 *  · 新项目优先用 KSP，旧项目逐步从 kapt 迁移到 KSP
 *
 * ── 3.2  KSP 配置  ★ 常用 ─────────────────────────────────────────────────────
 *
 *  // build.gradle.kts（模块级）
 *  plugins {
 *      id("com.google.devtools.ksp") version "1.9.0-1.0.13"
 *  }
 *
 *  dependencies {
 *      ksp("androidx.room:room-compiler:2.6.0")       // Room KSP
 *      ksp("com.google.dagger:hilt-compiler:2.50")    // Hilt KSP
 *  }
 *
 * ── 3.3  自定义 KSP 处理器  ★ 常用 ────────────────────────────────────────────
 *
 *  class AutoFactoryProcessor(private val codeGenerator: CodeGenerator) : SymbolProcessor {
 *
 *      override fun process(resolver: Resolver): List<KSAnnotated> {
 *          resolver.getSymbolsWithAnnotation("com.example.AutoFactory")
 *              .filterIsInstance<KSClassDeclaration>()
 *              .forEach { classDecl ->
 *                  generateFactory(classDecl)
 *              }
 *          return emptyList()
 *      }
 *
 *      private fun generateFactory(classDecl: KSClassDeclaration) {
 *          val packageName = classDecl.packageName.asString()
 *          val className = classDecl.simpleName.asString()
 *          val file = codeGenerator.createNewFile(
 *              Dependencies(false, classDecl.containingFile!!),
 *              packageName,
 *              "${className}Factory"
 *          )
 *          file.writer().use { writer ->
 *              writer.write("package $packageName\n\n")
 *              writer.write("object ${className}Factory {\n")
 *              writer.write("    fun create() = $className()\n")
 *              writer.write("}\n")
 *          }
 *      }
 *  }
 *
 *  // 注册 Provider
 *  class AutoFactoryProcessorProvider : SymbolProcessorProvider {
 *      override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
 *          AutoFactoryProcessor(environment.codeGenerator)
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  典型框架中的注解应用  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 4.1  Room 注解  ★ 必学 ────────────────────────────────────────────────────
 *
 *  · @Database：标记 RoomDatabase 子类，APT 生成实现类
 *  · @Entity：标记数据表，APT 生成建表 SQL
 *  · @Dao：标记 DAO 接口，APT 生成 SQL 执行实现
 *  · @Query / @Insert / @Update / @Delete：标记 DAO 方法
 *
 *  @Entity(tableName = "users")
 *  data class User(
 *      @PrimaryKey val id: Int,
 *      @ColumnInfo(name = "user_name") val name: String
 *  )
 *
 *  @Dao interface UserDao {
 *      @Query("SELECT * FROM users WHERE id = :id")
 *      suspend fun getUser(id: Int): User?
 *
 *      @Insert(onConflict = OnConflictStrategy.REPLACE)
 *      suspend fun insert(user: User)
 *  }
 *
 * ── 4.2  Retrofit 注解  ★ 必学 ────────────────────────────────────────────────
 *
 *  · @GET / @POST / @PUT / @DELETE：HTTP 方法
 *  · @Path / @Query / @Body / @Header：参数注解
 *  · Retrofit 通过动态代理 + 运行时反射读取注解，生成请求
 *
 *  interface UserApi {
 *      @GET("users/{id}")
 *      suspend fun getUser(@Path("id") id: Int): User
 *
 *      @POST("users")
 *      suspend fun createUser(@Body user: User): Response<User>
 *
 *      @GET("users")
 *      suspend fun getUsers(
 *          @Query("page") page: Int,
 *          @Query("size") size: Int
 *      ): List<User>
 *  }
 *
 * ── 4.3  Hilt 注解  ★ 必学 ────────────────────────────────────────────────────
 *
 *  · @HiltAndroidApp / @AndroidEntryPoint / @HiltViewModel：入口注解
 *  · @Inject / @Module / @Provides / @Binds：依赖提供注解
 *  · KSP/APT 在编译期生成 DaggerXxxComponent 等注入代码
 *  · 运行时零反射，性能优于手动反射注入
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  最佳实践  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  ✅ 应该做：
 *  · 新项目用 KSP 替代 kapt，编译更快
 *  · 需要运行时反射读取的注解用 @Retention(RUNTIME)
 *  · 仅编译期处理的注解用 @Retention(SOURCE)，减少 .class 体积
 *  · 查看 APT/KSP 生成的代码（build/generated/）理解框架原理
 *  · 自定义处理器用 KotlinPoet 生成 Kotlin 代码（比手拼字符串更安全）
 *
 *  ❌ 不应该做：
 *  · 想用反射读取注解却用了 SOURCE/BINARY（运行时读不到）
 *  · 在性能敏感路径大量使用运行时反射读取注解（有性能开销）
 *  · 混用 kapt 和 KSP 处理同一个库（会导致重复处理）
 */

val annotationAptData = NoteData(
    title = "注解与 APT / KSP",
    subtitle = "@Target · @Retention · APT · KSP · Room/Retrofit/Hilt 注解原理",
    color = Color.parseColor("#3F51B5"),
    chapters = listOf(
        ChapterItem("1",   "注解基础  ★ 必学"),
        ChapterItem("1.1", "注解的本质：元数据 / 三种处理方式  ★ 必学"),
        ChapterItem("1.2", "定义注解：@Target / @Retention / annotation class  ★ 必学"),
        ChapterItem("1.3", "@Retention 三种策略：SOURCE / BINARY / RUNTIME  ★ 必学"),
        ChapterItem("1.4", "运行时读取注解（反射）★ 常用"),
        ChapterItem("2",   "APT（注解处理器）★ 必学"),
        ChapterItem("2.1", "APT 工作原理：编译期生成代码  ★ 必学"),
        ChapterItem("2.2", "自定义 APT 处理器：AbstractProcessor + KotlinPoet  ★ 常用"),
        ChapterItem("3",   "KSP（Kotlin Symbol Processing）★ 必学"),
        ChapterItem("3.1", "KSP vs kapt 对比：速度 / Kotlin 支持 / KMP  ★ 必学"),
        ChapterItem("3.2", "KSP 配置：ksp() 依赖声明  ★ 常用"),
        ChapterItem("3.3", "自定义 KSP 处理器：SymbolProcessor  ★ 常用"),
        ChapterItem("4",   "典型框架中的注解应用  ★ 必学"),
        ChapterItem("4.1", "Room 注解：@Entity / @Dao / @Query  ★ 必学"),
        ChapterItem("4.2", "Retrofit 注解：@GET / @POST / @Path / @Body  ★ 必学"),
        ChapterItem("4.3", "Hilt 注解：编译期生成注入代码，运行时零反射  ★ 必学"),
        ChapterItem("5",   "最佳实践  ★ 必学"),
    )
)
