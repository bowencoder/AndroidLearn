package com.example.androidlearn.feature.senior.detail

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidlearn.databinding.ActivityNoteDetailBinding
import com.example.androidlearn.feature.intermediate.detail.ChapterAdapter
import com.example.androidlearn.feature.intermediate.detail.NoteData
import com.example.androidlearn.feature.senior.detail.stage5.anrData
import com.example.androidlearn.feature.senior.detail.stage5.appStartupData
import com.example.androidlearn.feature.senior.detail.stage5.packageSizeData
import com.example.androidlearn.feature.senior.detail.stage5.renderOptimizationData
import com.example.androidlearn.feature.senior.detail.stage5.securityData
import com.example.androidlearn.feature.senior.detail.stage5.stabilityMonitorData
import com.example.androidlearn.feature.senior.detail.stage6.androidInternalsData
import com.example.androidlearn.feature.senior.detail.stage6.ciCdAdvancedData
import com.example.androidlearn.feature.senior.detail.stage6.cleanArchData
import com.example.androidlearn.feature.senior.detail.stage6.gradlePluginData
import com.example.androidlearn.feature.senior.detail.stage6.kmmData
import com.example.androidlearn.feature.senior.detail.stage6.techDesignData
import com.example.androidlearn.feature.senior.detail.stage7.activityStartupData
import com.example.androidlearn.feature.senior.detail.stage7.artGcData
import com.example.androidlearn.feature.senior.detail.stage7.binderData
import com.example.androidlearn.feature.senior.detail.stage7.classLoaderData
import com.example.androidlearn.feature.senior.detail.stage7.viewDrawData
import com.example.androidlearn.feature.senior.detail.stage10.casData
import com.example.androidlearn.feature.senior.detail.stage10.jmmData
import com.example.androidlearn.feature.senior.detail.stage10.synchronizedData
import com.example.androidlearn.feature.senior.detail.stage10.threadPoolData
import com.example.androidlearn.feature.senior.detail.stage11.gcAlgorithmData
import com.example.androidlearn.feature.senior.detail.stage11.oomAnalysisData
import com.example.androidlearn.feature.senior.detail.stage12.hotfixData
import com.example.androidlearn.feature.senior.detail.stage12.pluginizationData
import com.example.androidlearn.feature.senior.detail.stage13.jniData
import com.example.androidlearn.feature.senior.detail.stage14.apkBuildData
import com.example.androidlearn.feature.senior.detail.stage14.appInstallData
import com.example.androidlearn.feature.senior.detail.stage14.resourceMgrData
import com.example.androidlearn.feature.senior.detail.stage14.skinChangeData
import com.example.androidlearn.feature.senior.detail.stage14.systemArchData
import com.example.androidlearn.feature.senior.detail.stage14.windowMechanismData
import com.example.androidlearn.feature.senior.detail.stage16.agpTransformData
import com.example.androidlearn.feature.senior.detail.stage16.proguardData
import com.example.androidlearn.feature.senior.detail.stage16.serializationData
import com.example.androidlearn.feature.senior.detail.stage16.signatureData
import com.example.androidlearn.feature.senior.detail.stage16.versionCompatData

/**
 * 高级工程师笔记详情页（传统 View 实现）。
 * 通过 Intent 接收 stageIndex + topicIndex，查找对应的 NoteData 并渲染。
 *
 * stageIndex 映射（与 SeniorScreen 的 Tab 分组对应）：
 *   7  → stage7  Android 底层原理（Binder、ViewDraw、ActivityStartup、ClassLoader、ArtGc、AndroidInternals）
 *   8  → stage10+stage11 并发深度与虚拟机（Jmm、Synchronized、Cas、ThreadPool、GcAlgorithm、OomAnalysis）
 *   9  → stage5  性能优化体系（AppStartup、RenderOptimization、Anr、PackageSize、StabilityMonitor、Security）
 *   10 → stage6  工程化与架构设计（CleanArch、Pluginization、GradlePlugin、TechDesign、CiCdAdvanced、Kmm）
 *   11 → 混合    系统原理与高阶扩展（ApkBuild、WindowMechanism、Hotfix、Jni、AgpTransform、VersionCompat）
 */
class SeniorNoteDetailActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_STAGE_INDEX = "stage_index"
        private const val EXTRA_TOPIC_INDEX = "topic_index"

        fun start(context: Context, stageIndex: Int, topicIndex: Int) {
            val intent = Intent(context, SeniorNoteDetailActivity::class.java).apply {
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

        val stageIndex = intent.getIntExtra(EXTRA_STAGE_INDEX, 7)
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
        // ── Stage 7 : Android 底层原理 ────────────────────────
        7 -> when (topicIndex) {
            0 -> binderData
            1 -> viewDrawData
            2 -> activityStartupData
            3 -> classLoaderData
            4 -> artGcData
            5 -> androidInternalsData
            else -> null
        }
        // ── Stage 8 : 并发深度与虚拟机 ───────────────────────
        8 -> when (topicIndex) {
            0 -> jmmData
            1 -> synchronizedData
            2 -> casData
            3 -> threadPoolData
            4 -> gcAlgorithmData
            5 -> oomAnalysisData
            else -> null
        }
        // ── Stage 9 : 性能优化体系 ────────────────────────────
        9 -> when (topicIndex) {
            0 -> appStartupData
            1 -> renderOptimizationData
            2 -> anrData
            3 -> packageSizeData
            4 -> stabilityMonitorData
            5 -> securityData
            else -> null
        }
        // ── Stage 10 : 工程化与架构设计 ──────────────────────
        10 -> when (topicIndex) {
            0 -> cleanArchData
            1 -> pluginizationData
            2 -> gradlePluginData
            3 -> techDesignData
            4 -> ciCdAdvancedData
            5 -> kmmData
            else -> null
        }
        // ── Stage 11 : 系统原理与高阶扩展 ────────────────────
        11 -> when (topicIndex) {
            0 -> apkBuildData
            1 -> windowMechanismData
            2 -> hotfixData
            3 -> jniData
            4 -> agpTransformData
            5 -> versionCompatData
            else -> null
        }
        else -> null
    }
}
