package com.wangj.kotlin.app.base.mvp

/**
 * 当前类注释：</p>
 * Author: LeonWang </p>
 * Created: 2018/5/3 - 14:01 </p>
 * Desc: </P>
 * Copyright (C)  2018 lijiawangjun@gmail.com
 */
abstract class BasePresenter<out V : IBaseView, out M : IBaseModel>(
        protected val mView: V,
        protected val mModel: M
)


//暂时还没搞懂kotlin抽象对象的 泛型获取的问题
//{
//    fun <T> applySchedulers(): ObservableTransformer<T, T> {
//        return ObservableTransformer { upstream ->
//            upstream
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//        }
//    }
//}