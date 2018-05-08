package com.wangj.kotlin.app.ui.gank.categoryblog

import com.wangj.kotlin.app.base.mvp.BasePresenter
import com.wangj.kotlin.app.base.mvp.IBaseFragmentView
import com.wangj.kotlin.app.base.mvp.IBaseModel
import com.wangj.kotlin.app.engine.networks.ApiException
import com.wangj.kotlin.app.entity.BaseEntity
import com.wangj.kotlin.app.entity.GankCategoryEntity
import io.reactivex.Observable

/**
 * 当前类注释：</p>
 * Author: LeonWang </p>
 * Created: 2018/5/7 - 17:20 </p>
 * Desc: </P>
 * Copyright (C)  2018 lijiawangjun@gmail.com
 */
interface CategoryContract {

    interface Model : IBaseModel {
        /**
         * 设置请求参数
         */
        fun setParams(params: String)

        fun setCurPage(page: Int)
        fun getCurPage(): Int
        fun requestCategoryData(): Observable<BaseEntity<ArrayList<GankCategoryEntity>>>
    }

    interface View : IBaseFragmentView {
        fun setCategoryList(page: Int, list: ArrayList<GankCategoryEntity>)
        fun loadError(e: ApiException)
        fun loadComplete(page: Int)
    }

    abstract class Presenter(mView: View, mModel: Model) : BasePresenter<View, Model>(mView, mModel) {
        abstract fun startRequstList()
    }

}