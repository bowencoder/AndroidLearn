package com.example.androidlearn.feature.senior.detail.stage13

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val cppBasicsData = NoteData(
    title = "C/C++ 核心基础",
    subtitle = "指针与内存管理，STL 容器，面向对象，C++11 现代特性",
    color = Color.parseColor("#546E7A"),
    chapters = listOf(
        ChapterItem("1",   "指针与引用"),
        ChapterItem("1.1", "原始指针、引用的区别，数组指针，函数指针，void* 泛型指针"),
        ChapterItem("2",   "内存管理"),
        ChapterItem("2.1", "栈 vs 堆，new/delete vs malloc/free，内存泄漏检测（Valgrind/ASan）"),
        ChapterItem("3",   "智能指针（C++11）"),
        ChapterItem("3.1", "unique_ptr（独占）、shared_ptr（共享）、weak_ptr（弱引用）"),
        ChapterItem("4",   "STL 容器"),
        ChapterItem("4.1", "vector、map、unordered_map、set、queue、deque 及其复杂度"),
        ChapterItem("5",   "C++11 特性"),
        ChapterItem("5.1", "auto 类型推导、lambda 表达式、移动语义（move）、右值引用（&&）"),
        ChapterItem("6",   "面向对象"),
        ChapterItem("6.1", "继承、多态（虚函数表 vtable）、构造/析构顺序、纯虚类（接口）"),
    )
)
