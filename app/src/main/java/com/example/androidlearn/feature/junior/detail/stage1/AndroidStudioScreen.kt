package com.example.androidlearn.feature.junior.detail.stage1

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.NoteChapter
import com.example.androidlearn.feature.shared.NoteDetailScaffold

/*
 * Android Studio 工具链笔记
 * 当前最新稳定版：Android Studio Quail 3（2026.1.3）
 * 官方文档：https://developer.android.com/studio/intro
 *
 * ── 1  主界面功能区域 ─────────────────────────────────────────────────────────
 *
 *  ┌──────────────────────────────────────────────────────────────────────────┐
 *  │  顶部工具栏（Toolbar）：运行配置 / Run / Debug / Apply Changes / Sync     │
 *  ├───────────┬──────────────────────────────────────┬──────────────────────┤
 *  │           │                                      │                      │
 *  │  Project  │         Editor（编辑区）              │  右侧工具栏           │
 *  │  面板     │  · 代码编辑                           │  · Gradle            │
 *  │  （左侧） │  · 多标签 / 分屏                      │  · Build Variants    │
 *  │           │  · 行号 / 断点 / Gutter 图标          │  · Structure         │
 *  │           │                                      │  · Device Manager    │
 *  ├───────────┴──────────────────────────────────────┴──────────────────────┤
 *  │  底部工具栏：Logcat / Running Devices / Terminal / Build / Debug / Git   │
 *  └──────────────────────────────────────────────────────────────────────────┘
 *
 *  ① Project 面板（左侧）
 *  · Android 视图（默认）：按逻辑分组（manifests / kotlin+java / res）
 *    注意：新版已将 "java" 改为 "kotlin+java"
 *  · Project 视图：完整文件树，看真实磁盘目录结构
 *  · 切换方式：面板顶部下拉菜单（Android / Project / Packages 等）
 *  · 快捷键：Alt+1（Mac: Cmd+1）打开/关闭 Project 面板
 *
 *  ② Editor（中央编辑区）
 *  · 多标签页，支持分屏（右键 Tab → Split Right / Split Down）
 *  · 左侧行号区：点击打断点，右键设置条件断点
 *  · 顶部面包屑（Navigation Bar）：显示当前类/方法层级，可快速跳转
 *  · Gutter 图标：行号旁绿色箭头可直接运行/调试测试方法
 *  · Inline Hints：变量类型、参数名、返回值等内联提示（可在 Settings 开关）
 *  · Live Edit（Quail 新特性）：Compose 代码修改后实时推送到模拟器，无需重新运行
 *
 *  ③ 右侧工具栏（竖向图标，点击展开）
 *  · Gradle：查看/运行 Gradle 任务树
 *  · Build Variants：切换 debug / release / 自定义 flavor
 *  · Structure：当前文件的类/方法/属性大纲，快速跳转
 *  · Device Manager：管理模拟器（AVD）和已连接真机
 *  · Version Control（Git）：提交、分支、历史记录
 *
 *  ④ 底部工具栏（Tool Windows）
 *  · Logcat：实时设备日志，支持过滤表达式（package:mine / tag:xxx / level:error）
 *  · Running Devices（新版）：内嵌模拟器窗口，无需单独开启模拟器窗口
 *    同时集成 Layout Inspector 入口（Toggle Layout Inspector 按钮）
 *  · Terminal：内置终端，可执行 adb / git / gradlew 命令
 *  · Build：构建输出，报错时看这里（含 Warnings / Errors 分类）
 *  · Run：运行输出（System.out.println 输出在这里）
 *  · Debug：调试面板（Variables / Frames / Watches / Console）
 *  · Problems：Lint 警告和错误汇总，双击跳转到问题代码
 *  · Git：版本控制操作（Commit / Push / Pull / Log）
 *
 *  ⑤ 顶部工具栏
 *  · 运行配置下拉：选择运行的 module 和目标设备
 *  · ▶ Run（Ctrl+R / Shift+F10）：编译并运行
 *  · 🐛 Debug（Ctrl+D / Shift+F9）：调试运行
 *  · ⚡ Apply Changes：热更新代码（不重启 App，仅推送变更的类）
 *  · ⚡⚡ Apply Changes and Restart Activity：重启当前 Activity
 *  · 🔨 Build Project（Cmd+F9 / Ctrl+F9）
 *  · 🔄 Sync Project with Gradle Files：修改 build.gradle 后必须同步
 *  · AVD Manager / SDK Manager 快捷入口（Tools 菜单也可找到）
 *  · 账号图标：登录 Google 账号，解锁 Gemini AI 功能
 *
 *  ⑥ Gemini in Android Studio（AI 功能，Quail 新增）
 *  · 入口：右侧工具栏 Gemini 图标，或 Tools → Gemini
 *  · 功能：代码解释 / 生成单元测试 / 重构建议 / 错误修复建议
 *  · 需登录 Google 账号并在 Settings → Tools → Gemini 中启用
 *
 *
 * ── 2  项目结构 ───────────────────────────────────────────────────────────────
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
 *  // AndroidManifest.xml 关键节点详解
 *
 *  ① <manifest> 根节点
 *     · package：应用唯一标识（反向域名，如 com.example.app）
 *     · xmlns:android：命名空间声明，固定写法
 *
 *  ② <uses-permission> 权限声明
 *     · 普通权限（安装时自动授予）：INTERNET、VIBRATE、RECEIVE_BOOT_COMPLETED
 *     · 危险权限（运行时需用户授权）：CAMERA、READ_CONTACTS、ACCESS_FINE_LOCATION
 *     · 特殊权限：SYSTEM_ALERT_WINDOW、WRITE_SETTINGS（需跳转系统设置页）
 *     · android:maxSdkVersion：限制权限仅在指定 API 以下生效
 *
 *  ③ <application> 应用节点
 *     · android:name：自定义 Application 类（如 ".MyApp"）
 *     · android:label：应用名称（引用 @string/app_name）
 *     · android:icon：应用图标（引用 @mipmap/ic_launcher）
 *     · android:theme：全局主题（引用 @style/Theme.App）
 *     · android:allowBackup：是否允许 ADB 备份数据（生产建议 false）
 *     · android:usesCleartextTraffic：是否允许 HTTP 明文流量（Android 9+ 默认禁止）
 *     · android:networkSecurityConfig：自定义网络安全配置（证书固定等）
 *
 *  ④ <activity> 活动节点
 *     · android:name：Activity 类名（"." 开头表示相对包名）
 *     · android:exported：是否可被其他应用启动（Android 12+ 有 intent-filter 时必须显式声明）
 *     · android:launchMode：启动模式（standard / singleTop / singleTask / singleInstance）
 *     · android:screenOrientation：屏幕方向（portrait / landscape / unspecified）
 *     · android:configChanges：声明后配置变更不重建 Activity（如 orientation|keyboardHidden）
 *     · android:windowSoftInputMode：软键盘弹出行为（adjustResize / adjustPan）
 *     · android:hardwareAccelerated：是否开启硬件加速（默认 true）
 *
 *  ⑤ <intent-filter> 意图过滤器
 *     · <action android:name="android.intent.action.MAIN"/>：标记为应用入口 Activity
 *     · <category android:name="android.intent.category.LAUNCHER"/>：显示在桌面启动器
 *     · 隐式 Intent 匹配：action + category + data 三者同时满足才能匹配
 *     · 自定义 action：<action android:name="com.example.app.OPEN_DETAIL"/>
 *
 *  ⑥ 其他常用节点
 *     · <service>：声明 Service（android:exported / android:foregroundServiceType）
 *     · <receiver>：声明 BroadcastReceiver（静态注册，Android 8+ 大部分隐式广播不再触发）
 *     · <provider>：声明 ContentProvider（android:authorities 唯一标识）
 *     · <uses-feature>：声明硬件特性需求（如 android.hardware.camera）
 *     · <meta-data>：键值对元数据（常用于 SDK 初始化 API Key）
 *
 *  // 完整示例
 *  <manifest xmlns:android="http://schemas.android.com/apk/res/android"
 *      package="com.example.app">
 *
 *      <!-- 权限声明 -->
 *      <uses-permission android:name="android.permission.INTERNET"/>
 *      <uses-permission android:name="android.permission.CAMERA"/>
 *
 *      <!-- 硬件特性（可选，不强制要求） -->
 *      <uses-feature android:name="android.hardware.camera" android:required="false"/>
 *
 *      <application
 *          android:name=".MyApplication"
 *          android:label="@string/app_name"
 *          android:icon="@mipmap/ic_launcher"
 *          android:theme="@style/Theme.App"
 *          android:allowBackup="false"
 *          android:usesCleartextTraffic="false">
 *
 *          <!-- 主 Activity（应用入口） -->
 *          <activity
 *              android:name=".MainActivity"
 *              android:exported="true"
 *              android:launchMode="singleTask"
 *              android:windowSoftInputMode="adjustResize">
 *              <intent-filter>
 *                  <action android:name="android.intent.action.MAIN"/>
 *                  <category android:name="android.intent.category.LAUNCHER"/>
 *              </intent-filter>
 *              <!-- Deep Link 支持 -->
 *              <intent-filter android:autoVerify="true">
 *                  <action android:name="android.intent.action.VIEW"/>
 *                  <category android:name="android.intent.category.DEFAULT"/>
 *                  <category android:name="android.intent.category.BROWSABLE"/>
 *                  <data android:scheme="https" android:host="example.com"/>
 *              </intent-filter>
 *          </activity>
 *
 *          <!-- 前台 Service -->
 *          <service
 *              android:name=".UploadService"
 *              android:exported="false"
 *              android:foregroundServiceType="dataSync"/>
 *
 *          <!-- FileProvider（共享文件给其他应用） -->
 *          <provider
 *              android:name="androidx.core.content.FileProvider"
 *              android:authorities="${applicationId}.fileprovider"
 *              android:exported="false"
 *              android:grantUriPermissions="true">
 *              <meta-data
 *                  android:name="android.support.FILE_PROVIDER_PATHS"
 *                  android:resource="@xml/file_paths"/>
 *          </provider>
 *
 *          <!-- SDK 初始化 API Key -->
 *          <meta-data
 *              android:name="com.google.android.geo.API_KEY"
 *              android:value="@string/maps_api_key"/>
 *
 *      </application>
 *  </manifest>
 *
 *
 * ── 3  Logcat ─────────────────────────────────────────────────────────────────
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
 * ── 4  调试器（Debugger）──────────────────────────────────────────────────────
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
 * ── 5  模拟器 AVD（Android Virtual Device）────────────────────────────────────
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
 * ── 9  工程的兼容性问题（新手常见）──────────────────────────────────────
 *
 * ── 9.1  Gradle / AGP 版本不匹配 ─────────────────────────────────────────────
 *
 *  · 现象：Sync 失败，报 "Gradle version X.X is required"
 *  · 原因：工程要求的 Gradle / AGP 版本与当前 Android Studio 不兼容
 *  · 解决：点击 Sync 失败提示中的 "Upgrade" 按钮，或手动修改：
 *    - gradle/wrapper/gradle-wrapper.properties → distributionUrl 改为新版本
 *    - gradle/libs.versions.toml → agp = "8.x.x"
 *  · 版本对照：AGP 8.x ↔ Gradle 8.x ↔ Android Studio Koala+
 *    完整对照表：https://developer.android.com/build/releases/gradle-plugin
 *
 *
 * ── 9.2  SDK 未安装（compileSdk 缺失）────────────────────────────────────────
 *
 *  · 现象：报 "Failed to find target with hash string 'android-XX'"
 *  · 原因：工程要求的 API Level 在本机 SDK Manager 中未安装
 *  · 解决：Tools → SDK Manager → 勾选对应 API Level → Apply
 *
 *  // build.gradle.kts 中的版本声明
 *  android {
 *      compileSdk = 35      // 需要安装 API 35
 *      defaultConfig { minSdk = 24; targetSdk = 35 }
 *  }
 *
 *
 * ── 9.3  依赖下载失败（国内网络）─────────────────────────────────────────────
 *
 *  · 现象：Sync 卡住或报 "Could not resolve xxx:xxx:xxx" / "Read timed out"
 *  · 原因：Google / Maven Central 仓库在国内访问受限
 *  · 解决：
 *    ① 开启 VPN，或在 Android Studio 中配置 HTTP 代理：
 *       File → Settings → Appearance & Behavior → System Settings → HTTP Proxy
 *    ② 配置阿里云镜像（settings.gradle.kts）：
 *       dependencyResolutionManagement {
 *           repositories {
 *               maven { url = uri("https://maven.aliyun.com/repository/google") }
 *               maven { url = uri("https://maven.aliyun.com/repository/central") }
 *               google(); mavenCentral()
 *           }
 *       }
 *
 *
 * ── 9.4  JDK 版本不匹配 ───────────────────────────────────────────────────────
 *
 *  · 现象：报 "Unsupported class file major version XX"
 *  · 原因：Gradle 使用的 JDK 版本与工程要求不一致
 *  · 解决：File → Settings → Build → Gradle → Gradle JDK
 *          选择 "Embedded JDK"（Android Studio 内置，最省事）
 *  · 规则：AGP 8.0+ 要求 JDK 17；AGP 7.x 要求 JDK 11
 */

private val Green = Color(0xFF4CAF50)

private val chapters = listOf(
    NoteChapter("1", "主界面功能区域"),
    NoteChapter("2", "项目结构"),
    NoteChapter("3", "Logcat 日志"),
    NoteChapter("4", "调试器（Debugger）"),
    NoteChapter("5", "模拟器 AVD"),
    NoteChapter("6", "Layout Inspector"),
    NoteChapter("7", "APK 分析器"),
    NoteChapter("8", "常用快捷键"),
    NoteChapter("9",   "打开工程的兼容性问题（新手常见）"),
    NoteChapter("9.1", "Gradle / AGP 版本不匹配：点击 Upgrade 或手动改版本号"),
    NoteChapter("9.2", "SDK 未安装：SDK Manager 安装对应 API Level"),
    NoteChapter("9.3", "依赖下载失败：VPN / 代理 / 阿里云镜像"),
    NoteChapter("9.4", "JDK 版本不匹配：切换为 Embedded JDK"),
)

@Composable
fun AndroidStudioScreen(
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    NoteDetailScaffold(
        title = "Android Studio 工具链",
        subtitle = "主界面 · 项目结构 · Logcat · 调试器 · 模拟器 · 快捷键 · 兼容性问题",
        color = Green,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
