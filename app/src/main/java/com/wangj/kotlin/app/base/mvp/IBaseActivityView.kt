package com.wangj.kotlin.app.base.mvp

import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.android.ActivityEvent

/**
 * 当前类注释：</p>
 * Author: LeonWang </p>
 * Created: 2018/5/3 - 14:11 </p>
 * Desc: </P>
 * Copyright (C)  2018 lijiawangjun@gmail.com
 */
interface IBaseActivityView : IBaseView {

    fun <T> bindUnitEvent(event: ActivityEvent): LifecycleTransformer<T>
}