package com.subaiqiao.androidutils.utils.common

import java.util.UUID

class CommonUtils {
    companion object {

        // 单例实例（可选）：适用于 nodeId 固定的情况
        private val idGenerator = SnowflakeIdGenerator(nodeId = 1)

        fun createUUID() = UUID.randomUUID().toString().replace("-", "")

        // 封装雪花ID生成方法
        @JvmStatic
        fun generateSnowflakeId(): Long {
            return idGenerator.nextId()
        }

        // 如果需要支持不同 nodeId，可以带参数
        fun generateSnowflakeId(nodeId: Long): Long {
            val generator = SnowflakeIdGenerator(nodeId)
            return generator.nextId()
        }
    }
}