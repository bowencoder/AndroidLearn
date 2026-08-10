package com.example.androidlearn.feature.junior.detail.stage1

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.NoteChapter
import com.example.androidlearn.feature.shared.NoteDetailScaffold

/*
 * Gradle / ADB 工程工具笔记
 *
 * ── 1  Gradle 核心概念 ────────────────────────────────────────────────────────
 *
 *  构建流程：
 *  · Initialization → Configuration → Execution
 *  · Initialization：确定哪些项目参与构建（settings.gradle.kts）
 *  · Configuration：解析所有 build.gradle.kts，构建任务依赖图
 *  · Execution：按依赖顺序执行任务
 *
 *  关键文件：
 *  · settings.gradle.kts：声明项目名称和子模块
 *  · build.gradle.kts（根）：配置所有模块共用的插件版本
 *  · app/build.gradle.kts：模块级配置（compileSdk、依赖等）
 *  · gradle/libs.versions.toml：版本目录，统一管理所有依赖版本
 *  · gradle.properties：全局属性（JVM 参数、特性开关等）
 *
 *  // settings.gradle.kts
 *  rootProject.name = "AndroidLearn"
 *  include(":app")
 *  include(":feature:login")   // 多模块项目
 *
 *  // gradle.properties 常用配置
 *  org.gradle.jvmargs=-Xmx4g -XX:+UseParallelGC   // 增大 Gradle JVM 内存
 *  org.gradle.parallel=true                         // 并行构建
 *  org.gradle.caching=true                          // 开启构建缓存
 *  android.useAndroidX=true
 *
 *
 * ── 2  Gradle 依赖管理 ────────────────────────────────────────────────────────
 *
 *  依赖配置类型：
 *  · implementation：编译+运行时，不暴露给依赖此模块的其他模块（推荐）
 *  · api：编译+运行时，暴露给上层模块（谨慎使用，会增加编译时间）
 *  · testImplementation：只用于单元测试
 *  · androidTestImplementation：只用于 Android 仪器测试
 *  · debugImplementation：只在 debug 构建中包含
 *  · releaseImplementation：只在 release 构建中包含
 *
 *  版本目录（libs.versions.toml）：
 *  [versions]
 *  retrofit = "2.9.0"
 *
 *  [libraries]
 *  retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
 *
 *  // build.gradle.kts 中引用
 *  implementation(libs.retrofit)
 *
 *  BOM（Bill of Materials）：
 *  · 导入 BOM 后，同系列依赖无需单独指定版本
 *  · Compose BOM、Firebase BOM 都是典型例子
 *
 *  implementation(platform(libs.androidx.compose.bom))
 *  implementation(libs.androidx.ui)          // 无需写版本，BOM 统一管理
 *  implementation(libs.androidx.material3)   // 同上
 *
 *  查看依赖树（排查版本冲突）：
 *  ./gradlew :app:dependencies --configuration releaseRuntimeClasspath
 *
 *
 * ── 3  Gradle 常用命令 ────────────────────────────────────────────────────────
 *
 *  构建：
 *  ./gradlew assembleDebug           // 构建 debug APK
 *  ./gradlew assembleRelease         // 构建 release APK
 *  ./gradlew bundleRelease           // 构建 AAB（上传 Play Store）
 *  ./gradlew clean                   // 清理构建产物
 *  ./gradlew clean assembleDebug     // 清理后重新构建
 *
 *  测试：
 *  ./gradlew test                    // 运行所有单元测试
 *  ./gradlew :app:testDebugUnitTest  // 运行指定模块测试
 *  ./gradlew connectedAndroidTest    // 运行设备测试（需连接设备）
 *
 *  分析：
 *  ./gradlew :app:dependencies       // 查看依赖树
 *  ./gradlew tasks                   // 查看所有可用任务
 *  ./gradlew --profile               // 生成构建性能报告
 *
 *  加速技巧：
 *  ./gradlew assembleDebug --parallel          // 并行构建
 *  ./gradlew assembleDebug --build-cache       // 使用构建缓存
 *  ./gradlew assembleDebug --configuration-cache  // 配置缓存（Gradle 7+）
 *
 *
 * ── 4  ADB 设备与应用管理 ─────────────────────────────────────────────────────
 *
 *  设备连接：
 *  adb devices                                   // 列出已连接设备
 *  adb devices -l                                // 详细信息（型号等）
 *  adb connect 192.168.1.100:5555               // 无线连接（同局域网）
 *  adb disconnect                                // 断开无线连接
 *  adb -s <device_id> <command>                 // 多设备时指定设备
 *
 *  应用管理：
 *  adb install app-debug.apk                    // 安装 APK
 *  adb install -r app-debug.apk                 // 覆盖安装（保留数据）
 *  adb install -t app-debug.apk                 // 允许安装测试 APK
 *  adb uninstall com.example.app                // 卸载应用
 *  adb uninstall -k com.example.app             // 卸载但保留数据
 *  adb shell pm list packages                   // 列出所有已安装包名
 *  adb shell pm list packages -3                // 只列出第三方应用
 *  adb shell pm clear com.example.app           // 清除应用数据（等同于"清除数据"）
 *
 *  Activity 控制：
 *  adb shell am start -n com.example/.MainActivity          // 启动 Activity
 *  adb shell am start -a android.intent.action.VIEW \
 *      -d "https://example.com"                             // 打开 URL
 *  adb shell am force-stop com.example.app                  // 强制停止
 *  adb shell am kill com.example.app                        // 温和停止
 *
 *
 * ── 5  ADB 调试与文件操作 ─────────────────────────────────────────────────────
 *
 *  日志：
 *  adb logcat                                   // 查看所有日志
 *  adb logcat -s MyTag                          // 按 Tag 过滤
 *  adb logcat *:E                               // 只看 ERROR 级别
 *  adb logcat --pid=$(adb shell pidof -s com.example.app)  // 只看当前 App
 *  adb logcat -c                                // 清空日志缓冲区
 *  adb logcat > log.txt                         // 保存日志到文件
 *
 *  文件操作：
 *  adb push local_file /sdcard/Download/        // 推送文件到设备
 *  adb pull /sdcard/Download/file.txt ./        // 从设备拉取文件
 *  adb shell ls /sdcard/                        // 列出目录
 *  adb shell cat /proc/meminfo                  // 查看内存信息
 *
 *  系统信息：
 *  adb shell dumpsys activity                   // Activity 栈信息
 *  adb shell dumpsys activity top               // 栈顶 Activity
 *  adb shell dumpsys meminfo com.example.app    // 应用内存详情
 *  adb shell dumpsys battery                    // 电池状态
 *  adb shell getprop ro.build.version.release   // Android 版本
 *  adb shell wm size                            // 屏幕分辨率
 *  adb shell wm density                         // 屏幕密度（dpi）
 *
 *  模拟输入：
 *  adb shell input tap 500 800                  // 模拟点击坐标
 *  adb shell input swipe 100 800 100 200 300    // 模拟滑动（含时长 ms）
 *  adb shell input text "hello"                 // 输入文字
 *  adb shell input keyevent 4                   // 发送按键（4=返回键）
 *  adb shell screencap /sdcard/screen.png       // 截图
 *  adb shell screenrecord /sdcard/demo.mp4      // 录屏（Ctrl+C 停止）
 *
 *
 * ── 6  工程效率技巧 ───────────────────────────────────────────────────────────
 *
 *  Gradle 加速：
 *  · 开启 Configuration Cache（Gradle 8+ 默认开启）
 *  · 使用国内 Maven 镜像（阿里云/腾讯云）加速依赖下载
 *  · 避免在 configuration 阶段执行耗时操作
 *  · 多模块项目开启 --parallel 并行构建
 *
 *  ADB 效率：
 *  · 设置 adb 环境变量，全局可用：export PATH=$PATH:~/Library/Android/sdk/platform-tools
 *  · 无线调试（Android 11+）：开发者选项 → 无线调试，无需 USB
 *  · adb shell 进入交互式 shell，可执行多条命令
 *
 *  // 配置国内 Maven 镜像（settings.gradle.kts）
 *  dependencyResolutionManagement {
 *      repositories {
 *          maven { url = uri("https://maven.aliyun.com/repository/public") }
 *          maven { url = uri("https://maven.aliyun.com/repository/google") }
 *          google()
 *          mavenCentral()
 *      }
 *  }
 */

private val Green = Color(0xFF4CAF50)

private val chapters = listOf(
    NoteChapter("1", "Gradle 核心概念"),
    NoteChapter("2", "Gradle 依赖管理"),
    NoteChapter("3", "Gradle 常用命令"),
    NoteChapter("4", "ADB 设备与应用管理"),
    NoteChapter("5", "ADB 调试与文件操作"),
    NoteChapter("6", "工程效率技巧"),
)

@Composable
fun DevToolsScreen(
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    NoteDetailScaffold(
        title = "Gradle / ADB",
        subtitle = "构建系统 · 调试工具",
        color = Green,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
