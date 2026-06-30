package com.example.androidlearn.feature.senior.detail.stage16

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【代码混淆与裁剪】专属学习页
//  stageIndex=15, topicIndex=2
//  阶段颜色：青绿 0xFF00897B（工程化进阶）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "代码混淆与裁剪",
    description = "R8/ProGuard 工作原理、Keep 规则、资源裁剪与混淆调试（mapping.txt）",
    overview = "R8 是 Android 官方的代码优化工具，集成了混淆（Obfuscation）、裁剪（Tree Shaking/Shrinking）和优化（Optimization）三大功能。合理配置混淆规则，能有效缩减 APK 体积、提升反编译门槛，是商业 App 发布的必要步骤。",
    keyPoints = listOf(
        "R8 三大功能：Shrinking（删除未使用代码/资源）+ Obfuscation（重命名类/方法/字段为 a/b/c）+ Optimization（方法内联、常量折叠等）",
        "-keep 规则：-keep class 保留类及成员；-keepclassmembers 只保留成员不保留类名；-keepnames 保留名称但允许裁剪",
        "mapping.txt：R8 生成的符号映射文件，用于还原混淆后的崩溃堆栈（retrace）。发布时必须保留此文件",
        "资源裁剪（shrinkResources=true）：配合 minifyEnabled 使用，删除代码未引用的资源文件和字符串",
        "反射使用的类/方法需要 Keep：通过反射访问的类名在 R8 混淆后会找不到，导致 ClassNotFoundException",
        "第三方库 ProGuard 规则：主流库（Retrofit、Gson、Room 等）通常在 AAR 中内置 consumer-rules.pro，自动应用"
    ),
    codeSnippet = """
// build.gradle.kts 开启 R8
android {
    buildTypes {
        release {
            minifyEnabled = true       // 开启代码裁剪+混淆
            shrinkResources = true     // 开启资源裁剪（需 minifyEnabled=true）
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

// proguard-rules.pro 常用规则
// 保留所有 Activity 不被裁剪（AndroidManifest 中引用的会自动保留）
-keep public class * extends android.app.Activity

// 保留数据类（用于 JSON 序列化/反序列化）
-keep class com.example.model.** { *; }

// 保留带注解的方法（如 @SerializedName）
-keepclassmembers class ** {
    @com.google.gson.annotations.SerializedName <fields>;
}

// 保留枚举（枚举有特殊方法 values()/valueOf()）
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

// 保留 Parcelable（序列化接口实现）
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

// 保留反射使用的类
-keep class com.example.plugin.** { *; }

// 混淆后还原崩溃堆栈（使用 retrace 工具）
// $ retrace mapping.txt crash_log.txt
// 或使用 Google Play Console 自动 retrace（需上传 mapping.txt）

// 查看混淆后的映射（mapping.txt 格式示例）
// com.example.MyClass -> a.b.c:
//     void onCreate(android.os.Bundle) -> a
//     String getUserName() -> b

// 调试混淆问题（仅混淆，不裁剪）
-dontobfuscate   // 禁用混淆（调试用，找到问题后删除）
-dontshrink      // 禁用裁剪
    """.trimIndent(),
    tips = listOf(
        "每次发布 Release 版本都应保存 mapping.txt（建议与版本号关联存档），线上崩溃 retrace 依赖它",
        "过度 Keep 会使 R8 裁剪效果大打折扣。使用 -printusage unused.txt 查看哪些代码被裁剪，辅助优化规则",
        "Kotlin data class 的 copy()/componentN() 方法在只用于 Java 互操作时会被裁剪，需要时添加 Keep 规则"
    )
)

@Composable
fun ProguardScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF00897B),
        stageTitle = "工程化进阶",
        onBack = onBack
    )
}
