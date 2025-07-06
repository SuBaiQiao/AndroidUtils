package com.subaiqiao.androidutils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.subaiqiao.androidutils.modules.home.fragment.HomeFragment
import com.subaiqiao.androidutils.modules.lockScreen.activity.LockScreenFragment
import com.subaiqiao.androidutils.modules.setting.activity.SettingsFragment


class MainActivity : AppCompatActivity() {

    /**
     * 用于保存 Fragments 的映射
     */
    private val fragments = HashMap<Int, Fragment>()
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_layout)
        bottomNav = findViewById(R.id.bottom_navigation)
        fragments[0] = LockScreenFragment()
        fragments[1] = HomeFragment()
        fragments[2] = SettingsFragment()

        bottomNav.selectedItemId = R.id.navigation_home
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_lock_screen -> {
                    // 切换到锁屏
                    showFragment(0)
                    true
                }
                R.id.navigation_home -> {
                    // 切换到首页
                    showFragment(1)
                    true
                }
                R.id.navigation_setting -> {
                    // 切换到设置
                    showFragment(2)
                    true
                }
                else -> false
            }
        }
        // 初始加载首页
        if (savedInstanceState == null) {
            showFragment(1)
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
