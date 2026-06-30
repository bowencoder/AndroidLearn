package com.example.androidlearn.feature.senior.detail.stage13

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "CMake 构建配置",
    description = "CMakeLists 编写，so 库编译，ABI 过滤，第三方库集成",
    overview = "CMake 是 Android NDK 推荐的 Native 构建工具。通过 CMakeLists.txt 配置编译规则，Gradle 驱动 CMake 生成各 ABI 架构的 .so 动态库。",
    keyPoints = listOf(
        "ABI 架构：arm64-v8a（主流 64位）、armeabi-v7a（32位兼容）、x86_64（模拟器）",
        "CMakeLists.txt：add_library 定义库，target_link_libraries 链接依赖，find_library 查找系统库",
        "abiFilters 配置：在 build.gradle 中指定只编译目标 ABI，减少 APK 体积",
        "预编译 .so 集成：add_prebuilt_library 引入第三方 so，配置 include 头文件路径",
        "编译优化选项：-O2/-O3 优化，-fvisibility=hidden 减少符号导出，strip 去除调试符号",
        "调试 Native 代码：LLDB 断点调试，__android_log_print 日志，addr2line 解析崩溃地址"
    ),
    codeSnippet = """
# CMakeLists.txt
cmake_minimum_required(VERSION 3.22.1)
project("mylib")

# 查找系统库
find_library(log-lib log)   # Android log 库
find_library(android-lib android)

# 编译自己的 so
add_library(
    mylib          # 库名 -> libmylib.so
    SHARED         # 动态库
    src/main/cpp/mylib.cpp
    src/main/cpp/utils.cpp
)

# 链接依赖
target_link_libraries(mylib
    ${'$'}{log-lib}
    ${'$'}{android-lib}
)

# 引入第三方预编译 so（以 OpenSSL 为例）
add_library(ssl SHARED IMPORTED)
set_target_properties(ssl PROPERTIES
    IMPORTED_LOCATION ${'$'}{CMAKE_SOURCE_DIR}/libs/${'$'}{ANDROID_ABI}/libssl.so
)
target_link_libraries(mylib ssl)

# ── build.gradle.kts 配置 ──────────────────
# android {
#     defaultConfig {
#         ndk {
#             abiFilters += listOf("arm64-v8a", "armeabi-v7a")
#         }
#         externalNativeBuild {
#             cmake { cppFlags("-std=c++17") }
#         }
#     }
#     externalNativeBuild {
#         cmake { path("src/main/cpp/CMakeLists.txt") }
#     }
# }
    """.trimIndent(),
    tips = listOf(
        "发布 APK 时只保留 arm64-v8a + armeabi-v7a 两个 ABI，覆盖 99% 设备且体积最小",
        "用 App Bundle 发布时，Google Play 会自动按设备 ABI 分发，不需要 abiFilters 过滤",
        "CMake 编译报错时查看 Build 面板的 ninja 输出，比 Logcat 更详细"
    )
)

@Composable
fun CMakeScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF546E7A),
        stageTitle = "NDK 开发",
        onBack = onBack
    )
}
