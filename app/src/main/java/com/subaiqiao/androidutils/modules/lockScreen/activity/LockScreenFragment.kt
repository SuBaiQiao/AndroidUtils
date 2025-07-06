package com.subaiqiao.androidutils.modules.lockScreen.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.subaiqiao.androidutils.R
import com.subaiqiao.androidutils.api.RetrofitClient
import com.subaiqiao.androidutils.constant.Constant
import com.subaiqiao.androidutils.modules.systemConfig.service.SystemConfigServiceImpl
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [LockScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LockScreenFragment : Fragment() {

    companion object {
        private const val TAG = "锁屏页面"
    }
    // 在一个Activity内部
    private val lifecycleOwner: LifecycleOwner = this

    private lateinit var context: Context
    private lateinit var view: View

    private val systemConfigServiceImpl: SystemConfigServiceImpl = SystemConfigServiceImpl()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_lock_screen, container, false)
        context = requireContext()
        init()
        return view
    }

    private fun init() {
        val mainActivityLockScreenBtn: Button = view.findViewById(R.id.main_activity_lock_screen_btn)
        val mainActivityLocationIpText: EditText = view.findViewById(R.id.main_activity_location_ip_text)
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
            Toast.makeText(context, "发送锁定屏幕请求", Toast.LENGTH_SHORT).show()
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

    private fun initNetworkBaseUrl(): String {
        val systemConfig = systemConfigServiceImpl.selectByCode(context, "NETWORK_BASE_URL")
        Log.d(TAG, "读取到网络连接地址: " + systemConfig.value)
        return systemConfig.value
    }

}