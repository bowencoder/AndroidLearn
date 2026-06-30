package com.example.androidlearn.feature.senior.detail.stage13

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "JNI 调用机制",
    description = "Java 与 Native 互调，数据类型映射，局部/全局引用，异常处理",
    overview = "JNI（Java Native Interface）是 Java/Kotlin 调用 C/C++ 代码的桥梁。掌握 JNI 数据类型映射、引用管理和异常处理，是 NDK 开发最核心的能力。",
    keyPoints = listOf(
        "数据类型映射：jint/jlong/jstring/jbyteArray 等 JNI 类型与 Java 类型的对应关系",
        "函数命名规则：Java_包名_类名_方法名，下划线替换点号（如 Java_com_example_Foo_bar）",
        "局部引用 vs 全局引用：局部引用在 JNI 调用结束后自动释放，全局引用需手动 DeleteGlobalRef",
        "字符串处理：GetStringUTFChars 获取 UTF-8 字符串，使用后必须 ReleaseStringUTFChars",
        "数组操作：GetByteArrayElements / ReleaseByteArrayElements，避免 critical 锁定过长",
        "异常处理：ExceptionCheck / ExceptionClear，Native 层捕获并处理 Java 异常"
    ),
    codeSnippet = """
// Kotlin 声明 native 方法
class NativeLib {
    external fun add(a: Int, b: Int): Int
    external fun processBytes(data: ByteArray): ByteArray

    companion object {
        init { System.loadLibrary("mylib") }
    }
}

// C++ 实现（mylib.cpp）
#include <jni.h>
#include <string>

extern "C" {

// 基础类型互调
JNIEXPORT jint JNICALL
Java_com_example_NativeLib_add(JNIEnv* env, jobject thiz, jint a, jint b) {
    return a + b;
}

// 字节数组处理
JNIEXPORT jbyteArray JNICALL
Java_com_example_NativeLib_processBytes(JNIEnv* env, jobject thiz, jbyteArray data) {
    jsize len = env->GetArrayLength(data);
    jbyte* bytes = env->GetByteArrayElements(data, nullptr);

    // 处理数据（示例：每个字节 +1）
    for (jsize i = 0; i < len; i++) bytes[i] += 1;

    jbyteArray result = env->NewByteArray(len);
    env->SetByteArrayRegion(result, 0, len, bytes);
    env->ReleaseByteArrayElements(data, bytes, JNI_ABORT); // 不写回
    return result;
}

// 回调 Java 方法
void callJavaCallback(JNIEnv* env, jobject obj) {
    jclass clazz = env->GetObjectClass(obj);
    jmethodID method = env->GetMethodID(clazz, "onCallback", "(I)V");
    if (method) env->CallVoidMethod(obj, method, 42);
}

} // extern "C"
    """.trimIndent(),
    tips = listOf(
        "避免在 JNI 中频繁创建局部引用且不释放，每帧 16 个局部引用超限会 crash",
        "字符串转换用 env->GetStringUTFChars 后一定要 Release，否则内存泄漏",
        "使用 javah 或 Android Studio 自动生成 JNI 方法签名，减少手写错误"
    )
)

@Composable
fun JniScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF546E7A),
        stageTitle = "NDK 开发",
        onBack = onBack
    )
}
