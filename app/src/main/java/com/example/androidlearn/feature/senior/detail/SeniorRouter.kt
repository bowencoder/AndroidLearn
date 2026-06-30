package com.example.androidlearn.feature.senior.detail

import androidx.compose.runtime.Composable
import com.example.androidlearn.feature.senior.detail.stage10.AqsScreen
import com.example.androidlearn.feature.senior.detail.stage10.CasScreen
import com.example.androidlearn.feature.senior.detail.stage10.JmmScreen
import com.example.androidlearn.feature.senior.detail.stage10.SynchronizedScreen
import com.example.androidlearn.feature.senior.detail.stage10.ThreadPoolScreen
import com.example.androidlearn.feature.senior.detail.stage11.GcAlgorithmScreen
import com.example.androidlearn.feature.senior.detail.stage11.OomAnalysisScreen
import com.example.androidlearn.feature.senior.detail.stage12.ComponentizationScreen
import com.example.androidlearn.feature.senior.detail.stage12.HotfixScreen
import com.example.androidlearn.feature.senior.detail.stage12.MultiChannelScreen
import com.example.androidlearn.feature.senior.detail.stage12.PluginizationScreen
import com.example.androidlearn.feature.senior.detail.stage13.CMakeScreen
import com.example.androidlearn.feature.senior.detail.stage13.JniScreen
import com.example.androidlearn.feature.senior.detail.stage14.ApkBuildScreen
import com.example.androidlearn.feature.senior.detail.stage14.AppInstallScreen
import com.example.androidlearn.feature.senior.detail.stage14.ResourceMgrScreen
import com.example.androidlearn.feature.senior.detail.stage14.SkinChangeScreen
import com.example.androidlearn.feature.senior.detail.stage14.SystemArchScreen
import com.example.androidlearn.feature.senior.detail.stage14.WindowMechanismScreen
import com.example.androidlearn.feature.senior.detail.stage16.AgpTransformScreen
import com.example.androidlearn.feature.senior.detail.stage16.ProguardScreen
import com.example.androidlearn.feature.senior.detail.stage16.SerializationScreen
import com.example.androidlearn.feature.senior.detail.stage16.SignatureScreen
import com.example.androidlearn.feature.senior.detail.stage16.VersionCompatScreen
import com.example.androidlearn.feature.senior.detail.stage5.AdvancedModularizationScreen
import com.example.androidlearn.feature.senior.detail.stage5.AnrScreen
import com.example.androidlearn.feature.senior.detail.stage5.AppStartupScreen
import com.example.androidlearn.feature.senior.detail.stage5.PackageSizeScreen
import com.example.androidlearn.feature.senior.detail.stage5.RenderOptimizationScreen
import com.example.androidlearn.feature.senior.detail.stage5.SecurityScreen
import com.example.androidlearn.feature.senior.detail.stage5.StabilityMonitorScreen
import com.example.androidlearn.feature.senior.detail.stage6.AndroidInternalsScreen
import com.example.androidlearn.feature.senior.detail.stage6.CiCdAdvancedScreen
import com.example.androidlearn.feature.senior.detail.stage6.CleanArchScreen
import com.example.androidlearn.feature.senior.detail.stage6.GradlePluginScreen
import com.example.androidlearn.feature.senior.detail.stage6.KmmScreen
import com.example.androidlearn.feature.senior.detail.stage6.TechDesignScreen
import com.example.androidlearn.feature.senior.detail.stage7.ActivityStartupScreen
import com.example.androidlearn.feature.senior.detail.stage7.ArtGcScreen
import com.example.androidlearn.feature.senior.detail.stage7.BinderScreen
import com.example.androidlearn.feature.senior.detail.stage7.ClassLoaderScreen
import com.example.androidlearn.feature.senior.detail.stage7.ThreadSyncScreen
import com.example.androidlearn.feature.senior.detail.stage7.ViewDrawScreen

/**
 * 高级工程师 Tab 路由（stageIndex 7, 8, 9, 10, 11）
 *
 *  Stage 7（Android 底层原理）:
 *    0=Binder  1=ViewDraw  2=ActivityStartup
 *    3=ClassLoader  4=ArtGc  5=AndroidInternals(系统架构与启动)
 *
 *  Stage 8（并发深度与虚拟机）:
 *    0=Jmm  1=Synchronized  2=Cas/Aqs
 *    3=ThreadPool  4=GcAlgorithm  5=OomAnalysis
 *
 *  Stage 9（性能优化体系）:
 *    0=AppStartup  1=RenderOptimization  2=Anr
 *    3=PackageSize  4=StabilityMonitor  5=Security
 *
 *  Stage 10（工程化与架构设计）:
 *    0=CleanArch  1=Pluginization(组件化/插件化)  2=GradlePlugin(AGP)
 *    3=TechDesign(签名混淆)  4=CiCdAdvanced  5=Kmm
 *
 *  Stage 11（系统原理与高阶扩展）:
 *    0=ApkBuild(APK编译/安装)  1=WindowMechanism(Window/资源)
 *    2=Hotfix(热修复)  3=Jni(NDK/JNI)
 *    4=AgpTransform(AOP)  5=VersionCompat(版本适配)
 */
@Composable
fun SeniorRouter(stageIndex: Int, topicIndex: Int, onBack: () -> Unit) {
    when (stageIndex) {
        // ── Stage 7 : Android 底层原理 ────────────────────────
        7 -> when (topicIndex) {
            0 -> BinderScreen(onBack)
            1 -> ViewDrawScreen(onBack)
            2 -> ActivityStartupScreen(onBack)
            3 -> ClassLoaderScreen(onBack)
            4 -> ArtGcScreen(onBack)
            5 -> AndroidInternalsScreen(onBack)
        }
        // ── Stage 8 : 并发深度与虚拟机 ───────────────────────
        8 -> when (topicIndex) {
            0 -> JmmScreen(onBack)
            1 -> SynchronizedScreen(onBack)
            2 -> CasScreen(onBack)
            3 -> ThreadPoolScreen(onBack)
            4 -> GcAlgorithmScreen(onBack)
            5 -> OomAnalysisScreen(onBack)
        }
        // ── Stage 9 : 性能优化体系 ────────────────────────────
        9 -> when (topicIndex) {
            0 -> AppStartupScreen(onBack)
            1 -> RenderOptimizationScreen(onBack)
            2 -> AnrScreen(onBack)
            3 -> PackageSizeScreen(onBack)
            4 -> StabilityMonitorScreen(onBack)
            5 -> SecurityScreen(onBack)
        }
        // ── Stage 10 : 工程化与架构设计 ──────────────────────
        10 -> when (topicIndex) {
            0 -> CleanArchScreen(onBack)
            1 -> PluginizationScreen(onBack)
            2 -> GradlePluginScreen(onBack)
            3 -> TechDesignScreen(onBack)
            4 -> CiCdAdvancedScreen(onBack)
            5 -> KmmScreen(onBack)
        }
        // ── Stage 11 : 系统原理与高阶扩展 ────────────────────
        11 -> when (topicIndex) {
            0 -> ApkBuildScreen(onBack)
            1 -> WindowMechanismScreen(onBack)
            2 -> HotfixScreen(onBack)
            3 -> JniScreen(onBack)
            4 -> AgpTransformScreen(onBack)
            5 -> VersionCompatScreen(onBack)
        }
    }
}
