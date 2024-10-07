package com.subaiqiao.androidutils.modules.privacyData.wifi

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.subaiqiao.androidutils.R
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class WifiManagerActivity : AppCompatActivity() {


    lateinit var wifiManagerLayoutText2: TextView
    lateinit var wifiManagerLayoutText4: TextView
    lateinit var wifiManagerLayoutText6: TextView

    val GRANTED = PackageManager.PERMISSION_GRANTED
    val ACCESS_FINE_LOCATION_PMSN = Manifest.permission.ACCESS_FINE_LOCATION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi_manager)
        wifiManagerLayoutText2 = findViewById(R.id.wifi_manager_layout_text2)
        wifiManagerLayoutText4 = findViewById(R.id.wifi_manager_layout_text4)
        wifiManagerLayoutText6 = findViewById(R.id.wifi_manager_layout_text6)
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION_PMSN) != GRANTED) {
            // 没有权限,去申请
            ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION_PMSN), 1)
        } else {
            // 已经有读取通讯录的权限
            getWifiInfo()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == GRANTED) {
                    getWifiInfo()
                } else {
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun getWifiInfo() {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+ 获取连接信息方式不同
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

            if (networkCapabilities != null && networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                val wifiInfo = wifiManager.connectionInfo
                val ssid = wifiInfo.ssid
                val formattedIpAddress = formatIpAddress(wifiInfo.ipAddress)
                val linkSpeed = wifiInfo.linkSpeed
                wifiManagerLayoutText2.text = ssid
                wifiManagerLayoutText4.text = formattedIpAddress
                wifiManagerLayoutText6.text = linkSpeed.toString() + " Mbps"
                Toast.makeText(this, "Connected to: $ssid, IP: $formattedIpAddress, Speed: ${linkSpeed}Mbps", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "No Wi-Fi connection", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Android 9.0 (API 28) 及以下版本
            val wifiInfo = wifiManager.connectionInfo
            val ssid = wifiInfo.ssid
            val ipAddress = wifiInfo.ipAddress
            val linkSpeed = wifiInfo.linkSpeed
            wifiManagerLayoutText2.text = ssid
            wifiManagerLayoutText4.text = ipAddress.toString()
            wifiManagerLayoutText6.text = linkSpeed.toString() + " Mbps"
            Toast.makeText(this, "Connected to: $ssid, IP: $ipAddress, Speed: ${linkSpeed}Mbps", Toast.LENGTH_LONG).show()
        }
    }

    // 将IP地址转换为常见的格式
    fun formatIpAddress(ip: Int): String {
        return String.format(
            "%d.%d.%d.%d",
            ip and 0xff,
            ip shr 8 and 0xff,
            ip shr 16 and 0xff,
            ip shr 24 and 0xff
        )
    }
}