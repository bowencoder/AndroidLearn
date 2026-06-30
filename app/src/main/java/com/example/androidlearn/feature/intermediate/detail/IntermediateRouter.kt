package com.example.androidlearn.feature.intermediate.detail

import androidx.compose.runtime.Composable
import com.example.androidlearn.feature.intermediate.detail.stage3.CoroutinesScreen
import com.example.androidlearn.feature.intermediate.detail.stage3.HiltScreen
import com.example.androidlearn.feature.intermediate.detail.stage3.JetpackComposeScreen
import com.example.androidlearn.feature.intermediate.detail.stage3.MvvmArchScreen
import com.example.androidlearn.feature.intermediate.detail.stage3.NavigationComponentScreen
import com.example.androidlearn.feature.intermediate.detail.stage3.ViewModelScreen
import com.example.androidlearn.feature.intermediate.detail.stage4.CiCdScreen
import com.example.androidlearn.feature.intermediate.detail.stage4.CustomViewScreen
import com.example.androidlearn.feature.intermediate.detail.stage4.ModularizationScreen
import com.example.androidlearn.feature.intermediate.detail.stage4.PerformanceScreen
import com.example.androidlearn.feature.intermediate.detail.stage4.UnitTestScreen
import com.example.androidlearn.feature.intermediate.detail.stage4.WorkManagerScreen
import com.example.androidlearn.feature.intermediate.detail.stage5.AidlMultiProcessScreen
import com.example.androidlearn.feature.intermediate.detail.stage5.AppWidgetScreen
import com.example.androidlearn.feature.intermediate.detail.stage5.BluetoothWifiScreen
import com.example.androidlearn.feature.intermediate.detail.stage5.CameraXScreen
import com.example.androidlearn.feature.intermediate.detail.stage5.ExoPlayerScreen
import com.example.androidlearn.feature.intermediate.detail.stage5.ScreenAdaptBitmapScreen
import com.example.androidlearn.feature.intermediate.detail.stage8.GestureConflictScreen
import com.example.androidlearn.feature.intermediate.detail.stage8.LooperHandlerScreen
import com.example.androidlearn.feature.intermediate.detail.stage8.TouchEventScreen
import com.example.androidlearn.feature.intermediate.detail.stage9.AnnotationAptScreen
import com.example.androidlearn.feature.intermediate.detail.stage9.DynamicProxyScreen
import com.example.androidlearn.feature.intermediate.detail.stage9.GenericsScreen
import com.example.androidlearn.feature.intermediate.detail.stage9.ReflectionScreen

/**
 * 中级工程师 Tab 路由（stageIndex 3, 4, 5, 6）
 *
 *  Stage 3（现代架构体系）:
 *    0=MvvmArch  1=ViewModel  2=Coroutines
 *    3=Hilt      4=JetpackCompose  5=NavigationComponent
 *
 *  Stage 4（Compose 进阶与工程能力）:
 *    0=JetpackCompose(进阶)  1=CustomView  2=WorkManager
 *    3=Performance(Paging)   4=UnitTest    5=CiCd(数据层进阶)
 *
 *  Stage 5（多媒体与系统能力）:
 *    0=ExoPlayer  1=CameraX  2=BluetoothWifi
 *    3=AidlMultiProcess  4=AppWidget  5=ScreenAdaptBitmap
 *
 *  Stage 6（事件机制与动态编程）:
 *    0=LooperHandler  1=TouchEvent  2=GestureConflict
 *    3=Generics       4=AnnotationApt  5=Reflection/DynamicProxy
 */
@Composable
fun IntermediateRouter(stageIndex: Int, topicIndex: Int, onBack: () -> Unit) {
    when (stageIndex) {
        // ── Stage 3 : 现代架构体系 ────────────────────────────
        3 -> when (topicIndex) {
            0 -> MvvmArchScreen(onBack)
            1 -> ViewModelScreen(onBack)
            2 -> CoroutinesScreen(onBack)
            3 -> HiltScreen(onBack)
            4 -> JetpackComposeScreen(onBack)
            5 -> NavigationComponentScreen(onBack)
        }
        // ── Stage 4 : Compose 进阶与工程能力 ─────────────────
        4 -> when (topicIndex) {
            0 -> JetpackComposeScreen(onBack)
            1 -> CustomViewScreen(onBack)
            2 -> WorkManagerScreen(onBack)
            3 -> PerformanceScreen(onBack)
            4 -> UnitTestScreen(onBack)
            5 -> ModularizationScreen(onBack)
        }
        // ── Stage 5 : 多媒体与系统能力 ───────────────────────
        5 -> when (topicIndex) {
            0 -> ExoPlayerScreen(onBack)
            1 -> CameraXScreen(onBack)
            2 -> BluetoothWifiScreen(onBack)
            3 -> AidlMultiProcessScreen(onBack)
            4 -> AppWidgetScreen(onBack)
            5 -> ScreenAdaptBitmapScreen(onBack)
        }
        // ── Stage 6 : 事件机制与动态编程 ─────────────────────
        6 -> when (topicIndex) {
            0 -> LooperHandlerScreen(onBack)
            1 -> TouchEventScreen(onBack)
            2 -> GestureConflictScreen(onBack)
            3 -> GenericsScreen(onBack)
            4 -> AnnotationAptScreen(onBack)
            5 -> ReflectionScreen(onBack)
            6 -> DynamicProxyScreen(onBack)
        }
    }
}
