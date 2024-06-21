package com.subaiqiao.androidutils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.subaiqiao.androidutils.api.RetrofitClient
import com.subaiqiao.androidutils.constant.Constant
import com.subaiqiao.androidutils.modules.camera.activity.CameraActivity
import com.subaiqiao.androidutils.modules.systemConfig.service.SystemConfigServiceImpl
import com.subaiqiao.androidutils.modules.videoPlayer.activity.VideoPlayerActivity
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    // 在一个Activity内部
    private val lifecycleOwner: LifecycleOwner = this

    private val context: Context = this

    private val systemConfigServiceImpl: SystemConfigServiceImpl = SystemConfigServiceImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_layout)
        val mainActivityLockScreenBtn: Button = findViewById(R.id.main_activity_lock_screen_btn)
        val mainActivityGotoVideoPlayerBtn: Button = findViewById(R.id.main_activity_goto_video_player_btn)
        val mainActivityCameraBtn: Button = findViewById(R.id.main_activity_camera_btn)
        val mainActivityPermissoinsBtn: Button = findViewById(R.id.main_activity_permissoins_btn)
        val mainActivityLocationIpText: EditText = findViewById(R.id.main_activity_location_ip_text)
        val networkBaseUrl = initNetworkBaseUrl()
        mainActivityLocationIpText.setText(networkBaseUrl)
        Constant.BASE_URL = "http://$networkBaseUrl/"
        RetrofitClient.buildRetrofit()
        mainActivityLocationIpText.addTextChangedListener((object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                val systemConfig = systemConfigServiceImpl.selectByCode(context, "NETWORK_BASE_URL")
                systemConfig.value = p0.toString()
                systemConfigServiceImpl.updateById(context, systemConfig)
                Constant.BASE_URL  = "http://" + p0.toString() + "/"
                RetrofitClient.buildRetrofit()
            }

        }))
        mainActivityLockScreenBtn.setOnClickListener {
            Toast.makeText(this, "发送锁定屏幕请求", Toast.LENGTH_SHORT).show()
            lifecycleOwner.lifecycleScope.launch {
                try {
                    var lockScreen: com.subaiqiao.androidutils.api.Result = RetrofitClient.apiService.lockScreen()
                    if ("ok".equals(lockScreen.result)) {
                        Toast.makeText(lifecycleOwner as Context, "锁定成功", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(lifecycleOwner as Context, "锁定失败！" + lockScreen.result, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        mainActivityGotoVideoPlayerBtn.setOnClickListener {
            startActivity(Intent(this, VideoPlayerActivity::class.java))
        }
        mainActivityCameraBtn.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
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

    private fun initNetworkBaseUrl(): String {
        val systemConfig = systemConfigServiceImpl.selectByCode(this, "NETWORK_BASE_URL")
        println("读取到网络连接地址：" + systemConfig.value)
        return systemConfig.value
    }
}
