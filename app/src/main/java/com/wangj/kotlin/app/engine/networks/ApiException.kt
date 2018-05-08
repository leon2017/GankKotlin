package com.wangj.kotlin.app.engine.networks

/**
 * 当前类注释：自定义网络异常</p>
 * Author: LeonWang </p>
 * Created: 2018/5/3 - 15:30 </p>
 * Desc: </P>
 * Copyright (C)  2018 lijiawangjun@gmail.com
 */
class ApiException : RuntimeException {

    var code: Int

    constructor(code: Int) {
        this.code = code
    }

    constructor(message: String, code: Int) : super(message) {
        this.code = code
    }

    companion object {
        val NET_ERROR = 1000
        val DATA_EMPTY = 1001
    }
}