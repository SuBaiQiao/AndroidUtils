package com.subaiqiao.androidutils.modules.systemConfig.service

import android.content.Context
import com.subaiqiao.androidutils.modules.systemConfig.entity.SystemConfig

interface SystemConfigService {
    fun selectByCode(context: Context, code: String): SystemConfig

    fun updateById(context: Context, systemConfig: SystemConfig)

    fun exportDatabase(context: Context)
}