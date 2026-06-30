package com.example.androidlearn.feature.senior.detail.stage13

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "C/C++ 核心基础",
    description = "指针与内存管理，STL 容器，面向对象，C++11 现代特性",
    overview = "NDK 开发的基础是扎实的 C/C++ 能力。理解指针、内存管理、STL 和现代 C++ 特性，是编写高性能、无内存泄漏 Native 代码的前提。",
    keyPoints = listOf(
        "指针与引用：原始指针、引用的区别，数组指针，函数指针，void* 泛型指针",
        "内存管理：栈 vs 堆，new/delete vs malloc/free，内存泄漏检测（Valgrind/ASan）",
        "智能指针（C++11）：unique_ptr（独占）、shared_ptr（共享）、weak_ptr（弱引用）",
        "STL 容器：vector、map、unordered_map、set、queue、deque 及其复杂度",
        "C++11 特性：auto 类型推导、lambda 表达式、移动语义（move）、右值引用（&&）",
        "面向对象：继承、多态（虚函数表 vtable）、构造/析构顺序、纯虚类（接口）"
    ),
    codeSnippet = """
#include <memory>
#include <vector>
#include <unordered_map>

// 智能指针（推荐替代裸指针）
auto ptr = std::make_unique<int>(42);          // 独占
auto shared = std::make_shared<std::string>("hello"); // 共享

// Lambda 表达式
auto add = [](int a, int b) -> int { return a + b; };
int result = add(3, 4); // result = 7

// 移动语义避免不必要拷贝
std::vector<int> createData() {
    std::vector<int> v = {1, 2, 3, 4, 5};
    return v; // RVO/移动语义，无拷贝开销
}

// 范围 for 循环
std::vector<int> nums = {1, 2, 3};
for (const auto& n : nums) {
    // 处理每个元素
}

// unordered_map O(1) 查找
std::unordered_map<std::string, int> scores;
scores["Alice"] = 95;
scores["Bob"] = 87;
if (scores.count("Alice")) {
    // 找到了
}
    """.trimIndent(),
    tips = listOf(
        "NDK 开发优先使用 unique_ptr/shared_ptr，避免手动 new/delete 导致的内存泄漏",
        "Android NDK 支持 C++17，可以使用结构化绑定（auto [key, val] = pair）等现代特性",
        "用 AddressSanitizer（ASan）检测 Native 内存问题：build.gradle 中开启 sanitizers"
    )
)

@Composable
fun CppBasicsScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF546E7A),
        stageTitle = "NDK 开发",
        onBack = onBack
    )
}
