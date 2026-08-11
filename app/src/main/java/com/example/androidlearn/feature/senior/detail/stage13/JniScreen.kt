package com.example.androidlearn.feature.senior.detail.stage13

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val jniData = NoteData(
    title = "JNI 调用机制",
    subtitle = "Java 与 Native 互调，数据类型映射，局部/全局引用，异常处理",
    color = Color.parseColor("#546E7A"),
    chapters = listOf(
        ChapterItem("1",   "数据类型映射"),
        ChapterItem("1.1", "jint/jlong/jstring/jbyteArray 等 JNI 类型与 Java 类型的对应关系"),
        ChapterItem("2",   "函数命名规则"),
        ChapterItem("2.1", "Java_包名_类名_方法名，下划线替换点号（如 Java_com_example_Foo_bar）"),
        ChapterItem("3",   "局部引用 vs 全局引用"),
        ChapterItem("3.1", "局部引用在 JNI 调用结束后自动释放，全局引用需手动 DeleteGlobalRef"),
        ChapterItem("4",   "字符串处理"),
        ChapterItem("4.1", "GetStringUTFChars 获取 UTF-8 字符串，使用后必须 ReleaseStringUTFChars"),
        ChapterItem("5",   "数组操作"),
        ChapterItem("5.1", "GetByteArrayElements / ReleaseByteArrayElements，避免 critical 锁定过长"),
        ChapterItem("6",   "异常处理"),
        ChapterItem("6.1", "ExceptionCheck / ExceptionClear，Native 层捕获并处理 Java 异常"),
    )
)
