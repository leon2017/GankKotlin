package com.wangj.kotlin.app.engine.networks

import io.reactivex.Observer

/**
 * 当前类注释：</p>
 * Author: LeonWang </p>
 * Created: 2018/5/3 - 16:11 </p>
 * Desc: </P>
 * Copyright (C)  2018 lijiawangjun@gmail.com
 */
abstract class ResponseObserver<T> :Observer<T> {

    override fun onError(e: Throwable) {
        if (e is ApiException) {
            error(e)
        }else{
            error(ApiException(e.message!!,ApiException.NET_ERROR))
        }
    }

    abstract fun error(e: ApiException)
}