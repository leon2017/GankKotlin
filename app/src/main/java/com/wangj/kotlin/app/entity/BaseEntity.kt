package com.wangj.kotlin.app.entity

/**
 * 当前类注释：</p>
 * Author: LeonWang </p>
 * Created: 2018/5/3 - 15:08 </p>
 * Desc: </P>
 * Copyright (C)  2018 lijiawangjun@gmail.com
 */
data class BaseEntity<T>(
        var error: Boolean,
        var results: T)