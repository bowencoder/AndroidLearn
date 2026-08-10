package com.example.androidlearn.feature.junior.detail.stage1

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.NoteChapter
import com.example.androidlearn.feature.shared.NoteDetailScaffold

/*
 * Android Studio 工具链笔记
 * 官方文档：https://developer.android.com/studio/intro
 *
 * ── 1  项目结构 ───────────────────────────────────────────────────────────────
 *
 *  Android 视图（默认）：
 *  · app/
 *    ├── manifests/AndroidManifest.xml   ← 应用入口、权限、组件声明
 *    ├── java/com.example.xxx/           ← Kotlin/Java 源码
 *    └── res/
 *        ├── drawable/                   ← 图片、矢量图
 *        ├── layout/                     ← XML 布局文件（View 体系）
 *        ├── values/                     ← strings.xml / colors.xml / themes.xml
 *        └── mipmap/                     ← 应用图标（各分辨率）
 *
 *  Project 视图（完整文件树）：
 *  · app/src/main/         ← 主源码集
 *  · app/src/test/         ← 本地单元测试（JVM）
 *  · app/src/androidTest/  ← 设备/模拟器 UI 测试
 *  · app/build.gradle.kts  ← 模块级构建脚本（依赖、compileSdk、minSdk）
 *  · build.gradle.kts      ← 项目级构建脚本（插件版本）
 *  · gradle/libs.versions.toml ← 版本目录（统一管理依赖版本）
 *
 *  // AndroidManifest.xml 关键节点
 *  <manifest package="com.example.app">
 *      <uses-permission android:name="android.permission.INTERNET"/>
 *      <application android:label="@string/app_name" ...>
 *          <activity android:name=".MainActivity"
 *              android:exported="true">
 *              <intent-filter>
 *                  <action android:name="android.intent.action.MAIN"/>
 *                  <category android:name="android.intent.category.LAUNCHER"/>
 *              </intent-filter>
 *          </activity>
 *      </application>
 *  </manifest>
 *
 *
 * ── 2  Logcat ─────────────────────────────────────────────────────────────────
 *
 *  日志级别（从低到高）：
 *  · VERBOSE(V) → DEBUG(D) → INFO(I) → WARN(W) → ERROR(E)
 *  · 生产环境通常只保留 W/E 级别
 *
 *  使用方式：
 *  · android.util.Log.d(TAG, "message")
 *  · android.util.Log.e(TAG, "error", throwable)
 *  · 推荐定义 TAG = "ClassName" 或用 javaClass.simpleName
 *
 *  Logcat 面板技巧：
 *  · package:mine          ← 只看当前应用日志（最常用）
 *  · tag:MyTag             ← 按 Tag 过滤
 *  · level:error           ← 只看 ERROR 级别
 *  · 崩溃时搜索 "FATAL EXCEPTION" 定位堆栈
 *  · 支持正则：tag:My(Activity|Fragment)
 *
 *  // Kotlin 推荐写法
 *  private const val TAG = "MainActivity"
 *  Log.d(TAG, "onCreate called")
 *  Log.e(TAG, "网络请求失败", exception)
 *
 *  // 也可用 Timber（第三方，更优雅）
 *  Timber.d("用户 id=%d 登录", userId)
 *  Timber.e(exception, "请求失败")
 *
 *
 * ── 3  调试器（Debugger）──────────────────────────────────────────────────────
 *
 *  断点类型：
 *  · 行断点（Line Breakpoint）：点击行号左侧，最常用
 *  · 条件断点：右键断点 → Condition，如 i == 5
 *  · 异常断点：Run → View Breakpoints → + → Exception Breakpoints
 *  · 方法断点：在方法签名行打断点，进入/退出时暂停
 *
 *  调试操作（工具栏 / 快捷键）：
 *  · Step Over（F8）：执行当前行，不进入方法内部
 *  · Step Into（F7）：进入方法内部
 *  · Step Out（Shift+F8）：跳出当前方法
 *  · Resume（F9）：继续运行到下一个断点
 *  · Evaluate Expression（Alt+F8）：在断点处执行任意表达式
 *
 *  Variables 面板：
 *  · 查看当前作用域所有变量的值
 *  · 右键变量 → Set Value：动态修改变量值（无需重新运行）
 *  · 右键变量 → Add to Watches：持续监视某个表达式
 *
 *  // 调试技巧
 *  // 1. 在循环中用条件断点，避免每次都暂停
 *  // 2. 用 Evaluate 验证修复方案，不用重新编译
 *  // 3. 崩溃时查看 Frames 面板，点击调用栈定位问题
 *
 *
 * ── 4  模拟器 AVD（Android Virtual Device）────────────────────────────────────
 *
 *  创建模拟器：
 *  · Tools → Device Manager → Create Device
 *  · 选择硬件配置（Pixel 系列推荐）→ 选择系统镜像（API Level）→ 完成
 *
 *  常用模拟功能：
 *  · 网络限速：Extended Controls → Cellular → Network type（模拟弱网）
 *  · 位置模拟：Extended Controls → Location → 输入经纬度或 GPX 路线
 *  · 传感器：Extended Controls → Virtual sensors（加速度、陀螺仪等）
 *  · 截图/录屏：侧边栏相机/录制按钮
 *  · 旋转：Ctrl+← / Ctrl+→
 *
 *  性能建议：
 *  · 开启 HAXM（Intel）或 WHPX（Windows）硬件加速
 *  · 选择 x86_64 镜像（比 ARM 快 10 倍以上）
 *  · 内存分配 ≥ 2GB，存储 ≥ 4GB
 *
 *  // ADB 连接模拟器
 *  adb devices                    // 查看已连接设备
 *  adb -s emulator-5554 shell    // 进入模拟器 shell
 *
 *
 * ── 5  Gradle 构建系统 ────────────────────────────────────────────────────────
 *
 *  核心概念：
 *  · Gradle 是 Android 的构建工具，负责编译、打包、签名、依赖管理
 *  · build.gradle.kts（Kotlin DSL）是主流写法，比 Groovy DSL 有更好的 IDE 支持
 *  · Sync：修改 build.gradle 后必须同步，让 IDE 识别新依赖
 *
 *  模块级 build.gradle.kts 关键配置：
 *  android {
 *      compileSdk = 35          // 编译 SDK 版本
 *      defaultConfig {
 *          minSdk = 24          // 最低支持版本（覆盖 ~97% 设备）
 *          targetSdk = 35       // 目标版本（影响行为兼容性）
 *          versionCode = 1      // 内部版本号（整数，每次发布递增）
 *          versionName = "1.0"  // 用户可见版本号
 *      }
 *      buildTypes {
 *          release {
 *              isMinifyEnabled = true   // 开启 R8 代码压缩混淆
 *              proguardFiles(...)
 *          }
 *      }
 *  }
 *  dependencies {
 *      implementation(libs.androidx.core.ktx)
 *      testImplementation(libs.junit)
 *  }
 *
 *  Build Variants：
 *  · debug：开发调试，包含调试信息，不混淆
 *  · release：发布版本，混淆压缩，需签名
 *  · 可自定义 flavor（如 free/paid、dev/prod）
 *
 *  常用 Gradle 任务：
 *  · assembleDebug：构建 debug APK
 *  · assembleRelease：构建 release APK
 *  · clean：清理构建产物
 *  · test：运行单元测试
 *
 *
 * ── 6  Layout Inspector ───────────────────────────────────────────────────────
 *
 *  作用：
 *  · 实时查看运行中应用的 UI 层级结构
 *  · 检查每个 View/Composable 的属性（大小、位置、颜色等）
 *  · 排查布局问题（重叠、裁剪、间距不对等）
 *
 *  使用方式：
 *  · Tools → Layout Inspector（或 View → Tool Windows → Layout Inspector）
 *  · 连接设备/模拟器，选择进程，点击 Refresh
 *  · 3D 模式：拖动旋转查看层级深度（发现过度绘制）
 *
 *  Compose 支持：
 *  · 可查看 Composable 树结构
 *  · 显示 Modifier 链、重组次数（Recomposition Count）
 *  · 帮助定位不必要的重组
 *
 *
 * ── 7  APK 分析器（APK Analyzer）─────────────────────────────────────────────
 *
 *  作用：
 *  · 分析 APK 文件大小构成，找出体积优化点
 *  · 查看 DEX 文件中的类/方法数量（65535 方法数限制）
 *  · 对比两个 APK 的差异
 *
 *  使用方式：
 *  · Build → Analyze APK → 选择 APK 文件
 *  · 或直接将 APK 拖入 Android Studio
 *
 *  关键指标：
 *  · Raw File Size：原始大小
 *  · Download Size：压缩后下载大小（更接近用户实际下载量）
 *  · classes.dex：代码体积（可展开查看每个包/类的大小）
 *  · res/：资源体积（图片通常是大头）
 *
 *
 * ── 8  常用快捷键 ─────────────────────────────────────────────────────────────
 *
 *  导航：
 *  · Cmd+O（Mac）/ Ctrl+N（Win）：按类名搜索
 *  · Cmd+Shift+O / Ctrl+Shift+N：按文件名搜索
 *  · Cmd+Shift+F / Ctrl+Shift+F：全局内容搜索
 *  · Cmd+E / Ctrl+E：最近打开的文件
 *  · Cmd+B / Ctrl+B：跳转到定义
 *  · Cmd+Alt+B / Ctrl+Alt+B：跳转到实现
 *  · Cmd+U / Ctrl+U：跳转到父类/接口
 *
 *  编辑：
 *  · Cmd+D / Ctrl+D：复制当前行
 *  · Cmd+Delete / Ctrl+Y：删除当前行
 *  · Alt+Enter：快速修复（Fix）/ 导入类
 *  · Cmd+Alt+L / Ctrl+Alt+L：格式化代码
 *  · Cmd+Alt+O / Ctrl+Alt+O：优化 import（删除未使用）
 *  · Shift+F6：重命名（Rename Refactor）
 *  · Cmd+Alt+M / Ctrl+Alt+M：提取方法（Extract Method）
 *
 *  运行/调试：
 *  · Ctrl+R（Mac）/ Shift+F10（Win）：运行
 *  · Ctrl+D（Mac）/ Shift+F9（Win）：调试运行
 *  · Cmd+F2 / Ctrl+F2：停止运行
 *
 *
 * ── 9  ADB 常用命令 ───────────────────────────────────────────────────────────
 *
 *  设备管理：
 *  adb devices                              // 列出已连接设备
 *  adb connect 192.168.1.100:5555          // 无线连接（同一局域网）
 *  adb -s <device_id> <command>            // 指定设备执行命令
 *
 *  应用管理：
 *  adb install app-debug.apk               // 安装 APK
 *  adb install -r app-debug.apk            // 覆盖安装（保留数据）
 *  adb uninstall com.example.app           // 卸载应用
 *  adb shell am start -n com.example/.MainActivity  // 启动 Activity
 *  adb shell am force-stop com.example.app // 强制停止应用
 *
 *  文件操作：
 *  adb push local_file /sdcard/            // 推送文件到设备
 *  adb pull /sdcard/file local_path        // 从设备拉取文件
 *
 *  调试：
 *  adb logcat -s MyTag                     // 过滤 Tag 日志
 *  adb logcat *:E                          // 只看 ERROR 级别
 *  adb shell dumpsys activity              // 查看 Activity 栈
 *  adb shell dumpsys meminfo com.example  // 查看内存使用
 *  adb shell input tap 500 800            // 模拟点击坐标
 *  adb shell input text "hello"           // 输入文字
 *
 *
 * ── 10  常见问题与解决 ────────────────────────────────────────────────────────
 *
 *  Gradle Sync 失败：
 *  · File → Invalidate Caches → Invalidate and Restart（最万能）
 *  · 检查网络，配置国内镜像（阿里云 / 腾讯云 Maven）
 *  · 删除 .gradle 缓存目录：~/.gradle/caches/
 *
 *  模拟器启动慢/卡：
 *  · 确认 HAXM/WHPX 已安装并启用
 *  · 使用 x86_64 镜像而非 ARM
 *  · 减少模拟器 RAM 分配（1.5GB 通常够用）
 *
 *  Build 报错 "Duplicate class"：
 *  · 通常是依赖版本冲突，用 ./gradlew dependencies 查看依赖树
 *  · 在 build.gradle 中用 exclude 排除重复依赖
 *
 *  "65535 method limit" 超出：
 *  · 开启 multiDexEnabled = true
 *  · 或用 R8 混淆裁剪掉未使用的代码
 *
 *  // 配置国内 Maven 镜像（settings.gradle.kts）
 *  dependencyResolutionManagement {
 *      repositories {
 *          maven { url = uri("https://maven.aliyun.com/repository/public") }
 *          google()
 *          mavenCentral()
 *      }
 *  }
 */

private val Green = Color(0xFF4CAF50)

private val chapters = listOf(
    NoteChapter("1",  "项目结构"),
    NoteChapter("2",  "Logcat 日志"),
    NoteChapter("3",  "调试器（Debugger）"),
    NoteChapter("4",  "模拟器 AVD"),
    NoteChapter("5",  "Gradle 构建系统"),
    NoteChapter("6",  "Layout Inspector"),
    NoteChapter("7",  "APK 分析器"),
    NoteChapter("8",  "常用快捷键"),
    NoteChapter("9",  "ADB 常用命令"),
    NoteChapter("10", "常见问题与解决"),
)

@Composable
fun AndroidStudioScreen(
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    NoteDetailScaffold(
        title = "Android Studio 工具链",
        subtitle = "项目结构 · 调试 · Gradle · ADB · 快捷键",
        color = Green,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
