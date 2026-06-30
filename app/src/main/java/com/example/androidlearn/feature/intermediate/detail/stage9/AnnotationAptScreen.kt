package com.example.androidlearn.feature.intermediate.detail.stage9

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【注解与 APT】专属学习页
//  stageIndex=8, topicIndex=1
//  阶段颜色：靛蓝 0xFF3F51B5（中级扩展 Stage 8）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "注解与 APT",
    description = "自定义注解、注解处理器、APT 实现原理、ButterKnife 架构实战",
    overview = "注解是 Android 框架设计的核心工具（Hilt/Room/Retrofit 全靠注解驱动），APT（注解处理器）在编译期生成代码，避免运行时反射开销。",
    keyPoints = listOf(
        "@Target / @Retention：注解作用目标（类/函数/属性）和保留策略（源码/字节码/运行时）",
        "@interface（Java）/ annotation class（Kotlin）：定义注解",
        "APT（Annotation Processing Tool）：编译期扫描注解，生成 Java/Kotlin 源码文件",
        "AbstractProcessor：实现 process() 方法处理注解元素，生成代码",
        "KSP（Kotlin Symbol Processing）：Kotlin 原生注解处理，比 APT 快 2x",
        "典型应用：Room（生成 DAO 实现）、Hilt（生成注入代码）、Retrofit（生成网络代理）"
    ),
    codeSnippet = """
// 1. 定义注解
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)   // 仅保留在源码，APT 处理后丢弃
annotation class BindView(val id: Int)

// 2. 在代码中使用
class MainActivity : AppCompatActivity() {
    @BindView(R.id.tvTitle)
    lateinit var tvTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // APT 生成的代码会在编译期生成 MainActivity_ViewBinding 类
        // 自动完成 tvTitle = findViewById(R.id.tvTitle)
        ViewBinder.bind(this)
    }
}

// 3. APT 处理器核心（简化版）
@SupportedAnnotationTypes("com.example.BindView")
class BindViewProcessor : AbstractProcessor() {
    override fun process(
        annotations: Set<TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        roundEnv.getElementsAnnotatedWith(BindView::class.java)
            .forEach { element ->
                val viewId = element.getAnnotation(BindView::class.java).id
                // 用 JavaPoet 或 KotlinPoet 生成绑定代码文件
                generateBindingCode(element, viewId)
            }
        return true
    }
}
    """.trimIndent(),
    tips = listOf(
        "新项目优先用 KSP 替代 kapt（APT），速度快、与 Kotlin 更兼容",
        "APT 生成的代码在 build/generated/ 目录，可以直接查看调试",
        "注解 @Retention(RUNTIME) 才能在运行时通过反射读取，SOURCE/BINARY 会被丢弃"
    )
)

@Composable
fun AnnotationAptScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF3F51B5),
        stageTitle = "泛型、注解与动态编程",
        onBack = onBack
    )
}
