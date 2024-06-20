package com.subaiqiao.androidutils.modules.systemConfig.entity

class SystemConfig {
    var id: String = ""
    var code: String = ""
    var value: String = ""
    var isDelete: Int = 0

    constructor()

    constructor(id: String, code: String, value: String, isDelete: Int) {
        this.id = id
        this.code = code
        this.value = value
        this.isDelete = isDelete
    }

}