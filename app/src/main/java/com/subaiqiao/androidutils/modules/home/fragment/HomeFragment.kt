package com.subaiqiao.androidutils.modules.home.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.subaiqiao.androidutils.R
import com.subaiqiao.androidutils.modules.app.transactionEdit.activity.TransactionEditActivity
import com.subaiqiao.androidutils.modules.camera.activity.CameraActivity
import com.subaiqiao.androidutils.modules.home.adapter.HomeRecyclerViewAdapter
import com.subaiqiao.androidutils.modules.home.placeholder.PlaceholderContent
import com.subaiqiao.androidutils.modules.privacyData.contactPerson.ContactPersonActivity
import com.subaiqiao.androidutils.modules.privacyData.pictureBackup.activity.PictureBackupActivity
import com.subaiqiao.androidutils.modules.privacyData.wifi.WifiManagerActivity
import com.subaiqiao.androidutils.modules.systemConfig.service.SystemConfigServiceImpl
import com.subaiqiao.androidutils.modules.videoPlayer.activity.VideoPlayerActivity
import java.io.FileOutputStream

/**
 * A fragment representing a list of Items.
 */
class HomeFragment : Fragment() {

    private val TAG = "首页"


    /**
     * 页面上下文
     */
    private lateinit var context: Context

    /**
     * 页面视图
     */
    private lateinit var view: View

    private var columnCount = 1

    private val REQUEST_CODE_IMPORT_DATABASE = 1001

    private val systemConfigServiceImpl: SystemConfigServiceImpl = SystemConfigServiceImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: ")
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        context = requireContext()
        this.view = view
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = HomeRecyclerViewAdapter(PlaceholderContent.ITEMS)
            }
        }
        init()
        return view
    }

    private fun init() {
        // 视频播放
        val mainActivityGotoVideoPlayerBtn: MaterialButton = view.findViewById(R.id.main_activity_goto_video_player_btn)
        // 拍照
        val mainActivityCameraBtn: MaterialButton = view.findViewById(R.id.main_activity_camera_btn)
        // 权限授予
        val mainActivityPermissoinsBtn: MaterialButton = view.findViewById(R.id.main_activity_permissoins_btn)
        // 通讯录读取
        val mainActivityContactPersonBtn: MaterialButton = view.findViewById(R.id.main_activity_contact_person_btn)
        // 无线网络信息
        val mainActivityWifiManagerBtn: MaterialButton = view.findViewById(R.id.main_activity_wifi_manager_btn)
        // 照片信息备份按钮
        val mainActivityPictureBackupBtn: MaterialButton = view.findViewById(R.id.main_activity_picture_backup_btn)
        // 数据库导出
        val mainActivityExportDbBtn: MaterialButton = view.findViewById(R.id.main_activity_export_db_btn)
        // 数据库导入
        val mainActivityImportDbBtn: MaterialButton = view.findViewById(R.id.main_activity_import_db_btn)
        mainActivityGotoVideoPlayerBtn.setOnClickListener {
            startActivity(Intent(context, VideoPlayerActivity::class.java))
        }
        mainActivityCameraBtn.setOnClickListener {
            startActivity(Intent(context, CameraActivity::class.java))
        }
        mainActivityContactPersonBtn.setOnClickListener {
            startActivity(Intent(context, ContactPersonActivity::class.java))
        }
        mainActivityWifiManagerBtn.setOnClickListener {
            startActivity(Intent(context, TransactionEditActivity::class.java))
        }
        mainActivityPictureBackupBtn.setOnClickListener {
            startActivity(Intent(context, PictureBackupActivity::class.java))
        }
        mainActivityExportDbBtn.setOnClickListener {
            // 数据库导出
            systemConfigServiceImpl.exportDatabase(requireContext())
        }
        mainActivityImportDbBtn.setOnClickListener {
            // 数据库导入
            // 启动系统文件选择器
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/x-sqlite3", "application/octet-stream"))
            }
            startActivityForResult(intent, REQUEST_CODE_IMPORT_DATABASE)
        }
        mainActivityPermissoinsBtn.setOnClickListener {
            XXPermissions.with(this)
                // 申请单个权限
                .permission(Permission.RECORD_AUDIO)
                .permission(Permission.CAMERA)
                .permission(Permission.Group.STORAGE)
                // 申请多个权限
                .permission(Permission.Group.CALENDAR)
                // 设置权限请求拦截器（局部设置）
                //.interceptor(new PermissionInterceptor())
                // 设置不触发错误检测机制（局部设置）
                //.unchecked()
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                        if (!allGranted) {
                            Toast.makeText(context, "获取部分权限成功，但部分权限未正常授予", Toast.LENGTH_SHORT).show()
                            return
                        }
                        Toast.makeText(context, "获取录音和日历权限成功", Toast.LENGTH_SHORT).show()
                    }
                    override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                        if (doNotAskAgain) {
                            Toast.makeText(context, "被永久拒绝授权，请手动授予录音和日历权限", Toast.LENGTH_SHORT).show()
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(context, permissions)
                        } else {
                            Toast.makeText(context, "获取录音和日历权限失败", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMPORT_DATABASE && resultCode == Activity.RESULT_OK) {
            data?.data?.also { uri ->
                importDatabase(requireContext(), uri)
            }
        }
    }

    private fun importDatabase(context: Context, uri: Uri) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val dbFile = context.getDatabasePath("androidUtils.db")
            Log.d(TAG, "目标数据库路径: ${dbFile.absolutePath}")
            // 删除旧数据库（如果存在）
            if (dbFile.exists()) {
                Log.d(TAG, "importDatabase: 删除原始数据库")
                val result = dbFile.delete()
                Log.d(TAG, "importDatabase: 数据库删除结果 + $result")
            }
            // 创建父目录（如果不存在）
            dbFile.parentFile?.mkdirs()
            // 写入新数据库文件
            val outputStream = FileOutputStream(dbFile)
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream!!.read(buffer) > 0) {
                length = inputStream.read(buffer)
                outputStream.write(buffer, 0, length)
            }
            inputStream.close()
            outputStream.flush()
            outputStream.close()
            Toast.makeText(context, "数据库导入成功", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "数据库导入失败: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}