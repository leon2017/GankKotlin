package com.wangj.kotlin.app.ui.gank.categoryblog

import com.wangj.kotlin.app.engine.networks.ApiService
import com.wangj.kotlin.app.entity.BaseEntity
import com.wangj.kotlin.app.entity.GankCategoryEntity
import io.reactivex.Observable

/**
 * 当前类注释：</p>
 * Author: LeonWang </p>
 * Created: 2018/5/7 - 18:13 </p>
 * Desc: </P>
 * Copyright (C)  2018 lijiawangjun@gmail.com
 */
class CategoryModel : CategoryContract.Model {

    lateinit var type: String
    var page: Int = 1

    override fun setParams(params: String) {
        this.type = params
    }

    override fun setCurPage(page: Int) {
        this.page = page
    }

    override fun getCurPage(): Int {
        return page
    }

    override fun requestCategoryData(): Observable<BaseEntity<ArrayList<GankCategoryEntity>>> {
        return ApiService
                .gankApi()
                .category(this.type, this.page.toString())
    }
}