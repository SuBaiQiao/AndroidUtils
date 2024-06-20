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
import com.subaiqiao.androidutils.api.RetrofitClient
import com.subaiqiao.androidutils.constant.Constant
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
    }

    private fun initNetworkBaseUrl(): String {
        val systemConfig = systemConfigServiceImpl.selectByCode(this, "NETWORK_BASE_URL")
        println("读取到网络连接地址：" + systemConfig.value)
        return systemConfig.value
    }
}
