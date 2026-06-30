package com.example.androidlearn.feature.senior.detail.stage6

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "自定义 Gradle 插件",
    description = "buildSrc / Composite Build，Transform API，字节码插桩",
    overview = "Gradle 插件是 Android 工程化的核心工具，从统一构建配置到字节码插桩实现 AOP，都依赖自定义插件能力。",
    keyPoints = listOf(
        "buildSrc：项目内 Gradle 插件，自动加入 classpath，适合小型插件",
        "Composite Build：独立插件工程，通过 includeBuild 引入，适合发布插件",
        "Plugin<Project> 接口：实现 apply() 配置项目",
        "Transform API（AGP 7+）→ AsmClassVisitorFactory：字节码变换",
        "ASM：轻量字节码操作框架，插入方法耗时统计、日志等",
        "Task 依赖：dependsOn / finalizedBy 控制任务执行顺序"
    ),
    codeSnippet = """
// buildSrc/src/main/kotlin/TimeTracePlugin.kt
class TimeTracePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val android = project.extensions.getByType(AndroidComponentsExtension::class.java)
        android.onVariants { variant ->
            variant.instrumentation.transformClassesWith(
                TimeTraceTransform::class.java,
                InstrumentationScope.ALL
            ) { }
        }
    }
}

// 字节码插桩 - 在方法前后插入耗时统计
class TimeTraceTransform : AsmClassVisitorFactory<InstrumentationParameters.None> {
    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor = TimeTraceClassVisitor(nextClassVisitor)
}
    """.trimIndent(),
    tips = listOf(
        "先用 buildSrc 快速验证，稳定后迁移到 Composite Build 独立发布",
        "Transform 会显著增加构建时间，只在 Release 或特定 variant 启用",
        "ASM 操作字节码前先用 Bytecode Viewer 查看目标类的字节码结构"
    )
)

@Composable
fun GradlePluginScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF3F51B5),
        stageTitle = "架构设计与前沿技术",
        onBack = onBack
    )
}
