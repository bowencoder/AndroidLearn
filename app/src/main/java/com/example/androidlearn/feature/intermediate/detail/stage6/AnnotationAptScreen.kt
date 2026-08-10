package com.example.androidlearn.feature.intermediate.detail.stage6

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * 注解与 APT
 * 官方文档：https://developer.android.com/studio/build/dependencies#annotation_processor
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  注解基础
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  定义注解 ─────────────────────────────────────────────────────────────
 *
 *  · @interface（Java）/ annotation class（Kotlin）：定义注解
 *  · @Target：注解作用目标（类/函数/属性）
 *  · @Retention：保留策略（SOURCE / BINARY / RUNTIME）
 *
 *  // Kotlin 定义注解
 *  @Target(AnnotationTarget.FUNCTION)
 *  @Retention(AnnotationRetention.SOURCE)   // 仅保留在源码，APT 处理后丢弃
 *  annotation class BindView(val id: Int)
 *
 * ── 1.2  使用注解 ─────────────────────────────────────────────────────────────
 *
 *  class MainActivity : AppCompatActivity() {
 *      @BindView(R.id.tvTitle)
 *      lateinit var tvTitle: TextView
 *
 *      override fun onCreate(savedInstanceState: Bundle?) {
 *          super.onCreate(savedInstanceState)
 *          // APT 生成的代码会在编译期生成 MainActivity_ViewBinding 类
 *          // 自动完成 tvTitle = findViewById(R.id.tvTitle)
 *          ViewBinder.bind(this)
 *      }
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  APT（注解处理器）
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  原理 ─────────────────────────────────────────────────────────────────
 *
 *  · APT（Annotation Processing Tool）：编译期扫描注解，生成 Java/Kotlin 源码文件
 *  · AbstractProcessor：实现 process() 方法处理注解元素，生成代码
 *  · 生成的代码在 build/generated/ 目录，可以直接查看调试
 *
 * ── 2.2  APT 处理器示例 ───────────────────────────────────────────────────────
 *
 *  @SupportedAnnotationTypes("com.example.BindView")
 *  class BindViewProcessor : AbstractProcessor() {
 *      override fun process(
 *          annotations: Set<TypeElement>,
 *          roundEnv: RoundEnvironment
 *      ): Boolean {
 *          roundEnv.getElementsAnnotatedWith(BindView::class.java)
 *              .forEach { element ->
 *                  val viewId = element.getAnnotation(BindView::class.java).id
 *                  // 用 JavaPoet 或 KotlinPoet 生成绑定代码文件
 *                  generateBindingCode(element, viewId)
 *              }
 *          return true
 *      }
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  KSP（Kotlin Symbol Processing）
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · Kotlin 原生注解处理，比 kapt（APT）快 2x
 *  · 与 Kotlin 更兼容，支持多平台（KMP）
 *  · 新项目优先用 KSP 替代 kapt
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  典型应用
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · Room：生成 DAO 实现类
 *  · Hilt：生成依赖注入代码
 *  · Retrofit：生成网络代理
 *  · ButterKnife：生成 View 绑定代码（已废弃，ViewBinding 替代）
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 新项目优先用 KSP 替代 kapt（APT），速度快、与 Kotlin 更兼容
 *  · APT 生成的代码在 build/generated/ 目录，可以直接查看调试
 *  · 注解 @Retention(RUNTIME) 才能在运行时通过反射读取，SOURCE/BINARY 会被丢弃
 */

val annotationAptData = NoteData(
    title = "注解与 APT",
    subtitle = "泛型、注解与动态编程 · APT · KSP · AbstractProcessor",
    color = Color.parseColor("#3F51B5"),
    chapters = listOf(
        ChapterItem("1",   "注解基础"),
        ChapterItem("1.1", "定义注解"),
        ChapterItem("1.2", "使用注解"),
        ChapterItem("2",   "APT（注解处理器）"),
        ChapterItem("2.1", "原理"),
        ChapterItem("2.2", "APT 处理器示例"),
        ChapterItem("3",   "KSP（Kotlin Symbol Processing）"),
        ChapterItem("4",   "典型应用"),
        ChapterItem("5",   "最佳实践"),
    )
)
