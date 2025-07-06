package com.subaiqiao.androidutils.modules.privacyData.pictureBackup.activity

import android.Manifest
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.subaiqiao.androidutils.R
import com.subaiqiao.androidutils.modules.privacyData.pictureBackup.adapter.PictureItemAdapter
import com.subaiqiao.androidutils.modules.privacyData.pictureBackup.entity.MediaItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PictureBackupActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "图片备份"
    }

    private val DATE_FORMATTER = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    private lateinit var recyclerView: RecyclerView
    private lateinit var backButton: ImageButton
    private lateinit var refreshButton: ImageButton
    private lateinit var uploadButton: Button
    private lateinit var adapter: PictureItemAdapter
    private lateinit var rootLayout: ConstraintLayout
    private lateinit var selectAllFab: ImageButton

    private val REQUEST_CODE_PERMISSION = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture_backup)
        // 绑定控件
        rootLayout = findViewById(R.id.main)
        recyclerView = findViewById(R.id.recycler_view)
        refreshButton = findViewById(R.id.refresh_button)
        uploadButton = findViewById(R.id.upload_button)
        backButton = findViewById(R.id.back_button)
        selectAllFab = findViewById(R.id.select_all_fab)

        // 初始化适配器
        adapter = PictureItemAdapter(
            onSelectedCountChanged = { selectedCount ->
                // 控制上传按钮显示与隐藏
                Log.d(TAG, "当前选中数量: $selectedCount")
                uploadButton.visibility = if (selectedCount > 0) View.VISIBLE else View.GONE
                updateRecyclerViewHeight()
                updateSelectAllFab()
            },
            onItemCheckChanged = { position, isChecked ->
                // 可以在这里做额外处理（如刷新“全选”按钮状态）
                Log.d("PictureItemAdapter", "位置 $position 状态变为 $isChecked")
                updateSelectAllFab()
            }
        )

        // 设置 RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        Log.d(TAG, "准备检查权限")
        requestMediaPermissions()

        // 刷新按钮点击事件（可扩展）
        refreshButton.setOnClickListener {
            // 可重新加载数据
            loadMediaItems()
        }
        // 返回按钮点击事件
        backButton.setOnClickListener {
            finish()
        }
        selectAllFab.setOnClickListener {
            adapter.toggleSelectAll()
        }
    }

    private fun updateSelectAllFab() {
        if (adapter.isAllSelected()) {
            Log.d(TAG, "全选")
            selectAllFab.setImageResource(R.drawable.ic_uncheck_all) // 使用“取消全选”图标
        } else {
            Log.d(TAG, "非全选")
            selectAllFab.setImageResource(R.drawable.ic_check_all)
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

    private fun loadMediaItems() {
        Log.d(TAG, "开始读取媒体文件...")
        val allMediaItems = mutableListOf<MediaItem>()
        val projection = arrayOf(
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.DATE_ADDED,
            MediaStore.MediaColumns.MIME_TYPE
        )

        // 图片
        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED)
            val mimeColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val dateAddedMillis = cursor.getLong(dateColumn) * 1000L
                // ✅ 格式化时间
                val date =
                    DATE_FORMATTER.format(Date(dateAddedMillis))
                val mimeType = cursor.getString(mimeColumn)
                val uri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                        .toString()

                allMediaItems.add(MediaItem(id, uri, name, dateAddedMillis, date, mimeType))
            }
        }

        // 视频
        contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED)
            val mimeColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val dateAddedMillis = cursor.getLong(dateColumn) * 1000L
                // ✅ 格式化时间
                val date =
                    DATE_FORMATTER.format(Date(dateAddedMillis))
                val mimeType = cursor.getString(mimeColumn)
                val uri =
                    ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                        .toString()
                allMediaItems.add(MediaItem(id, uri, name, dateAddedMillis, date, mimeType, true))
            }
        }
        // ✅ 按照日期降序排列：最新在前
        val sortedList = allMediaItems.sortedByDescending { it.dateAddedMillis }
        Log.d(TAG, "读取到 ${sortedList.size} 条图片、视频数据")
        adapter.submitList(sortedList)
    }

    private fun requestMediaPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            // Android 14+ 使用 MANAGE_EXTERNAL_STORAGE
            if (Environment.isExternalStorageManager()) {
                // ✅ 已经拥有权限，直接加载数据
                Log.d(TAG, "已有 MANAGE_EXTERNAL_STORAGE 权限")
                loadMediaItems()
            } else {
                // Android 14+ 请求 MANAGE_EXTERNAL_STORAGE
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                    data = Uri.parse("package:$packageName")
                }
                try {
                    startActivityForResult(intent, REQUEST_CODE_PERMISSION)
                } catch (e: Exception) {
                    // fallback to settings
                    val uri = Uri.fromParts("package", packageName, null)
                    val fallbackIntent =
                        Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION).apply {
                            data = uri
                        }
                    startActivityForResult(fallbackIntent, REQUEST_CODE_PERMISSION)
                }
            }
        } else {
            // 其他版本继续使用旧权限逻辑
            val permission = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> Manifest.permission.READ_MEDIA_IMAGES
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> Manifest.permission.READ_EXTERNAL_STORAGE
                else -> Manifest.permission.READ_EXTERNAL_STORAGE
            }

            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(permission),
                    REQUEST_CODE_PERMISSION
                )
            } else {
                loadMediaItems()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionsResult 被调用，requestCode=$requestCode")

        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "权限已授予，开始加载媒体数据")
                loadMediaItems()
            } else {
                Log.e(TAG, "权限被拒绝，请提示用户开启权限")
                // 可选：弹出对话框引导用户去设置页开启权限
                showPermissionDeniedDialog()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    Log.d(TAG, "MANAGE_EXTERNAL_STORAGE 权限已授予")
                    loadMediaItems()
                } else {
                    Log.e(TAG, "MANAGE_EXTERNAL_STORAGE 权限被拒绝")
                    showPermissionDeniedDialog()
                }
            }
        }
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("权限被拒绝")
            .setMessage("需要访问您的照片和视频才能继续，请前往设置开启权限。")
            .setPositiveButton("去设置") { _, _ ->
                val intent = Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", packageName, null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
            }
            .setNegativeButton("取消", null)
            .show()
    }

}