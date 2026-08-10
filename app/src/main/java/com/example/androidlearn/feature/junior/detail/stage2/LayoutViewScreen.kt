package com.example.androidlearn.feature.junior.detail.stage2

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
 * 布局与 View 笔记
 * 官方文档：https://developer.android.com/develop/ui/views/layout/declaring-layout
 *
 * ── 1  常用布局容器 ───────────────────────────────────────────────────────────
 *
 *  LinearLayout：线性排列（水平/垂直），weight 按比例分配剩余空间
 *  <LinearLayout
 *      android:orientation="horizontal"
 *      android:layout_width="match_parent"
 *      android:layout_height="wrap_content">
 *      <TextView android:layout_weight="1" ... />
 *      <Button  android:layout_weight="1" ... />
 *  </LinearLayout>
 *
 *  FrameLayout：层叠布局，子 View 默认左上角叠放；常用于 Fragment 容器
 *  <FrameLayout android:id="@+id/container" ... />
 *
 *  RelativeLayout：相对定位，已被 ConstraintLayout 取代，不推荐新项目使用
 *
 *
 * ── 2  ConstraintLayout（推荐首选）────────────────────────────────────────────
 *
 *  · 扁平化布局，一层解决大多数场景，性能优于多层嵌套
 *  · 约束方向：Start/End/Top/Bottom，必须在水平和垂直方向各有至少一个约束
 *
 *  <androidx.constraintlayout.widget.ConstraintLayout>
 *      <TextView
 *          android:id="@+id/tvTitle"
 *          app:layout_constraintTop_toTopOf="parent"
 *          app:layout_constraintStart_toStartOf="parent"
 *          android:text="标题" />
 *      <Button
 *          android:id="@+id/btnOk"
 *          app:layout_constraintTop_toBottomOf="@id/tvTitle"
 *          app:layout_constraintEnd_toEndOf="parent"
 *          android:text="确认" />
 *  </androidx.constraintlayout.widget.ConstraintLayout>
 *
 *  · Guideline：辅助线，按百分比或固定距离划分区域
 *  · Barrier：跟随多个 View 中最大尺寸的边缘
 *  · Group：批量控制多个 View 的可见性
 *  · Chain：水平/垂直方向均匀分布（spread / packed / spread_inside）
 *
 *
 * ── 3  RecyclerView ───────────────────────────────────────────────────────────
 *
 *  · 高性能列表，替代 ListView；通过 ViewHolder 复用 View
 *
 *  // Adapter
 *  class MyAdapter(private val items: List<String>) :
 *      RecyclerView.Adapter<MyAdapter.VH>() {
 *
 *      class VH(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)
 *
 *      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
 *          val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
 *          return VH(binding)
 *      }
 *
 *      override fun onBindViewHolder(holder: VH, position: Int) {
 *          holder.binding.tvName.text = items[position]
 *      }
 *
 *      override fun getItemCount() = items.size
 *  }
 *
 *  // 设置
 *  recyclerView.layoutManager = LinearLayoutManager(this)
 *  recyclerView.adapter = MyAdapter(dataList)
 *
 *  · DiffUtil：高效计算列表差异，只刷新变化的 Item
 *  · ListAdapter：内置 DiffUtil，推荐替代普通 Adapter
 *  · GridLayoutManager：网格布局；StaggeredGridLayoutManager：瀑布流
 *
 *
 * ── 4  ViewBinding ────────────────────────────────────────────────────────────
 *
 *  · 类型安全地访问 View，替代 findViewById，编译期检查，不会 NPE
 *
 *  // build.gradle.kts 开启
 *  android {
 *      buildFeatures { viewBinding = true }
 *  }
 *
 *  // Activity 中使用
 *  private lateinit var binding: ActivityMainBinding
 *  override fun onCreate(savedInstanceState: Bundle?) {
 *      super.onCreate(savedInstanceState)
 *      binding = ActivityMainBinding.inflate(layoutInflater)
 *      setContentView(binding.root)
 *      binding.tvTitle.text = "Hello"
 *      binding.btnOk.setOnClickListener { /* ... */ }
 *  }
 *
 *  // Fragment 中使用（注意在 onDestroyView 置 null）
 *  private var _binding: FragmentHomeBinding? = null
 *  private val binding get() = _binding!!
 *  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
 *      _binding = FragmentHomeBinding.inflate(inflater, container, false)
 *      return binding.root
 *  }
 *  override fun onDestroyView() { super.onDestroyView(); _binding = null }
 *
 *
 * ── 5  常用基础控件 ───────────────────────────────────────────────────────────
 *
 *  TextView：
 *  · android:ellipsize="end" + android:maxLines="2"：超出省略
 *  · android:autoLink="web|phone"：自动识别链接
 *  · Spannable：富文本（颜色、大小、点击）
 *
 *  EditText：
 *  · android:inputType="textPassword"：密码输入
 *  · android:imeOptions="actionDone"：键盘右下角按钮
 *  · TextWatcher：监听文字变化
 *
 *  ImageView：
 *  · android:scaleType="centerCrop"：裁剪填充
 *  · 加载网络图片用 Glide / Coil（不要在主线程 decode Bitmap）
 *
 *  Button / MaterialButton：
 *  · MaterialButton 支持 icon、cornerRadius、strokeColor 等 Material 属性
 *
 *
 * ── 6  自定义 View ────────────────────────────────────────────────────────────
 *
 *  class CircleView @JvmOverloads constructor(
 *      context: Context,
 *      attrs: AttributeSet? = null,
 *      defStyleAttr: Int = 0
 *  ) : View(context, attrs, defStyleAttr) {
 *
 *      private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
 *          color = Color.BLUE
 *          style = Paint.Style.FILL
 *      }
 *
 *      override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
 *          val size = resolveSize(200, widthMeasureSpec)
 *          setMeasuredDimension(size, size)
 *      }
 *
 *      override fun onDraw(canvas: Canvas) {
 *          val cx = width / 2f
 *          canvas.drawCircle(cx, cx, cx, paint)
 *      }
 *  }
 *
 *  · onMeasure：确定 View 尺寸
 *  · onLayout：确定子 View 位置（ViewGroup 重写）
 *  · onDraw：绘制内容；避免在此创建对象（GC 压力）
 *  · invalidate()：请求重绘；requestLayout()：请求重新测量+布局
 *
 *
 * ── 7  最佳实践 ───────────────────────────────────────────────────────────────
 *
 *  · 减少布局嵌套层级，优先用 ConstraintLayout 扁平化
 *  · 使用 ViewBinding 替代 findViewById，避免类型错误和 NPE
 *  · RecyclerView 配合 ListAdapter + DiffUtil，避免 notifyDataSetChanged()
 *  · 图片加载用 Glide/Coil，不要手动管理 Bitmap 内存
 *  · 自定义 View 的 onDraw 中不要 new 对象，提前在成员变量初始化
 *  · 新项目考虑直接用 Jetpack Compose，彻底告别 XML 布局
 */

private val Green = Color(0xFF4CAF50)

private data class LayoutChapter(val num: String, val title: String)

private val chapters = listOf(
    LayoutChapter("1", "常用布局容器"),
    LayoutChapter("2", "ConstraintLayout（推荐首选）"),
    LayoutChapter("3", "RecyclerView"),
    LayoutChapter("4", "ViewBinding"),
    LayoutChapter("5", "常用基础控件"),
    LayoutChapter("6", "自定义 View"),
    LayoutChapter("7", "最佳实践"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutViewScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("布局与 View", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        Text(
                            "ConstraintLayout · RecyclerView · ViewBinding",
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
                    containerColor = Green,
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
            items(chapters.size) { i -> ChapterRowLayout(chapters[i]) }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun ChapterRowLayout(chapter: LayoutChapter) {
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
                color = Green.copy(alpha = 0.12f)
            ) {
                Text(
                    chapter.num,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Green
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(chapter.title, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}
