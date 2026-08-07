package com.example.androidlearn.feature.junior.detail.stage2

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "布局与 View",
    description = "LinearLayout、ConstraintLayout、常用控件",
    overview = "XML 布局是传统 View 体系的核心，ConstraintLayout 是目前最推荐的布局容器，性能优秀且扁平化。",
    keyPoints = listOf(
        "LinearLayout：线性排列，weight 按比例分配空间",
        "ConstraintLayout：约束布局，替代嵌套，推荐首选",
        "FrameLayout：层叠布局，常用于 Fragment 容器",
        "RecyclerView：高性能列表，替代 ListView",
        "TextView / Button / ImageView / EditText：基础控件",
        "ViewBinding：类型安全地访问 View，替代 findViewById"
    ),
    codeSnippet = """
<androidx.constraintlayout.widget.ConstraintLayout>
    <TextView
        android:id="@+id/tvTitle"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        android:text="标题" />
    <Button
        app:layout_constraintTop_toBottomOf="@id/tvTitle"
        android:text="点击" />
</androidx.constraintlayout.widget.ConstraintLayout>
    """.trimIndent(),
    tips = listOf(
        "尽量减少布局嵌套层级，ConstraintLayout 一层解决大多数场景",
        "使用 ViewBinding 而非 findViewById，避免类型错误",
        "复杂列表用 RecyclerView + DiffUtil，性能更好"
    )
)

@Composable
fun LayoutViewScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF4CAF50),
        stageTitle = "语言与开发基础",
        onBack = onBack
    )
}
