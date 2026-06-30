package com.example.androidlearn.feature.senior.detail.stage16

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【AGP 与 Transform API】专属学习页
//  stageIndex=15, topicIndex=0
//  阶段颜色：青绿 0xFF00897B（工程化进阶）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "AGP 与 Transform API",
    description = "Android Gradle Plugin 架构、Task 依赖、Transform API（字节码插桩）与 AGP 8.x 新 API",
    overview = "Android Gradle Plugin（AGP）是 Android 项目构建的核心基础。理解 AGP 的 Task 依赖图、Transform API（字节码处理）和 Variant 配置，是做构建速度优化、字节码插桩（AOP、埋点、线程监控）和自定义构建流程的必备技能。",
    keyPoints = listOf(
        "AGP 架构：基于 Gradle 的 Task DAG（有向无环图），每个构建步骤是一个 Task（如 compileDebugKotlin、packageDebug）",
        "Transform API（AGP 7.x 前）：在 .class → .dex 阶段插入字节码处理逻辑，可遍历所有 class 文件进行 AOP 插桩",
        "AGP 8.x 新 API：Transform 废弃，改用 Instrumentation API（TransformAction）和 AsmClassVisitorFactory，更高效支持增量编译",
        "自定义 Gradle Plugin：实现 Plugin<Project> 接口，通过 project.tasks.register 注册 Task，在 afterEvaluate 时配置 Task 依赖",
        "Variant API：访问 Build Variant（Debug/Release）的 Artifact，可在构建流程中修改 Manifest、资源、代码",
        "构建缓存：Gradle Build Cache 和 Configuration Cache 是加速构建的关键，Task 输入/输出声明完整才能命中缓存"
    ),
    codeSnippet = """
// 自定义 Gradle Plugin（Kotlin DSL）
class MyPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // 注册扩展配置
        val extension = project.extensions.create("myConfig", MyExtension::class.java)

        // 注册自定义 Task
        project.tasks.register("generateCode", GenerateCodeTask::class.java) { task ->
            task.outputDir.set(project.layout.buildDirectory.dir("generated/my_code"))
        }

        // 在 afterEvaluate 中配置依赖（确保 AGP Task 已注册）
        project.afterEvaluate {
            project.tasks.named("compileDebugKotlin").configure { compileTask ->
                compileTask.dependsOn("generateCode")
            }
        }
    }
}

// AGP 8.x 字节码插桩（AsmClassVisitorFactory）
abstract class TimingVisitorFactory :
    AsmClassVisitorFactory<InstrumentationParameters.None> {

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor = TimingClassVisitor(nextClassVisitor)

    override fun isInstrumentable(classData: ClassData): Boolean {
        // 只处理自己的包
        return classData.className.startsWith("com.example")
    }
}

// 在 build.gradle.kts 中注册
androidComponents {
    onVariants { variant ->
        variant.instrumentation.transformClassesWith(
            TimingVisitorFactory::class.java,
            InstrumentationScope.ALL
        ) {}
        variant.instrumentation.setAsmFramesComputationMode(
            FramesComputationMode.COPY_FRAMES
        )
    }
}

// 查看 Task 依赖图
// $ ./gradlew app:assembleDebug --dry-run   // 列出所有将执行的 Task
// $ ./gradlew app:assembleDebug --scan      // 生成 Build Scan 分析报告
    """.trimIndent(),
    tips = listOf(
        "AGP 8.0+ 废弃了 Transform API，字节码插桩必须迁移到 AsmClassVisitorFactory，新 API 天然支持增量编译",
        "Gradle Configuration Cache 要求 Task 的 Action 不能捕获 Project 对象，需仔细检查 Plugin 实现",
        "使用 --profile 参数生成 HTML 构建性能报告，找出构建瓶颈 Task（通常是 kaptDebugKotlin 或资源处理）"
    )
)

@Composable
fun AgpTransformScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF00897B),
        stageTitle = "工程化进阶",
        onBack = onBack
    )
}
