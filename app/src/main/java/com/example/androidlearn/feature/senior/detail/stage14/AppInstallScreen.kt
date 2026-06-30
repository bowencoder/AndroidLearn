package com.example.androidlearn.feature.senior.detail.stage14

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【应用安装过程】专属学习页
//  stageIndex=13, topicIndex=2
//  阶段颜色：深蓝紫 0xFF3F51B5（系统核心原理）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "应用安装过程",
    description = "PMS 解析 APK、dex2oat 预编译、数据目录创建与多用户安装机制",
    overview = "Android 应用安装由 PackageManagerService（PMS）主导，涉及 APK 验证、签名校验、文件拷贝、DEX 优化（dex2oat）、数据目录创建、系统广播通知等多个环节。理解安装流程有助于解决安装失败、静默安装权限和多渠道包管理等问题。",
    keyPoints = listOf(
        "安装入口：PackageInstaller（用户触发）或 adb install（通过 adbd → installd）",
        "PMS 职责：解析 AndroidManifest.xml、校验签名、分配 UID、注册 Package 信息到数据库",
        "dex2oat：将 DEX 字节码 AOT 编译为本地机器码（.oat/.art 文件），存放在 /data/dalvik-cache/",
        "文件布局：APK 本体 → /data/app/包名/；用户数据 → /data/data/包名/（shared_prefs、databases、files、cache）",
        "多用户安装：Android 支持多用户，/data/user/0/包名 是 user 0 的数据目录（0 号用户为主用户）",
        "广播通知：安装完成后 PMS 发送 ACTION_PACKAGE_ADDED 广播，Launcher 监听此广播刷新图标"
    ),
    codeSnippet = """
// 安装流程简化链路
// 用户点击安装 → PackageInstaller.Session.commit()
//   → PMS.installPackage()
//     → 1. 解析 APK（parsePackage）：读取 Manifest、权限声明、组件列表
//     → 2. 签名校验（verifySignatures）：与已安装版本对比，升级时必须相同
//     → 3. 拷贝 APK → /data/app/包名-随机后缀/
//     → 4. 调用 installd（Native 守护进程）执行：
//          - dex2oat 编译 DEX
//          - 创建 /data/data/包名/ 并设置 UID/权限
//     → 5. 更新 PMS 内存数据库 + packages.xml 持久化
//     → 6. 发送 ACTION_PACKAGE_ADDED 广播

// adb 安装命令
// $ adb install -r app-release.apk          // -r 允许覆盖安装
// $ adb install -t app-debug.apk            // -t 允许测试 APK
// $ adb shell pm install-existing 包名      // 为当前用户安装已存在的包

// 查看包信息
// $ adb shell dumpsys package com.example.app
// $ adb shell pm path com.example.app       // 查看 APK 路径

// 静默安装（需要 INSTALL_PACKAGES 权限，系统 App 专用）
val intent = Intent(Intent.ACTION_INSTALL_PACKAGE).apply {
    data = Uri.fromFile(apkFile)
    putExtra(Intent.EXTRA_RETURN_RESULT, true)
    putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
}
// 或使用 PackageInstaller API（Android 6+）
val installer = packageManager.packageInstaller
val params = PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
val sessionId = installer.createSession(params)
    """.trimIndent(),
    tips = listOf(
        "dex2oat 是安装慢的主要原因。Android 7+ 采用混合编译，安装时只做部分 AOT，剩余在运行时 JIT 编译",
        "INSTALL_FAILED_UPDATE_INCOMPATIBLE 通常是签名不一致导致，需先卸载再安装",
        "/data/app 和 /data/data 在不同分区，Factory Reset 会清除 /data 但保留 /system（预装 App）"
    )
)

@Composable
fun AppInstallScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF3F51B5),
        stageTitle = "Android 系统核心原理",
        onBack = onBack
    )
}
