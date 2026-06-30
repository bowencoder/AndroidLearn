package com.example.androidlearn.feature.senior.detail.stage15

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【SurfaceView 与自定义渲染】专属学习页
//  stageIndex=14, topicIndex=4
//  阶段颜色：橙红 0xFFFF5722（UI 进阶）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "SurfaceView 与自定义渲染",
    description = "SurfaceView/TextureView 区别、双缓冲机制、Canvas 绘制与 OpenGL ES 集成",
    overview = "普通 View 的绘制在主线程中进行，无法承载高频刷新（如视频播放、游戏、相机预览）。SurfaceView 拥有独立的 Surface 和绘制线程，可在后台线程进行绘制，适合高性能渲染场景。TextureView 则更灵活，支持动画变换，但消耗更多内存。",
    keyPoints = listOf(
        "SurfaceView：独立 Window（不在 View 层级中），绘制在子线程，性能高，但不支持普通 View 动画变换",
        "TextureView：在 View 层级中，复用硬件加速层（SurfaceTexture），支持 alpha/旋转等变换，但比 SurfaceView 消耗更多内存",
        "SurfaceHolder：SurfaceView 的控制接口，通过回调（surfaceCreated/Changed/Destroyed）感知 Surface 生命周期",
        "双缓冲机制：前缓冲（显示中）和后缓冲（绘制中）交替显示，避免撕裂（Tearing）",
        "Canvas 绘制流程：lockCanvas() 获取画布 → 绘制内容 → unlockCanvasAndPost() 提交到 Surface",
        "GLSurfaceView：SurfaceView 的子类，内置 OpenGL ES 上下文和渲染线程，用于游戏和 3D 渲染"
    ),
    codeSnippet = """
// SurfaceView 基础使用（自定义绘图线程）
class GameSurfaceView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private var drawThread: DrawThread? = null

    init {
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        drawThread = DrawThread(holder).also { it.start() }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // Surface 尺寸/格式变化时调用
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        drawThread?.let {
            it.running = false
            it.join()  // 等待线程结束，避免使用已销毁的 Surface
        }
    }

    class DrawThread(private val holder: SurfaceHolder) : Thread() {
        var running = true
        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        override fun run() {
            while (running) {
                var canvas: Canvas? = null
                try {
                    canvas = holder.lockCanvas()  // 获取画布（阻塞到 Vsync）
                    synchronized(holder) {
                        // 清屏
                        canvas.drawColor(Color.BLACK)
                        // 绘制内容
                        paint.color = Color.WHITE
                        canvas.drawCircle(200f, 200f, 50f, paint)
                    }
                } finally {
                    canvas?.let { holder.unlockCanvasAndPost(it) }  // 提交
                }
                sleep(16)  // 控制帧率约 60fps
            }
        }
    }
}

// TextureView（支持变换动画）
class CameraPreview(context: Context) : TextureView(context), TextureView.SurfaceTextureListener {
    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        // 将 Camera 输出绑定到此 SurfaceTexture
        camera.setPreviewTexture(surface)
        camera.startPreview()
    }
    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture) = true
    override fun onSurfaceTextureSizeChanged(s: SurfaceTexture, w: Int, h: Int) {}
    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
}
// TextureView 支持直接做动画
textureView.animate().rotationY(180f).setDuration(500).start()
    """.trimIndent(),
    tips = listOf(
        "SurfaceView 的 Surface 在普通 View 层级之下（或之上），无法与普通 View 做叠加动画，可用 TextureView 替代",
        "lockCanvas(dirty: Rect) 可只刷新脏区域，减少不必要的绘制开销",
        "视频播放优先使用 SurfaceView（ExoPlayer 默认），因为 TextureView 的帧复制会增加一次内存拷贝"
    )
)

@Composable
fun SurfaceViewScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFFFF5722),
        stageTitle = "Android UI 进阶",
        onBack = onBack
    )
}
