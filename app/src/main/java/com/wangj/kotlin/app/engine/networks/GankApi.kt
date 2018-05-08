package com.wangj.kotlin.app.engine.networks

import com.wangj.kotlin.app.entity.BaseEntity
import com.wangj.kotlin.app.entity.GankCategoryEntity
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * 当前类注释：gank api</p>
 * Author: LeonWang </p>
 * Created: 2018/5/7 - 17:47 </p>
 * Desc: </P>
 * Copyright (C)  2018 lijiawangjun@gmail.com
 */
interface GankApi {


    @GET("data/{type}/20/{page}")
    fun category(@Path("type") type: String,
                 @Path("page") page: String): Observable<BaseEntity<ArrayList<GankCategoryEntity>>>

}