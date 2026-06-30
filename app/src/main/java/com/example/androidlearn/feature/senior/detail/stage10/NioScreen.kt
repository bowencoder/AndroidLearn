package com.example.androidlearn.feature.senior.detail.stage10

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【高效 IO 与序列化】专属学习页
//  stageIndex=9, topicIndex=5
//  阶段颜色：粉红 0xFFE91E63（高级扩展 Stage 9）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "高效 IO 与序列化",
    description = "NIO 内核机制、epoll、零拷贝、Java 序列化原理与性能对比",
    overview = "IO 性能直接影响网络请求速度和数据传输效率。NIO + epoll 是 OkHttp/Netty 的基础，零拷贝技术大幅减少 CPU 开销，序列化格式的选择影响数据体积和解析速度。",
    keyPoints = listOf(
        "BIO vs NIO：BIO 阻塞等待（一线程一连接），NIO 非阻塞（Selector 多路复用）",
        "Selector + Channel：一个 Selector 监听多个 Channel 事件，适合高并发连接",
        "epoll（Linux）：事件驱动，只通知就绪的 FD，O(1) 复杂度，Android/OkHttp 底层",
        "零拷贝：transferTo() 直接 DMA 传输，避免内核态↔用户态数据复制，减少 CPU 负担",
        "序列化对比：Java 序列化（慢/大）< JSON（易读）< Protobuf（快/小）< FlatBuffers（零解析）",
        "Android 序列化：Parcelable（IPC/内存高效）> Serializable（简单，频繁 GC）"
    ),
    codeSnippet = """
// NIO Selector 核心用法
val selector = Selector.open()
val channel = SocketChannel.open().apply {
    configureBlocking(false)    // 非阻塞模式
    connect(InetSocketAddress("api.example.com", 80))
    register(selector, SelectionKey.OP_CONNECT)
}

while (true) {
    selector.select()  // 阻塞直到有就绪事件
    val keys = selector.selectedKeys().iterator()
    while (keys.hasNext()) {
        val key = keys.next(); keys.remove()
        when {
            key.isConnectable -> {
                (key.channel() as SocketChannel).finishConnect()
                key.interestOps(SelectionKey.OP_READ)
            }
            key.isReadable -> {
                val buf = ByteBuffer.allocate(1024)
                (key.channel() as SocketChannel).read(buf)
                // 处理读取的数据
            }
        }
    }
}

// Protobuf 序列化（比 JSON 小 3-5 倍，解析快 5-10 倍）
// person.proto: message Person { string name = 1; int32 age = 2; }
val person = Person.newBuilder().setName("Alice").setAge(25).build()
val bytes = person.toByteArray()   // 紧凑二进制
val decoded = Person.parseFrom(bytes)

// Android Parcelable（IPC 数据传递，比 Serializable 快 10x）
@Parcelize
data class User(val id: Int, val name: String) : Parcelable
    """.trimIndent(),
    tips = listOf(
        "OkHttp 底层用 Okio 封装 NIO，Socket 读写都经过 epoll，理解它帮助排查网络问题",
        "新项目用 Protobuf 替代 JSON，接口数据体积减少 60-70%，解析 CPU 降低明显",
        "Android IPC 用 Parcelable，加 @Parcelize 注解自动生成代码，无需手写"
    )
)

@Composable
fun NioScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFFE91E63),
        stageTitle = "并发编程深度",
        onBack = onBack
    )
}
