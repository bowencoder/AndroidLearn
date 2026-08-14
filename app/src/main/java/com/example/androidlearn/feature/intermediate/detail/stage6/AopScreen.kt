package com.example.androidlearn.feature.intermediate.detail.stage6

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * AOP 面向切面编程
 * 官方文档：https://developer.android.com/build/extend-agp
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  0  AOP 核心思想  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · AOP（Aspect-Oriented Programming）：将横切关注点（日志、权限、埋点）从业务逻辑中分离
 *  · 核心概念：
 *    - 切面（Aspect）：横切逻辑的封装（如：耗时统计切面）
 *    - 切入点（Pointcut）：指定在哪些方法上织入（如：所有加了 @TrackTime 的方法）
 *    - 通知（Advice）：织入的具体逻辑（Before / After / Around）
 *    - 织入（Weaving）：将切面逻辑插入目标代码的过程
 *
 *  · Android 中 AOP 的三种实现方式：
 *  ┌──────────────────┬──────────────────────┬──────────────────────────────┐
 *  │      方式         │       时机            │          特点                │
 *  ├──────────────────┼──────────────────────┼──────────────────────────────┤
 *  │ AspectJ          │ 编译期字节码织入       │ 简单易用，支持注解，编译慢     │
 *  │ ASM 字节码插桩    │ 编译期 .class 修改    │ 灵活强大，大厂首选，学习成本高  │
 *  │ 动态代理          │ 运行时               │ 只能代理接口，零编译开销        │
 *  └──────────────────┴──────────────────────┴──────────────────────────────┘
 *
 *  · 典型应用场景：方法耗时统计、无埋点采集、登录态拦截、权限检查、防重复点击
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  AspectJ  ★ 常用
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  核心注解  ★ 常用 ──────────────────────────────────────────────────────
 *
 *  · @Aspect：标记切面类
 *  · @Pointcut：定义切入点表达式（可复用）
 *  · @Around：环绕通知，可在方法前后插入逻辑，可修改返回值
 *  · @Before：方法执行前通知
 *  · @After：方法执行后通知（无论是否异常）
 *  · @AfterReturning：方法正常返回后通知
 *  · @AfterThrowing：方法抛出异常后通知
 *
 *  · Pointcut 表达式语法：
 *    - execution(* com.example..*.*(..))：匹配包下所有方法
 *    - @annotation(com.example.TrackTime)：匹配加了指定注解的方法
 *    - within(com.example.ui.*)：匹配指定包内所有类的方法
 *
 * ── 1.2  示例：方法耗时统计  ★ 常用 ───────────────────────────────────────────
 *
 *  // 1. 定义注解
 *  @Target(AnnotationTarget.FUNCTION)
 *  @Retention(AnnotationRetention.RUNTIME)
 *  annotation class TrackTime
 *
 *  // 2. 定义切面
 *  @Aspect
 *  class TimingAspect {
 *
 *      // 切入点：所有加了 @TrackTime 的方法
 *      @Pointcut("@annotation(com.example.TrackTime)")
 *      fun trackTimePointcut() {}
 *
 *      // 环绕通知
 *      @Around("trackTimePointcut()")
 *      @Throws(Throwable::class)
 *      fun aroundTrackTime(joinPoint: ProceedingJoinPoint): Any? {
 *          val start = SystemClock.elapsedRealtime()
 *          val result = joinPoint.proceed()   // 执行原方法
 *          val cost = SystemClock.elapsedRealtime() - start
 *          Log.d("AOP", "${joinPoint.signature.name} 耗时: ${cost}ms")
 *          return result
 *      }
 *  }
 *
 *  // 3. 使用：只需加注解，无需改动方法本身
 *  @TrackTime
 *  fun loadUserData() { /* 业务逻辑不变 */ }
 *
 * ── 1.3  示例：登录态拦截  ★ 常用 ─────────────────────────────────────────────
 *
 *  @Target(AnnotationTarget.FUNCTION)
 *  @Retention(AnnotationRetention.RUNTIME)
 *  annotation class LoginRequired
 *
 *  @Aspect
 *  class LoginAspect {
 *      @Around("@annotation(com.example.LoginRequired)")
 *      @Throws(Throwable::class)
 *      fun checkLogin(joinPoint: ProceedingJoinPoint): Any? {
 *          val context = AppContext.get()
 *          return if (UserManager.isLoggedIn()) {
 *              joinPoint.proceed()  // 已登录，执行原方法
 *          } else {
 *              context.startActivity(Intent(context, LoginActivity::class.java))
 *              null  // 未登录，跳转登录页，不执行原方法
 *          }
 *      }
 *  }
 *
 *  // 使用
 *  @LoginRequired
 *  fun onFavoriteClick() { /* 收藏逻辑 */ }
 *
 * ── 1.4  AspectJ 配置（build.gradle.kts）─────────────────────────────────────
 *
 *  // 项目级 build.gradle.kts
 *  buildscript {
 *      dependencies {
 *          classpath("org.aspectj:aspectjtools:1.9.20")
 *      }
 *  }
 *
 *  // 模块级 build.gradle.kts（需自定义 Task 织入）
 *  dependencies {
 *      implementation("org.aspectj:aspectjrt:1.9.20")
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  ASM 字节码插桩  ★ 常用
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  核心思路  ★ 常用 ──────────────────────────────────────────────────────
 *
 *  · ASM 是轻量级字节码操作框架，直接读写 .class 文件
 *  · 在编译期（Gradle Transform / AGP Transform API）修改字节码
 *  · 比 AspectJ 更灵活，大厂生产环境首选（性能监控、无埋点、热修复）
 *
 *  · AGP 7+ 推荐使用 AsmClassVisitorFactory（替代旧的 Transform API）
 *  · 访问者模式：ClassVisitor → MethodVisitor → 在方法入口/出口插入指令
 *
 * ── 2.2  AsmClassVisitorFactory 示例（AGP 7+）★ 常用 ──────────────────────────
 *
 *  // 1. 实现 ClassVisitorFactory
 *  abstract class TimingClassVisitorFactory :
 *      AsmClassVisitorFactory<InstrumentationParameters.None> {
 *
 *      override fun createClassVisitor(
 *          classContext: ClassContext,
 *          nextClassVisitor: ClassVisitor
 *      ): ClassVisitor = TimingClassVisitor(nextClassVisitor)
 *
 *      override fun isInstrumentable(classData: ClassData): Boolean =
 *          classData.className.startsWith("com.example")  // 只处理自己的类
 *  }
 *
 *  // 2. 实现 ClassVisitor
 *  class TimingClassVisitor(cv: ClassVisitor) : ClassVisitor(ASM9, cv) {
 *      override fun visitMethod(
 *          access: Int, name: String, desc: String,
 *          signature: String?, exceptions: Array<String>?
 *      ): MethodVisitor {
 *          val mv = super.visitMethod(access, name, desc, signature, exceptions)
 *          return TimingMethodVisitor(ASM9, mv, access, name, desc)
 *      }
 *  }
 *
 *  // 3. 实现 MethodVisitor（在方法入口/出口插入计时代码）
 *  class TimingMethodVisitor(
 *      api: Int, mv: MethodVisitor, access: Int, name: String, desc: String
 *  ) : AdviceAdapter(api, mv, access, name, desc) {
 *
 *      private var startVar = -1
 *
 *      override fun onMethodEnter() {
 *          // 插入：long start = SystemClock.elapsedRealtime();
 *          mv.visitMethodInsn(INVOKESTATIC,
 *              "android/os/SystemClock", "elapsedRealtime", "()J", false)
 *          startVar = newLocal(Type.LONG_TYPE)
 *          mv.visitVarInsn(LSTORE, startVar)
 *      }
 *
 *      override fun onMethodExit(opcode: Int) {
 *          // 插入：Log.d("ASM", name + " 耗时: " + (SystemClock.elapsedRealtime() - start) + "ms");
 *          mv.visitVarInsn(LLOAD, startVar)
 *          mv.visitMethodInsn(INVOKESTATIC,
 *              "android/os/SystemClock", "elapsedRealtime", "()J", false)
 *          mv.visitInsn(LSUB)
 *          // ... 拼接日志字符串并调用 Log.d
 *      }
 *  }
 *
 *  // 4. 在 Gradle Plugin 中注册
 *  androidComponents.onVariants { variant ->
 *      variant.instrumentation.transformClassesWith(
 *          TimingClassVisitorFactory::class.java,
 *          InstrumentationScope.ALL
 *      ) {}
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  动态代理（运行时 AOP）★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 3.1  JDK 动态代理  ★ 必学 ─────────────────────────────────────────────────
 *
 *  · 只能代理接口（不能代理普通类）
 *  · 运行时通过反射拦截方法调用
 *  · Retrofit 的核心原理：动态代理 + 注解解析
 *
 *  interface UserApi {
 *      fun getUser(id: Int): User
 *  }
 *
 *  // 创建代理
 *  val proxy = Proxy.newProxyInstance(
 *      UserApi::class.java.classLoader,
 *      arrayOf(UserApi::class.java)
 *  ) { _, method, args ->
 *      val start = System.currentTimeMillis()
 *      val result = method.invoke(realApi, *args.orEmpty())
 *      Log.d("Proxy", "${method.name} 耗时: ${System.currentTimeMillis() - start}ms")
 *      result
 *  } as UserApi
 *
 *  proxy.getUser(1)  // 自动触发 InvocationHandler
 *
 * ── 3.2  Retrofit 动态代理原理  ★ 常用 ────────────────────────────────────────
 *
 *  // Retrofit.create() 核心逻辑（简化）
 *  fun <T> create(service: Class<T>): T {
 *      return Proxy.newProxyInstance(
 *          service.classLoader,
 *          arrayOf(service)
 *      ) { _, method, args ->
 *          // 1. 解析方法上的注解（@GET、@POST、@Path 等）
 *          val annotations = method.annotations
 *          // 2. 构建 OkHttp Request
 *          val request = buildRequest(method, annotations, args)
 *          // 3. 执行网络请求
 *          okHttpClient.newCall(request).execute()
 *      } as T
 *  }
 *
 * ── 3.3  CGLIB 代理  ★ 了解 ────────────────────────────────────────────────
 *
 *  · 字节码子类化，可代理普通类（不限于接口），弥补 JDK 动态代理的限制
 *  · Android 不内置，需引入依赖（cglib / cglib-nodep）
 *  · Spring AOP 默认使用 CGLIB 代理普通 Bean
 *  · Android 端极少直接使用，了解原理即可；需要代理普通类时优先选 ASM 插桩
 *
 *  // CGLIB 代理示例（Java/Kotlin 通用，Android 需额外依赖）
 *  class UserService {
 *      fun getUser(id: Int): String = "User-$id"
 *  }
 *
 *  val enhancer = Enhancer()
 *  enhancer.setSuperclass(UserService::class.java)
 *  enhancer.setCallback(MethodInterceptor { obj, method, args, proxy ->
 *      println("Before: ${method.name}")
 *      val result = proxy.invokeSuper(obj, args)
 *      println("After: ${method.name}")
 *      result
 *  })
 *  val proxy = enhancer.create() as UserService
 *  proxy.getUser(1)  // 触发 MethodInterceptor
 *
 *  · JDK 动态代理 vs CGLIB 对比：
 *  ┌──────────────┬──────────────────────┬──────────────────────┐
 *  │              │   JDK 动态代理        │      CGLIB           │
 *  ├──────────────┼──────────────────────┼──────────────────────┤
 *  │ 代理目标      │ 只能代理接口          │ 可代理普通类          │
 *  │ 实现方式      │ 反射 + 接口           │ 字节码子类化          │
 *  │ 性能          │ 较快（JDK 优化）      │ 首次创建慢，调用快    │
 *  │ Android 支持  │ 原生支持              │ 需额外依赖            │
 *  │ 典型使用      │ Retrofit.create()    │ Spring AOP           │
 *  └──────────────┴──────────────────────┴──────────────────────┘
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  Kotlin 委托（轻量 AOP）★ 常用
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · by 关键字实现接口委托，无需反射，编译期生成代码
 *  · 适合简单的方法拦截场景，比动态代理性能更好
 *
 *  // 日志委托
 *  class LoggingList<T>(private val inner: MutableList<T>) : MutableList<T> by inner {
 *      override fun add(element: T): Boolean {
 *          Log.d("List", "add: $element")
 *          return inner.add(element)
 *      }
 *      override fun remove(element: T): Boolean {
 *          Log.d("List", "remove: $element")
 *          return inner.remove(element)
 *      }
 *  }
 *
 *  // 属性委托（lazy、observable）
 *  val config: Config by lazy { loadConfig() }  // 懒加载
 *
 *  var name: String by Delegates.observable("") { _, old, new ->
 *      Log.d("Delegate", "name changed: $old → $new")
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  AOP 方案对比与选型  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  ┌──────────────────┬────────────┬────────────┬────────────┬──────────────────┐
 *  │      方案         │  织入时机  │  代理目标  │  编译影响  │    适用场景       │
 *  ├──────────────────┼────────────┼────────────┼────────────┼──────────────────┤
 *  │ AspectJ          │ 编译期     │ 类/方法    │ 增加编译时间│ 快速验证/小项目   │
 *  │ ASM 插桩         │ 编译期     │ 类/方法    │ 增加编译时间│ 生产环境/大厂     │
 *  │ JDK 动态代理      │ 运行时     │ 接口       │ 无         │ 网络层拦截        │
 *  │ CGLIB 代理        │ 运行时     │ 普通类     │ 无         │ Spring/服务端     │
 *  │ Kotlin 委托       │ 编译期     │ 接口       │ 无         │ 简单属性/接口拦截 │
 *  └──────────────────┴────────────┴────────────┴────────────┴──────────────────┘
 *
 *  · 快速验证/小项目：AspectJ（配置简单，注解驱动）
 *  · 生产环境/大厂：ASM（灵活，性能好，可精确控制）
 *  · 网络层/接口代理：JDK 动态代理（Retrofit 原理）
 *  · 代理普通类（服务端）：CGLIB（Spring AOP 默认方案）
 *  · 简单属性/接口拦截：Kotlin 委托（零开销）
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  6  最佳实践  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  ✅ 应该做：
 *  · 横切关注点（日志、埋点、权限、耗时）用 AOP 解耦，不污染业务代码
 *  · 生产环境优先用 ASM（AsmClassVisitorFactory），比 AspectJ 更稳定
 *  · Transform 只在 Release 或专用 variant 启用，减少 Debug 编译时间
 *  · 动态代理用于接口层拦截（网络、缓存），Kotlin 委托用于简单场景
 *
 *  ❌ 不应该做：
 *  · 不要用 AOP 处理核心业务逻辑（难以调试和追踪）
 *  · 不要在 Transform 中处理所有类（用 isInstrumentable 过滤，减少编译时间）
 *  · 动态代理不能代理普通类（只能代理接口），需要代理类用 ASM
 */

val aopData = NoteData(
    title = "AOP 面向切面编程",
    subtitle = "AspectJ · ASM 字节码插桩 · 动态代理 · Kotlin 委托",
    color = Color.parseColor("#3F51B5"),
    chapters = listOf(
        ChapterItem("0",   "AOP 核心思想：切面 / 切入点 / 通知 / 织入  ★ 必学"),
        ChapterItem("1",   "AspectJ  ★ 常用"),
        ChapterItem("1.1", "核心注解：@Aspect / @Pointcut / @Around / @Before  ★ 常用"),
        ChapterItem("1.2", "示例：方法耗时统计（@TrackTime）★ 常用"),
        ChapterItem("1.3", "示例：登录态拦截（@LoginRequired）★ 常用"),
        ChapterItem("1.4", "AspectJ Gradle 配置"),
        ChapterItem("2",   "ASM 字节码插桩  ★ 常用"),
        ChapterItem("2.1", "核心思路：编译期修改 .class / AsmClassVisitorFactory  ★ 常用"),
        ChapterItem("2.2", "AsmClassVisitorFactory 完整示例（AGP 7+）★ 常用"),
        ChapterItem("3",   "动态代理（运行时 AOP）★ 必学"),
        ChapterItem("3.1", "JDK 动态代理：Proxy / InvocationHandler  ★ 必学"),
        ChapterItem("3.2", "Retrofit 动态代理原理  ★ 常用"),
        ChapterItem("3.3", "CGLIB 代理：字节码子类化，可代理普通类  ★ 了解"),
        ChapterItem("4",   "Kotlin 委托（轻量 AOP）★ 常用"),
        ChapterItem("5",   "AOP 方案对比与选型  ★ 必学"),
        ChapterItem("6",   "最佳实践  ★ 必学"),
    )
)
