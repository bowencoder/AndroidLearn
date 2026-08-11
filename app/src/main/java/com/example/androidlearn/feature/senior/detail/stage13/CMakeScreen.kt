package com.example.androidlearn.feature.senior.detail.stage13

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val cmakeData = NoteData(
    title = "CMake 构建配置",
    subtitle = "CMakeLists 编写，so 库编译，ABI 过滤，第三方库集成",
    color = Color.parseColor("#546E7A"),
    chapters = listOf(
        ChapterItem("1",   "ABI 架构"),
        ChapterItem("1.1", "arm64-v8a（主流 64位）、armeabi-v7a（32位兼容）、x86_64（模拟器）"),
        ChapterItem("2",   "CMakeLists.txt"),
        ChapterItem("2.1", "add_library 定义库，target_link_libraries 链接依赖，find_library 查找系统库"),
        ChapterItem("3",   "abiFilters 配置"),
        ChapterItem("3.1", "在 build.gradle 中指定只编译目标 ABI，减少 APK 体积"),
        ChapterItem("4",   "预编译 .so 集成"),
        ChapterItem("4.1", "add_prebuilt_library 引入第三方 so，配置 include 头文件路径"),
        ChapterItem("5",   "编译优化选项"),
        ChapterItem("5.1", "-O2/-O3 优化，-fvisibility=hidden 减少符号导出，strip 去除调试符号"),
        ChapterItem("6",   "调试 Native 代码"),
        ChapterItem("6.1", "LLDB 断点调试，__android_log_print 日志，addr2line 解析崩溃地址"),
    )
)
