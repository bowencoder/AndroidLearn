package com.example.androidlearn.feature.senior.detail.stage11

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【Android 虚拟机指令】专属学习页
//  stageIndex=10, topicIndex=0
//  阶段颜色：深青 0xFF009688（高级扩展 Stage 10）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "Android 虚拟机指令",
    description = "Dalvik/ART 指令集解读、深入内存管理、字节码动态替换方案",
    overview = "Dalvik/ART 虚拟机是 Android 应用运行的基础。理解字节码指令集、内存布局和字节码替换机制，是实现热修复、插件化的核心前提。",
    keyPoints = listOf(
        "Dalvik vs ART：Dalvik JIT 即时编译；ART AOT 预编译 + JIT（Android 7+），启动更快",
        "DEX 格式：Dalvik Executable，专为移动端优化的字节码格式，多个类共享常量池",
        "Dalvik 指令集：基于寄存器（非 JVM 的栈式），指令更少、执行效率更高",
        "smali/baksmali：DEX 的汇编/反汇编工具，用于逆向和热修复分析",
        "字节码动态替换：通过替换 ArtMethod 指针实现方法级热修复（Robust 方案）",
        "类加载时机：第一次访问时懒加载，ClassLoader 双亲委派保证核心类不被篡改"
    ),
    codeSnippet = """
// smali 字节码示例（对应 Kotlin fun add(a: Int, b: Int): Int = a + b）
// .method public add(II)I
//     .registers 4        # 4 个寄存器（v0=返回值，v1=this，v2=a，v3=b）
//     add-int v0, v2, v3  # v0 = v2 + v3
//     return v0           # 返回 v0
// .end method

// 字节码插桩热修复核心思路（Robust 方案）
// 在每个方法入口插入：
// if (changeQuickRedirect != null) {
//     return PatchProxy.accessDispatch(this, args, changeQuickRedirect)
// }

// 运行时通过反射替换 ArtMethod 指针（底层方案）
fun patchMethod(origin: Method, patch: Method) {
    val artMethodSize = getArtMethodSize()
    val originAddr = getMethodAddress(origin)
    val patchAddr = getMethodAddress(patch)
    // 内存拷贝：将 patch 的 ArtMethod 内容覆盖到 origin
    // 此后调用 origin 实际执行 patch 的逻辑
    memcpy(originAddr, patchAddr, artMethodSize)
}

// 查看 DEX 文件内容（工具）
// $ dexdump -d classes.dex        # 反汇编所有方法
// $ adb shell am dumpheap <pid> /sdcard/heap.hprof  # 导出堆快照
    """.trimIndent(),
    tips = listOf(
        "ART Android 7+ 采用混合模式：AOT 编译常用代码 + JIT 编译热点代码 + 解释执行",
        "用 jadx/apktool 可以将 APK 反编译为 smali 或伪 Java 代码，辅助逆向分析",
        "热修复替换 ArtMethod 在 Android 10+ 受到限制，现代方案更多用代码插桩"
    )
)

@Composable
fun DalvikArtScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF009688),
        stageTitle = "Android 虚拟机原理",
        onBack = onBack
    )
}
