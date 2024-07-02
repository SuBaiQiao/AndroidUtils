package com.subaiqiao.androidutils.modules.setting.activity

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.subaiqiao.androidutils.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}