package com.subaiqiao.androidutils.utils.common

import java.util.UUID

class CommonUtils {
    companion object {
        fun createUUID() = UUID.randomUUID().toString().replace("-", "")
    }
}