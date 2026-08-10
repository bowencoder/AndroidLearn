package com.example.androidlearn.feature.junior.detail.stage3

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/*
 * RecyclerView 笔记
 * 官方文档：https://developer.android.com/develop/ui/views/layout/recyclerview
 *
 * ── 1  核心组件 ───────────────────────────────────────────────────────────────
 *
 *  RecyclerView 由四个核心部分组成：
 *  · Adapter：数据与 View 的桥梁，负责创建和绑定 ViewHolder
 *  · ViewHolder：缓存 View 引用，避免重复 findViewById，是复用的基本单元
 *  · LayoutManager：决定 Item 的排列方式（线性/网格/瀑布流）
 *  · ItemDecoration：添加分割线、间距等装饰
 *
 *  // 基础设置
 *  recyclerView.layoutManager = LinearLayoutManager(this)
 *  recyclerView.adapter = MyAdapter()
 *  recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
 *
 *
 * ── 2  ListAdapter + DiffUtil（推荐）─────────────────────────────────────────
 *
 *  · ListAdapter 内置 DiffUtil，异步计算差异，只刷新变化的 Item
 *  · 避免 notifyDataSetChanged() 全量刷新导致的闪烁和性能问题
 *
 *  data class Item(val id: Int, val title: String, val desc: String)
 *
 *  class MyAdapter : ListAdapter<Item, MyAdapter.VH>(DiffCallback) {
 *
 *      companion object DiffCallback : DiffUtil.ItemCallback<Item>() {
 *          // 判断是否是同一个 Item（通常比较 id）
 *          override fun areItemsTheSame(oldItem: Item, newItem: Item) =
 *              oldItem.id == newItem.id
 *          // 判断内容是否相同（data class 自动实现 equals）
 *          override fun areContentsTheSame(oldItem: Item, newItem: Item) =
 *              oldItem == newItem
 *      }
 *
 *      class VH(val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root)
 *
 *      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
 *          val binding = ItemCardBinding.inflate(
 *              LayoutInflater.from(parent.context), parent, false
 *          )
 *          return VH(binding)
 *      }
 *
 *      override fun onBindViewHolder(holder: VH, position: Int) {
 *          val item = getItem(position)
 *          holder.binding.tvTitle.text = item.title
 *          holder.binding.tvDesc.text = item.desc
 *      }
 *  }
 *
 *  // 提交新数据（DiffUtil 自动计算差异）
 *  adapter.submitList(newList)
 *
 *
 * ── 3  点击事件处理 ───────────────────────────────────────────────────────────
 *
 *  // 方式一：在 onCreateViewHolder 中设置（推荐）
 *  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
 *      val binding = ItemCardBinding.inflate(...)
 *      val holder = VH(binding)
 *      // 在此设置，避免 onBindViewHolder 中重复设置
 *      binding.root.setOnClickListener {
 *          val position = holder.bindingAdapterPosition
 *          if (position != RecyclerView.NO_ID.toInt()) {
 *              onItemClick(getItem(position))
 *          }
 *      }
 *      return holder
 *  }
 *
 *  // 方式二：通过构造函数传入回调
 *  class MyAdapter(
 *      private val onItemClick: (Item) -> Unit,
 *      private val onItemLongClick: (Item) -> Boolean
 *  ) : ListAdapter<Item, MyAdapter.VH>(DiffCallback) { ... }
 *
 *  // 使用
 *  val adapter = MyAdapter(
 *      onItemClick = { item -> navigateToDetail(item.id) },
 *      onItemLongClick = { item -> showDeleteDialog(item); true }
 *  )
 *
 *
 * ── 4  LayoutManager 布局方式 ─────────────────────────────────────────────────
 *
 *  // 线性列表（垂直/水平）
 *  LinearLayoutManager(context)                                    // 垂直
 *  LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)  // 水平
 *
 *  // 网格布局
 *  GridLayoutManager(context, 2)                                   // 2 列
 *  GridLayoutManager(context, 3, GridLayoutManager.HORIZONTAL, false)   // 3 行水平
 *
 *  // 瀑布流
 *  StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
 *
 *  // 动态切换布局
 *  recyclerView.layoutManager = if (isGrid) GridLayoutManager(context, 2)
 *                               else LinearLayoutManager(context)
 *
 *
 * ── 5  多类型 Item（Multi ViewType）──────────────────────────────────────────
 *
 *  // 定义 Item 类型
 *  sealed class FeedItem {
 *      data class Header(val title: String) : FeedItem()
 *      data class Content(val id: Int, val text: String) : FeedItem()
 *      object Footer : FeedItem()
 *  }
 *
 *  class FeedAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
 *      companion object {
 *          const val TYPE_HEADER  = 0
 *          const val TYPE_CONTENT = 1
 *          const val TYPE_FOOTER  = 2
 *      }
 *
 *      override fun getItemViewType(position: Int) = when (items[position]) {
 *          is FeedItem.Header  -> TYPE_HEADER
 *          is FeedItem.Content -> TYPE_CONTENT
 *          is FeedItem.Footer  -> TYPE_FOOTER
 *      }
 *
 *      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
 *          TYPE_HEADER  -> HeaderVH(ItemHeaderBinding.inflate(...))
 *          TYPE_CONTENT -> ContentVH(ItemContentBinding.inflate(...))
 *          else         -> FooterVH(ItemFooterBinding.inflate(...))
 *      }
 *
 *      override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
 *          when (holder) {
 *              is HeaderVH  -> holder.bind(items[position] as FeedItem.Header)
 *              is ContentVH -> holder.bind(items[position] as FeedItem.Content)
 *          }
 *      }
 *  }
 *
 *
 * ── 6  ItemDecoration 与 ItemAnimator ─────────────────────────────────────────
 *
 *  // 系统分割线
 *  recyclerView.addItemDecoration(
 *      DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
 *  )
 *
 *  // 自定义间距
 *  class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
 *      override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
 *          outRect.bottom = space
 *          if (parent.getChildAdapterPosition(view) == 0) outRect.top = space
 *      }
 *  }
 *  recyclerView.addItemDecoration(SpaceItemDecoration(16.dp.toPx()))
 *
 *  // 默认动画（增删改带动画）
 *  recyclerView.itemAnimator = DefaultItemAnimator()
 *  // 关闭动画（列表频繁刷新时）
 *  recyclerView.itemAnimator = null
 *
 *
 * ── 7  性能优化 ───────────────────────────────────────────────────────────────
 *
 *  · setHasFixedSize(true)：Item 尺寸固定时开启，跳过重新测量
 *  · setItemViewCacheSize(n)：增大屏幕外缓存数量，减少 onCreateViewHolder 调用
 *  · RecycledViewPool：多个 RecyclerView 共享 ViewHolder 缓存（如 ViewPager2 中）
 *  · 图片加载指定固定尺寸，避免布局抖动
 *  · onBindViewHolder 中不做耗时操作，数据处理提前在后台完成
 *  · 使用 ListAdapter + DiffUtil，避免 notifyDataSetChanged()
 *
 *  recyclerView.setHasFixedSize(true)
 *  recyclerView.setItemViewCacheSize(20)
 *
 *  // 预取（默认开启，滚动时提前创建下一个 Item）
 *  (recyclerView.layoutManager as LinearLayoutManager)
 *      .initialPrefetchItemCount = 4
 *
 *
 * ── 8  最佳实践 ───────────────────────────────────────────────────────────────
 *
 *  · 优先使用 ListAdapter + DiffUtil，不要用 notifyDataSetChanged()
 *  · 点击事件在 onCreateViewHolder 中设置，不要在 onBindViewHolder（避免重复绑定）
 *  · ViewHolder 中用 ViewBinding，不要 findViewById
 *  · 图片加载用 Coil/Glide，指定固定尺寸
 *  · Compose 项目用 LazyColumn/LazyGrid 替代 RecyclerView，更简洁
 *  · 多类型 Item 用 sealed class 管理，避免魔法数字
 */

private val Teal = Color(0xFF009688)

private data class RvChapter(val num: String, val title: String)

private val chapters = listOf(
    RvChapter("1", "核心组件"),
    RvChapter("2", "ListAdapter + DiffUtil（推荐）"),
    RvChapter("3", "点击事件处理"),
    RvChapter("4", "LayoutManager 布局方式"),
    RvChapter("5", "多类型 Item（Multi ViewType）"),
    RvChapter("6", "ItemDecoration 与 ItemAnimator"),
    RvChapter("7", "性能优化"),
    RvChapter("8", "最佳实践"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecyclerViewScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("RecyclerView 高效列表", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        Text(
                            "Adapter · DiffUtil · LayoutManager · 多类型",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "返回", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Teal,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(chapters.size) { i -> ChapterRowRv(chapters[i]) }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun ChapterRowRv(chapter: RvChapter) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Teal.copy(alpha = 0.12f)
            ) {
                Text(
                    chapter.num,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Teal
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(chapter.title, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}
