package com.wangj.kotlin.app.engine.networks

import com.wangj.kotlin.app.engine.App
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * 当前类注释：RetrofitClient </p>
 * Author: LeonWang </p>
 * Created: 2018/5/3 - 14:32 </p>
 * Desc: </P>
 * Copyright (C)  2018 lijiawangjun@gmail.com
 */
class ApiCreate private constructor(val baseUrl: String) {

    private val DEFAULT_TIMEOUT: Long = 15
    //    private val BASE_URL = "http://www.gank.io"
    var mOkHttpClient: OkHttpClient? = null
    var mRetrofit: Retrofit? = null

    init {
        mOkHttpClient = OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .cache(Cache(File(App.instance.cacheDir, "httpCache"), 10 * 1024 * 1024))
                .addInterceptor(LoggerInterceptor(LoggerInterceptor.Level.HEADERS))
                .build()

        mRetrofit = Retrofit.Builder()
                .client(mOkHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
    }

    companion object {
        @Volatile
        var sApiCreate: ApiCreate? = null

        fun initApiCreate(baseUrl: String): ApiCreate {
            if (sApiCreate == null) {
                synchronized(ApiCreate::class.java) {
                    if (sApiCreate == null) {
                        sApiCreate = ApiCreate(baseUrl)
                    }
                }
            }
            return sApiCreate!!
        }

        fun instance(baseUrl: String): ApiCreate {
            return initApiCreate(baseUrl)
        }

//        val instance(var baseUrl: String): ApiCreate?
//            get() = initApiCreate(baseUrl)
    }
}