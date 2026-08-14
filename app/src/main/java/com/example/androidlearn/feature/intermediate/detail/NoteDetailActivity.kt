package com.example.androidlearn.feature.intermediate.detail

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidlearn.databinding.ActivityNoteDetailBinding
import com.example.androidlearn.R
import com.example.androidlearn.feature.intermediate.detail.stage3.coroutinesData
import com.example.androidlearn.feature.intermediate.detail.stage3.hiltData
import com.example.androidlearn.feature.intermediate.detail.stage3.mvvmArchData
import com.example.androidlearn.feature.intermediate.detail.stage4.modularizationData
import com.example.androidlearn.feature.intermediate.detail.stage4.workManagerData
import com.example.androidlearn.feature.intermediate.detail.stage5.aidlMultiProcessData
import com.example.androidlearn.feature.intermediate.detail.stage5.appWidgetData
import com.example.androidlearn.feature.intermediate.detail.stage5.bluetoothWifiData
import com.example.androidlearn.feature.intermediate.detail.stage5.cameraXData
import com.example.androidlearn.feature.intermediate.detail.stage5.exoPlayerData
import com.example.androidlearn.feature.intermediate.detail.stage5.screenAdaptBitmapData
import com.example.androidlearn.feature.intermediate.detail.stage6.aopData
import com.example.androidlearn.feature.intermediate.detail.stage6.gestureConflictData
import com.example.androidlearn.feature.intermediate.detail.stage6.looperHandlerData
import com.example.androidlearn.feature.intermediate.detail.stage6.touchEventData
import com.example.androidlearn.feature.intermediate.detail.stage6.annotationAptData
import com.example.androidlearn.feature.intermediate.detail.stage6.reflectionData

/**
 * 中级工程师笔记详情页（传统 View 实现）。
 * 通过 Intent 接收 stageIndex + topicIndex，查找对应的 NoteData 并渲染。
 */
class NoteDetailActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_STAGE_INDEX = "stage_index"
        private const val EXTRA_TOPIC_INDEX = "topic_index"

        fun start(context: Context, stageIndex: Int, topicIndex: Int) {
            val intent = Intent(context, NoteDetailActivity::class.java).apply {
                putExtra(EXTRA_STAGE_INDEX, stageIndex)
                putExtra(EXTRA_TOPIC_INDEX, topicIndex)
            }
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityNoteDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val stageIndex = intent.getIntExtra(EXTRA_STAGE_INDEX, 3)
        val topicIndex = intent.getIntExtra(EXTRA_TOPIC_INDEX, 0)

        val data = resolveData(stageIndex, topicIndex) ?: run {
            finish()
            return
        }

        // 设置 Toolbar
        binding.toolbar.setBackgroundColor(data.color)
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.navigationIcon?.let {
            val wrapped = DrawableCompat.wrap(it.mutate())
            DrawableCompat.setTint(wrapped, Color.WHITE)
            binding.toolbar.navigationIcon = wrapped
        }
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.tvTitle.text = data.title
        binding.tvSubtitle.text = data.subtitle

        // 设置 RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = ChapterAdapter(data.chapters, data.color)
    }

    private fun resolveData(stageIndex: Int, topicIndex: Int): NoteData? = when (stageIndex) {
        3 -> when (topicIndex) {
            0 -> mvvmArchData
            1 -> coroutinesData
            2 -> hiltData
            else -> null
        }
        4 -> when (topicIndex) {
            0 -> workManagerData
            1 -> modularizationData
            else -> null
        }
        5 -> when (topicIndex) {
            0 -> exoPlayerData
            1 -> cameraXData
            2 -> bluetoothWifiData
            3 -> aidlMultiProcessData
            4 -> appWidgetData
            5 -> screenAdaptBitmapData
            else -> null
        }
        6 -> when (topicIndex) {
            0 -> looperHandlerData
            1 -> touchEventData
            2 -> gestureConflictData
            3 -> annotationAptData
            4 -> reflectionData
            5 -> aopData
            else -> null
        }
        else -> null
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  数据模型
// ─────────────────────────────────────────────────────────────────────────────

/**
 * 笔记详情页数据模型（传统 View 版）。
 *
 * @param title    页面主标题
 * @param subtitle 页面副标题
 * @param color    主题色（Int，ARGB）
 * @param chapters 章节列表
 */
data class NoteData(
    val title: String,
    val subtitle: String,
    val color: Int,
    val chapters: List<ChapterItem>
)

/**
 * 章节数据。
 *
 * @param num   章节编号，如 "1"、"1.1"
 * @param title 章节标题
 */
data class ChapterItem(val num: String, val title: String)

// ─────────────────────────────────────────────────────────────────────────────
//  RecyclerView Adapter
// ─────────────────────────────────────────────────────────────────────────────

internal class ChapterAdapter(
    private val items: List<ChapterItem>,
    private val themeColor: Int
) : RecyclerView.Adapter<ChapterAdapter.VH>() {

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNum: TextView = itemView.findViewById(R.id.tv_num)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_chapter_title)
        val root: ViewGroup = itemView.findViewById(R.id.item_root)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note_chapter, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        val isTopLevel = !item.num.contains('.')

        // 章节编号
        holder.tvNum.text = item.num
        holder.tvNum.textSize = if (isTopLevel) 12f else 10f
        holder.tvNum.setTextColor(
            if (isTopLevel) themeColor else adjustAlpha(themeColor, 0.75f)
        )
        // 编号徽章背景色
        val numBg = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = dpToPx(holder.itemView.context, 6f)
            setColor(adjustAlpha(themeColor, if (isTopLevel) 0.18f else 0.08f))
        }
        holder.tvNum.background = numBg

        // 章节标题
        holder.tvTitle.text = item.title
        holder.tvTitle.textSize = if (isTopLevel) 15f else 13f
        holder.tvTitle.setTypeface(
            null,
            if (isTopLevel) android.graphics.Typeface.BOLD else android.graphics.Typeface.NORMAL
        )
        holder.tvTitle.alpha = if (isTopLevel) 1f else 0.85f

        // 缩进：二级章节左移
        val startPadding = dpToPx(holder.itemView.context, if (isTopLevel) 14f else 26f).toInt()
        holder.root.setPaddingRelative(
            startPadding,
            dpToPx(holder.itemView.context, if (isTopLevel) 13f else 10f).toInt(),
            dpToPx(holder.itemView.context, 14f).toInt(),
            dpToPx(holder.itemView.context, if (isTopLevel) 13f else 10f).toInt()
        )

        // 卡片阴影
        (holder.itemView as? CardView)?.cardElevation =
            dpToPx(holder.itemView.context, if (isTopLevel) 2f else 1f)
    }

    override fun getItemCount() = items.size

    private fun adjustAlpha(color: Int, alpha: Float): Int {
        val a = (Color.alpha(color) * alpha).toInt().coerceIn(0, 255)
        return Color.argb(a, Color.red(color), Color.green(color), Color.blue(color))
    }

    private fun dpToPx(context: Context, dp: Float): Float =
        dp * context.resources.displayMetrics.density
}
