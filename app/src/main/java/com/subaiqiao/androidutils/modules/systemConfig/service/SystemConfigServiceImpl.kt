package com.subaiqiao.androidutils.modules.systemConfig.service

import android.content.Context
import com.subaiqiao.androidutils.constant.Constant
import com.subaiqiao.androidutils.modules.systemConfig.dao.SystemConfigDao
import com.subaiqiao.androidutils.modules.systemConfig.entity.SystemConfig
import com.subaiqiao.androidutils.utils.common.CommonUtils

class SystemConfigServiceImpl: SystemConfigService {

    override fun selectByCode(context: Context, code: String): SystemConfig {
        val systemConfigDao = SystemConfigDao(context)
        val networkBaseUrl = "NETWORK_BASE_URL"
        systemConfigDao.openDB()
        val list = systemConfigDao.selectList("is_delete=? and code=?", arrayOf("0", networkBaseUrl))
        println(list.size)
        val systemConfig: SystemConfig
        if (list.isEmpty()) {
            systemConfig = SystemConfig(CommonUtils.createUUID(), networkBaseUrl, Constant.DEFAULT_NETWORK_BASE_URL, 0)
            systemConfigDao.insert(systemConfig)
        } else {
            systemConfig = list[0]
        }
        systemConfigDao.close()
        return systemConfig
    }

    override fun updateById(context: Context, systemConfig: SystemConfig) {
        val systemConfigDao = SystemConfigDao(context)
        systemConfigDao.openDB()
        systemConfigDao.update(systemConfig)
        systemConfigDao.close()
    }
}