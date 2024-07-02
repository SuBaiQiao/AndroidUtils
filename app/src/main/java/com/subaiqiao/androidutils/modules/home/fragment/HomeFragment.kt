package com.subaiqiao.androidutils.modules.home.fragment

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.subaiqiao.androidutils.R
import com.subaiqiao.androidutils.modules.camera.activity.CameraActivity
import com.subaiqiao.androidutils.modules.home.adapter.HomeRecyclerViewAdapter
import com.subaiqiao.androidutils.modules.home.placeholder.PlaceholderContent
import com.subaiqiao.androidutils.modules.videoPlayer.activity.VideoPlayerActivity

/**
 * A fragment representing a list of Items.
 */
class HomeFragment : Fragment() {

    private lateinit var context: Context
    private lateinit var view: View


    private var columnCount = 1

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

    fun init() {
        val mainActivityGotoVideoPlayerBtn: Button = view.findViewById(R.id.main_activity_goto_video_player_btn)
        val mainActivityCameraBtn: Button = view.findViewById(R.id.main_activity_camera_btn)
        val mainActivityPermissoinsBtn: Button = view.findViewById(R.id.main_activity_permissoins_btn)
        mainActivityGotoVideoPlayerBtn.setOnClickListener {
            startActivity(Intent(context, VideoPlayerActivity::class.java))
        }
        mainActivityCameraBtn.setOnClickListener {
            startActivity(Intent(context, CameraActivity::class.java))
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

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}