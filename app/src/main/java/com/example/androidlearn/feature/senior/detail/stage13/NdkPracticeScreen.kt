package com.example.androidlearn.feature.senior.detail.stage13

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "NDK 实战应用",
    description = "音视频处理，图像算法，加解密，跨平台逻辑复用",
    overview = "NDK 的主要应用场景是对性能要求极高或需要跨平台复用的逻辑。音视频编解码、图像处理算法、加解密运算和游戏引擎是最典型的 NDK 使用场景。",
    keyPoints = listOf(
        "音视频处理：FFmpeg 集成（解封装、解码、转码），MediaCodec 硬编解码，OpenSL ES 音频",
        "图像算法：OpenCV NDK 集成，图像滤镜、人脸检测、矩阵运算，NEON SIMD 指令加速",
        "加解密：OpenSSL/BoringSSL NDK 集成，AES/RSA 加解密，HMAC 签名，安全存储密钥",
        "跨平台逻辑：将业务逻辑用 C++ 实现，同时供 Android（JNI）和 iOS（Objective-C++ 桥接）调用",
        "性能调优：NEON 向量化指令，多线程 pthread，内存对齐，避免 JNI 频繁切换开销",
        "崩溃分析：读取 tombstone 文件，用 ndk-stack 解析 Native 崩溃堆栈，addr2line 定位行号"
    ),
    codeSnippet = """
// 示例1：使用 NDK 的 Bitmap API 处理图像（grayscale 灰度化）
#include <android/bitmap.h>

JNIEXPORT void JNICALL
Java_com_example_ImageProcessor_toGrayscale(JNIEnv* env, jobject, jobject bitmap) {
    AndroidBitmapInfo info;
    void* pixels;
    AndroidBitmap_getInfo(env, bitmap, &info);
    AndroidBitmap_lockPixels(env, bitmap, &pixels);

    uint32_t* line = (uint32_t*) pixels;
    for (uint32_t y = 0; y < info.height; y++) {
        for (uint32_t x = 0; x < info.width; x++) {
            uint32_t pixel = line[x];
            uint8_t r = (pixel >> 16) & 0xFF;
            uint8_t g = (pixel >> 8)  & 0xFF;
            uint8_t b =  pixel        & 0xFF;
            uint8_t gray = (uint8_t)(0.299f * r + 0.587f * g + 0.114f * b);
            line[x] = (0xFF << 24) | (gray << 16) | (gray << 8) | gray;
        }
        line = (uint32_t*)((char*)line + info.stride);
    }
    AndroidBitmap_unlockPixels(env, bitmap);
}

// 示例2：崩溃堆栈解析
// $ adb pull /data/tombstones/tombstone_00 .
// $ ndk-stack -sym app/build/intermediates/cmake/debug/obj/arm64-v8a \
//             -dump tombstone_00
// 输出：精确到 cpp 文件和行号的崩溃位置
    """.trimIndent(),
    tips = listOf(
        "FFmpeg 集成推荐使用 mobile-ffmpeg 或 ffmpeg-kit，已封装好各平台编译脚本",
        "JNI 调用开销约 10-100ns，高频调用（如每帧处理）需要减少 JNI 跨越次数，批量传数据",
        "Android Profiler 的 CPU Profiler 支持分析 Native 代码，可直接看 C++ 函数耗时"
    )
)

@Composable
fun NdkPracticeScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF546E7A),
        stageTitle = "NDK 开发",
        onBack = onBack
    )
}
