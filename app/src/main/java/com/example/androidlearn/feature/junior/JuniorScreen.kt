package com.example.androidlearn.feature.junior

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class LearningTopic(val title: String, val description: String)

data class LearningStage(
    val index: Int,       // 全局 stageIndex（0-based），用于路由跳转
    val title: String,
    val subtitle: String,
    val color: Color,
    val topics: List<LearningTopic>
)

// ── 初级阶段（stageIndex 0, 1, 2）────────────────────────────────────────────
val juniorRoadmap = listOf(
    LearningStage(
        index = 0,
        title = "语言与工具基础",
        subtitle = "Java · Kotlin · Android Studio · Git · Gradle · ADB · 泛型",
        color = Color(0xFF4CAF50),
        topics = listOf(
            LearningTopic("Java 核心基础", "面向对象、集合框架、泛型、IO 流、多线程基础、异常处理"),
            LearningTopic("Kotlin 基础语法", "变量/函数/类、Lambda、空安全、扩展函数、数据类、高阶函数"),
            LearningTopic("Android Studio 工具链", "项目结构、Logcat、调试器、模拟器、布局编辑器、快捷键"),
            LearningTopic("Git / Gradle / ADB", "Git 版本控制基础、Gradle 构建脚本解读、ADB 常用调试命令"),
            LearningTopic("资源管理基础", "strings/colors/dimens 资源、多语言/多屏幕适配、VectorDrawable"),
            LearningTopic("泛型机制与类型擦除", "类型擦除、PECS 上下边界、reified 内联函数、星号投影、声明处型变"),
        )
    ),
    LearningStage(
        index = 1,
        title = "四大组件",
        subtitle = "Activity · Service · Broadcast · ContentProvider",
        color = Color(0xFF2196F3),
        topics = listOf(
            LearningTopic("Activity 与 Intent", "生命周期、启动模式、显式/隐式 Intent、数据传递、ActivityResultLauncher、Deep Link"),
            LearningTopic("Service 与后台处理", "前台/后台 Service、IntentService、生命周期、bindService"),
            LearningTopic("BroadcastReceiver", "静态/动态注册、有序广播、LocalBroadcastManager、系统广播"),
            LearningTopic("ContentProvider 数据共享", "URI 设计、增删改查、FileProvider、跨进程数据访问"),
        )
    ),
    LearningStage(
        index = 2,
        title = "UI 组件与数据基础",
        subtitle = "架构入口 · 布局容器 · 常用 View · Fragment · RecyclerView · 权限 · 数据存储 · 网络与图片 · 多媒体",
        color = Color(0xFF009688),
        topics = listOf(
            LearningTopic("Android 架构入口", "Application、Activity 导航、Fragment 导航、BottomNavigationView"),
            LearningTopic("布局容器", "LinearLayout、FrameLayout、RelativeLayout、ScrollView、ConstraintLayout、MotionLayout"),
            LearningTopic("常用 View 与自定义 View", "ViewBinding、TextView/EditText/Button/ImageView、自定义 View、onDraw/onMeasure、attrs.xml"),
            LearningTopic("Fragment 与弹窗", "生命周期、FragmentManager、回退栈、Fragment 间通信、ViewPager2、AlertDialog、DialogFragment、BottomSheetDialogFragment、Snackbar"),
            LearningTopic("RecyclerView 高效列表", "ScrollView/NestedScrollView、Adapter/ViewHolder、DiffUtil、ListAdapter、ItemDecoration"),
            LearningTopic("权限申请基础", "普通/危险/特殊权限、运行时申请、ActivityResultContracts、永久拒绝"),
            LearningTopic("数据存储", "Room 数据库、SharedPreferences、DataStore、存储方案选型"),
            LearningTopic("网络与图片加载", "Retrofit + OkHttp、错误处理、文件上传下载、Glide/Coil 基础用法、缓存策略"),
            LearningTopic("文件与多媒体基础", "文件读写、MediaPlayer 播放、相机拍照、MediaStore 访问媒体库"),
        )
    )
)

// ── 中级阶段（stageIndex 3, 4, 5, 6）────────────────────────────────────────
val intermediateRoadmap = listOf(
    LearningStage(
        index = 3,
        title = "现代架构体系",
        subtitle = "MVVM · ViewModel · 协程 · Flow · Hilt",
        color = Color(0xFF9C27B0),
        topics = listOf(
            LearningTopic("MVVM & ViewModel", "MVVM 三层职责、ViewModel 生命周期、StateFlow、UDF、MVP 对比"),
            LearningTopic("Kotlin 协程与 Flow", "suspend、Dispatcher、async/await、Flow、SharedFlow、Channel"),
            LearningTopic("Hilt 依赖注入", "@HiltAndroidApp、@Inject、@Module、Scope 管理、测试替换"),
        )
    ),
    LearningStage(
        index = 4,
        title = "工程能力进阶",
        subtitle = "WorkManager · Paging · 模块化",
        color = Color(0xFFFF9800),
        topics = listOf(
            LearningTopic("WorkManager 后台调度", "CoroutineWorker、约束条件、链式任务、进度上报、周期任务"),
            LearningTopic("Paging 3 分页加载", "PagingSource、RemoteMediator、LoadState、LazyPagingItems"),
            LearningTopic("模块化架构", "多模块拆分、依赖管理、动态特性模块、模块间通信"),
        )
    ),
    LearningStage(
        index = 5,
        title = "多媒体与系统能力",
        subtitle = "音视频 · 相机 · 蓝牙 · 多进程 · App Widget · 屏幕适配",
        color = Color(0xFF00BCD4),
        topics = listOf(
            LearningTopic("音视频与 ExoPlayer", "MediaPlayer/ExoPlayer 播放、视频渲染、音频焦点、MediaSession"),
            LearningTopic("CameraX 相机开发", "PreviewView、ImageCapture、VideoCapture、人脸/条码分析"),
            LearningTopic("蓝牙与 Wi-Fi 连接", "BLE 扫描/连接/GATT、Wi-Fi Direct、NFC 基础"),
            LearningTopic("多进程与 AIDL", "进程间通信原理、AIDL 接口定义、Messenger、多进程数据同步"),
            LearningTopic("App Widget 与 Shortcut", "桌面小组件（RemoteViews/Glance）、动态快捷方式"),
            LearningTopic("屏幕适配与 Bitmap", "dp/dpi 换算、今日头条/smallestWidth 方案、inSampleSize 采样"),
        )
    ),
    LearningStage(
        index = 6,
        title = "事件机制与动态编程",
        subtitle = "Handler · 事件分发 · 注解 · 反射 · AOP",
        color = Color(0xFF3F51B5),
        topics = listOf(
            LearningTopic("Looper / Handler 消息机制", "MessageQueue、epoll、IdleHandler、主线程消息循环、内存泄漏"),
            LearningTopic("触摸事件分发机制", "dispatch/intercept/onTouchEvent 三级传递、ACTION 序列、优先级"),
            LearningTopic("手势冲突解决方案", "外部拦截法、内部拦截法、NestedScrolling、Compose nestedScroll"),
            LearningTopic("注解与 APT", "自定义注解、注解处理器、KSP、ButterKnife/Retrofit 注解原理"),
            LearningTopic("反射与动态代理", "Class/Field/Method API、JDK 动态代理、Proxy/InvocationHandler"),
            LearningTopic("AOP 面向切面编程", "AspectJ、ASM 字节码插桩、动态代理、CGLIB、Kotlin 委托、方案选型"),
        )
    )
)

// ── 高级阶段（stageIndex 7, 8, 9, 10, 11）────────────────────────────────────────
val seniorRoadmap = listOf(
    LearningStage(
        index = 7,
        title = "Android 底层原理",
        subtitle = "Binder · View绘制 · Activity启动 · ClassLoader · ART/GC · 系统启动",
        color = Color(0xFF795548),
        topics = listOf(
            LearningTopic("Binder 机制深度解析", "一次拷贝、mmap、ServiceManager、AIDL 调用链、Binder 线程池"),
            LearningTopic("View 绘制全流程", "measure/layout/draw 三大步、硬件加速、RenderThread、VSYNC/Choreographer"),
            LearningTopic("Activity 启动全链路", "Launcher→AMS→Zygote fork→ActivityThread→onCreate 完整调用栈"),
            LearningTopic("ClassLoader 与类加载", "双亲委派机制、PathClassLoader/DexClassLoader、热修复类替换原理"),
            LearningTopic("ART 与 GC 机制", "Dalvik vs ART、AOT+JIT 混合编译、分代回收、GC Roots、LMK"),
            LearningTopic("Android 系统架构与启动", "Linux Kernel→HAL→Zygote→SystemServer→Launcher 完整链路"),
        )
    ),
    LearningStage(
        index = 8,
        title = "并发深度与虚拟机",
        subtitle = "JMM · 锁机制 · CAS · AQS · 线程池 · GC算法 · OOM 分析",
        color = Color(0xFFE91E63),
        topics = listOf(
            LearningTopic("JMM 并发内存模型", "主内存与工作内存、happens-before、volatile 可见性与有序性"),
            LearningTopic("synchronized 与锁升级", "Monitor 对象、偏向锁→轻量级锁→重量级锁升级、临界区"),
            LearningTopic("CAS 与 AQS 框架", "CAS 原理与 ABA 问题、ReentrantLock/Semaphore/CountDownLatch"),
            LearningTopic("线程池深度原理", "ThreadPoolExecutor 参数、任务排队策略、拒绝策略、Executors 对比"),
            LearningTopic("GC 算法与对象结构", "标记-复制-整理算法、7 种收集器、klass/markword 内存布局"),
            LearningTopic("OOM 分析与内存工具", "OOM 分类、MAT 堆快照分析、Profiler 实战、Bitmap 内存管理"),
        )
    ),
    LearningStage(
        index = 9,
        title = "性能优化体系",
        subtitle = "启动 · 渲染 · ANR · 包体积 · 稳定性 · 安全",
        color = Color(0xFFFF5722),
        topics = listOf(
            LearningTopic("启动性能优化", "冷/热/温启动分析、App Startup、异步初始化、Baseline Profile"),
            LearningTopic("渲染与内存优化", "Perfetto/Systrace 分析、过度绘制、Compose 重组优化、LeakCanary"),
            LearningTopic("ANR 排查与治理", "主线程耗时分析、ANR Watchdog、StrictMode、Trace 埋点、traces.txt"),
            LearningTopic("包体积优化（APK 瘦身）", "R8 裁剪混淆、资源压缩、App Bundle、动态功能模块、WebP"),
            LearningTopic("稳定性监控体系", "Crash/ANR 监控、Firebase Crashlytics、线上矩阵监控、线下工具"),
            LearningTopic("安全与加固", "HTTPS 证书固定、数据加密、Root/模拟器检测、代码加固、防逆向"),
        )
    ),
    LearningStage(
        index = 10,
        title = "工程化与架构设计",
        subtitle = "Clean Arch · 组件化 · AGP · 签名混淆 · CI/CD · KMM",
        color = Color(0xFF607D8B),
        topics = listOf(
            LearningTopic("Clean Architecture 设计", "分层架构、UseCase、依赖规则、可测试性、多模块依赖治理"),
            LearningTopic("组件化与插件化", "路由框架 ARouter、模块间通信、动态加载 Activity、VirtualAPK"),
            LearningTopic("AGP 与字节码插桩", "AGP 构建流程、AsmClassVisitorFactory、自定义 Gradle Task"),
            LearningTopic("APK 签名与代码混淆", "v1/v2/v3/v4 签名原理、R8 Keep 规则、mapping.txt 还原崩溃"),
            LearningTopic("CI/CD 与多渠道发布", "GitHub Actions/Fastlane、Walle 多渠道、Play Store API 自动化"),
            LearningTopic("Kotlin Multiplatform", "KMM 共享逻辑、expect/actual、与 iOS/Web 互操作"),
        )
    ),
    LearningStage(
        index = 11,
        title = "系统原理与高阶扩展",
        subtitle = "APK编译 · Window · 资源 · 热修复 · NDK · AOP · 版本适配",
        color = Color(0xFF1565C0),
        topics = listOf(
            LearningTopic("APK 编译与安装过程", "AAPT2/D8/R8 编译链、APK 结构、PMS 安装流程、dex2oat"),
            LearningTopic("Window 与资源管理", "WMS/SurfaceFlinger、Window 层级、resources.arsc、换肤原理"),
            LearningTopic("热修复原理", "Tinker/Robust/Sophix、dex 差量合并、ArtMethod 替换、资源热替换"),
            LearningTopic("NDK / JNI 开发", "JNI 数据类型映射、CMakeLists 配置、ABI 过滤、C++ 与 Kotlin 互调"),
            LearningTopic("AOP 面向切面编程", "字节码插桩、AspectJ、ASM Transform、动态代理 AOP 方案对比"),
            LearningTopic("Android 版本适配", "各版本重大变更（10~14）、权限演进、分区存储、targetSdk 升级"),
        )
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JuniorScreen(onTopicClick: (stageIndex: Int, topicIndex: Int) -> Unit = { _, _ -> }) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "初级工程师",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "1 ～ 3 年经验  ·  打好扎实基础",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4CAF50),
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                LevelBadge(
                    label = "初级",
                    yearRange = "1 ～ 3 年",
                    description = "掌握语言基础、四大组件、常用 UI 与数据组件，能独立完成中等复杂度的功能开发。",
                    color = Color(0xFF4CAF50)
                )
            }
            items(juniorRoadmap, key = { it.index }) { stage ->
                StageCard(stage = stage, onTopicClick = onTopicClick)
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

// ── 通用组件（被三个 Tab Screen 复用）─────────────────────────────────────

@Composable
fun LevelBadge(label: String, yearRange: String, description: String, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(color.copy(alpha = 0.15f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = yearRange,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = color
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 17.sp,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }
        }
    }
}

@Composable
fun StageCard(stage: LearningStage, onTopicClick: (stageIndex: Int, topicIndex: Int) -> Unit) {
    var expanded by rememberSaveable(key = "stage_expanded_${stage.index}") { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            // 阶段标题行
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(stage.color, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${stage.index + 1}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stage.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stage.subtitle,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = stage.color.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "${stage.topics.size} 个主题",
                        fontSize = 11.sp,
                        color = stage.color,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "收起" else "展开",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (expanded) {
                Divider(color = MaterialTheme.colorScheme.outlineVariant)
                stage.topics.forEachIndexed { i, topic ->
                    TopicRow(
                        topic = topic,
                        stageColor = stage.color,
                        onClick = { onTopicClick(stage.index, i) }
                    )
                    if (i < stage.topics.lastIndex) {
                        Divider(
                            modifier = Modifier.padding(start = 56.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TopicRow(topic: LearningTopic, stageColor: Color, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .size(8.dp)
                    .background(stageColor, CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = topic.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = topic.description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp),
                    lineHeight = 18.sp
                )
            }
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "查看详情",
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier
                .size(18.dp)
                .padding(top = 2.dp)
        )
    }
}
