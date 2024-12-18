package com.subaiqiao.androidutils

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.media3.common.util.ColorParser
import com.roughike.bottombar.BottomBar
import com.subaiqiao.androidutils.modules.home.fragment.HomeFragment
import com.subaiqiao.androidutils.modules.lockScreen.activity.LockScreenFragment
import com.subaiqiao.androidutils.modules.setting.activity.SettingsFragment


class MainActivity : AppCompatActivity() {

    private val fragments = HashMap<Int, Fragment>() // 用于保存 Fragments 的映射
    private lateinit var bottomBar: BottomBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_layout)
        bottomBar = findViewById(R.id.bottomBar)
        fragments[0] = LockScreenFragment()
        fragments[1] = HomeFragment()
        fragments[2] = SettingsFragment()
        bottomBar.setDefaultTab(R.id.tab_2)
        bottomBar.setActiveTabColor(Color.parseColor("#395cf5"))
        bottomBar.setOnTabSelectListener {tableId ->
            println(tableId)
            if (tableId == R.id.tab_1) {
                showFragment(0)
            }
            if (tableId == R.id.tab_2) {
                showFragment(1)
            }
            if (tableId == R.id.tab_3) {
                showFragment(2)
            }
        }
    }

    private fun showFragment(tabId: Int) {
        val transaction = supportFragmentManager.beginTransaction();
        for ((key, fragment) in fragments) {
            if (key != tabId) {
                transaction.hide(fragment)
            }
        }
        if (fragments[tabId] != null && !fragments[tabId]!!.isAdded) {
            transaction.add(R.id.contentContainer, fragments[tabId]!!, "" + tabId)
        }
        transaction.show(fragments[tabId]!!).commit()
    }
}
