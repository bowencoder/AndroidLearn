package com.example.androidlearn.feature.junior.detail.stage2

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "RecyclerView",
    description = "Adapter、ViewHolder、LayoutManager、DiffUtil",
    overview = "RecyclerView 是 Android 最核心的列表组件，通过 ViewHolder 复用机制高效渲染大量数据。",
    keyPoints = listOf(
        "Adapter：数据与 View 的桥梁，onCreateViewHolder / onBindViewHolder",
        "ViewHolder：缓存 View 引用，避免重复 findViewById",
        "LayoutManager：Linear（列表）/ Grid（网格）/ StaggeredGrid（瀑布流）",
        "DiffUtil：计算数据差异，局部刷新性能更好",
        "ListAdapter：配合 DiffUtil 的便捷 Adapter 基类",
        "ItemDecoration / ItemAnimator：分割线与动画"
    ),
    codeSnippet = """
class MyAdapter : ListAdapter<Item, MyAdapter.VH>(DiffCallback()) {
    class VH(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, type: Int) =
        VH(ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.tvTitle.text = getItem(position).title
    }

    class DiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(old: Item, new: Item) = old.id == new.id
        override fun areContentsTheSame(old: Item, new: Item) = old == new
    }
}
    """.trimIndent(),
    tips = listOf(
        "使用 ListAdapter + DiffUtil，避免 notifyDataSetChanged() 全量刷新",
        "点击监听在 onCreateViewHolder 中设置，不要在 onBindViewHolder",
        "Compose 使用 LazyColumn / LazyGrid 代替 RecyclerView"
    )
)

@Composable
fun RecyclerViewScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF2196F3),
        stageTitle = "核心UI与数据组件",
        onBack = onBack
    )
}
