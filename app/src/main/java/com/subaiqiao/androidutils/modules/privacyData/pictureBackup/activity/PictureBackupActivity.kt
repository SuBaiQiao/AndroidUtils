package com.subaiqiao.androidutils.modules.privacyData.pictureBackup.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.subaiqiao.androidutils.R
import com.subaiqiao.androidutils.modules.privacyData.pictureBackup.adapter.PictureItemAdapter
import com.subaiqiao.androidutils.modules.privacyData.pictureBackup.entity.PictureItem

class PictureBackupActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var backButton: ImageButton
    private lateinit var refreshButton: ImageButton
    private lateinit var uploadButton: Button
    private lateinit var adapter: PictureItemAdapter
    private lateinit var rootLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture_backup)

        // 绑定控件
        rootLayout = findViewById(R.id.main)
        recyclerView = findViewById(R.id.recycler_view)
        refreshButton = findViewById(R.id.refresh_button)
        uploadButton = findViewById(R.id.upload_button)
        backButton = findViewById(R.id.back_button)

        // 初始化适配器
        adapter = PictureItemAdapter { selectedCount ->
            // 控制上传按钮显示与隐藏
            if (selectedCount > 0) {
                uploadButton.visibility = View.VISIBLE
                updateRecyclerViewHeight()
            } else {
                uploadButton.visibility = View.GONE
                updateRecyclerViewHeight()
            }
        }

        // 设置 RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // 模拟数据
        val mockData = (1..10).map {
            PictureItem(
                id = it,
                name = "图片_$it.jpg",
                createTime = "2024-05-$it",
                thumbnailResId = R.drawable.ic_launcher_foreground // 使用你的缩略图资源
            )
        }

        // 提供给 Adapter
        adapter.submitList(mockData)

        // 刷新按钮点击事件（可扩展）
        refreshButton.setOnClickListener {
            // 可重新加载数据
        }
        // 返回按钮点击事件
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun updateRecyclerViewHeight() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(rootLayout)

        if (uploadButton.visibility == View.VISIBLE) {
            constraintSet.connect(
                R.id.recycler_view,
                ConstraintSet.BOTTOM,
                R.id.upload_button,
                ConstraintSet.TOP,
                resources.getDimensionPixelSize(R.dimen.margin_8dp)
            )
        } else {
            constraintSet.connect(
                R.id.recycler_view,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM,
                resources.getDimensionPixelSize(R.dimen.margin_8dp)
            )
        }

        constraintSet.applyTo(rootLayout)
    }
}