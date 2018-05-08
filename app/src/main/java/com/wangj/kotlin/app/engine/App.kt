package com.wangj.kotlin.app.engine

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

/**
 * 当前类注释：</p>
 * Author: LeonWang </p>
 * Created: 2018/4/17 - 12:01 </p>
 * Desc: </P>
 * Copyright (C)  2018 lijiawangjun@gmail.com
 */
class App : Application() {

    companion object {
        lateinit var instance: Application
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initConfig()
    }

    private fun initConfig() {
        initLogger()
    }

    private fun initLogger() {
        val strategy = PrettyFormatStrategy.newBuilder().tag("leon").build()
        Logger.addLogAdapter(AndroidLogAdapter(strategy))
    }
}