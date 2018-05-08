package com.wangj.kotlin.app.engine.networks

import com.trello.rxlifecycle2.LifecycleTransformer
import com.wangj.kotlin.app.entity.BaseEntity
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

/**
 * 当前类注释：</p>
 * Author: LeonWang </p>
 * Created: 2018/5/3 - 15:23 </p>
 * Desc: </P>
 * Copyright (C)  2018 lijiawangjun@gmail.com
 */
class HttpTransFormer<T>(private val mLifecycle: LifecycleTransformer<BaseEntity<T>>) :
        ObservableTransformer<BaseEntity<T>, BaseEntity<T>> {
    override fun apply(upstream: Observable<BaseEntity<T>>): ObservableSource<BaseEntity<T>> {
        return upstream
                .map { response ->
                    if (response.error) {
                        throw ApiException("加载失败", 300)
                    }
                    response
                }
                .compose<BaseEntity<T>>(mLifecycle)
    }
}
