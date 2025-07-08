package com.subaiqiao.androidutils.utils.queue

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object SerialTaskQueue {
    private val mutex = Mutex()
    private var job: Job = Job()

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + job)

    fun enqueue(task: suspend () -> Unit) {
        scope.launch {
            mutex.withLock {
                task()
            }
        }
    }

    fun cancelAllTasks() {
        job.cancel()
        job = Job() // 重新初始化 Job，以便后续任务可继续提交
    }
}
