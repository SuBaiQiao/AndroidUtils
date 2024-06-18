package com.subaiqiao.androidutils

import android.content.Context
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
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    // 在一个Activity内部
    private val lifecycleOwner: LifecycleOwner = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_layout)
        val mainActivityLockScreenBtn: Button = findViewById(R.id.main_activity_lock_screen_btn)
        val mainActivityLocationIpText: EditText = findViewById(R.id.main_activity_location_ip_text)
        Constant.BASE_URL  = "http://" + mainActivityLocationIpText.text + "/"
        mainActivityLocationIpText.addTextChangedListener((object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
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
    }
}
