package com.wangj.kotlin.app.engine.networks

/**
 * 当前类注释：</p>
 * Author: LeonWang </p>
 * Created: 2018/5/7 - 17:43 </p>
 * Desc: </P>
 * Copyright (C)  2018 lijiawangjun@gmail.com
 */
class ApiService {

    companion object {
        val GANK_BASE_URL = "http://gank.io/api/"
        fun gankApi(): GankApi {
            return ApiCreate.instance(GANK_BASE_URL).mRetrofit!!.create(GankApi::class.java)
        }
    }
}