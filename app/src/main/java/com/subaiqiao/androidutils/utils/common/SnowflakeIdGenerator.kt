package com.subaiqiao.androidutils.utils.common

/**
 * 雪花算法生成器
 */
class SnowflakeIdGenerator(private val nodeId: Long) {
    // 节点ID范围限制
    private val maxNodeId = 1023L
    // 时间戳起始点（自定义纪元），例如：2023-01-01
    private val nodeIdBits = 10
    private val sequenceBits = 12
    private val maxSequence = (-1L shl sequenceBits) - 1 // 最大序列号值

    private var lastTimestamp = -1L
    private var sequence = 0L

    init {
        require(nodeId >= 0 && nodeId <= maxNodeId) { "节点ID超出范围" }
    }

    @Synchronized
    fun nextId(): Long {
        var timestamp = System.currentTimeMillis()

        if (timestamp < lastTimestamp) {
            throw RuntimeException("时钟回拨")
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) and maxSequence
            if (sequence == 0L) {
                timestamp = tilNextMillis(lastTimestamp)
            }
        } else {
            sequence = 0
        }

        lastTimestamp = timestamp

        return (timestamp shl (nodeIdBits + sequenceBits)) or (nodeId shl sequenceBits) or sequence
    }

    private fun tilNextMillis(lastTimestamp: Long): Long {
        var timestamp = System.currentTimeMillis()
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis()
        }
        return timestamp
    }
}



