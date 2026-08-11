package com.example.androidlearn.feature.senior.detail.stage15

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val animationData = NoteData(
    title = "动画进阶",
    subtitle = "属性动画原理、插值器/估值器、Lottie 矢量动画与 Compose 动画 API",
    color = Color.parseColor("#FF5722"),
    chapters = listOf(
        ChapterItem("1",   "属性动画原理"),
        ChapterItem("1.1", "ValueAnimator 按时间进度（0~1）→ 插值器（非线性映射）→ 估值器（计算属性值）→ 监听器更新 UI"),
        ChapterItem("2",   "插值器（Interpolator）"),
        ChapterItem("2.1", "控制动画节奏，如 AccelerateDecelerateInterpolator（先加速后减速）、OvershootInterpolator（回弹）"),
        ChapterItem("3",   "估值器（TypeEvaluator）"),
        ChapterItem("3.1", "将 0~1 的 fraction 映射为实际属性值，默认 IntEvaluator/FloatEvaluator，可自定义"),
        ChapterItem("4",   "ObjectAnimator"),
        ChapterItem("4.1", "针对对象的具体属性（translationX、alpha、scaleX 等）的动画，必须有对应的 getter/setter"),
        ChapterItem("5",   "AnimatorSet"),
        ChapterItem("5.1", "组合多个动画，支持顺序（playSequentially）、同时（playTogether）和依赖（after/before/with）"),
        ChapterItem("6",   "Compose 动画"),
        ChapterItem("6.1", "animate*AsState（状态驱动）、AnimatedVisibility（显隐动画）、Transition（多属性同步动画）"),
    )
)
