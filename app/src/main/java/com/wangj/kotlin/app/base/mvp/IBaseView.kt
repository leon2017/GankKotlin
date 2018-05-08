package com.wangj.kotlin.app.base.mvp

import com.wangj.kotlin.app.engine.networks.HttpTransFormer

/**
 * 当前类注释：</p>
 * Author: LeonWang </p>
 * Created: 2018/5/3 - 13:27 </p>
 * Desc: </P>
 * Copyright (C)  2018 lijiawangjun@gmail.com
 */
interface IBaseView {

    fun <T> bindResponseTransformer():HttpTransFormer<T>
}