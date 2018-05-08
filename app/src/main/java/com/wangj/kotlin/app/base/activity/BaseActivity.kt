package com.wangj.kotlin.app.base.activity

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.MenuItem
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.wangj.kotlin.app.utils.ThemeUtil

/**
 * 当前类注释：</p>
 * Author: LeonWang </p>
 * Created: 2018/5/3 - 10:36 </p>
 * Desc: </P>
 * Copyright (C)  2018 lijiawangjun@gmail.com
 */
abstract class BaseActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtil.init(this).initTheme()
        setContentView(layoutResId())
        findViews()
        initViews()
        initDatas()
    }

    /**
     * 布局 id
     */
    @LayoutRes
    abstract fun layoutResId(): Int

    abstract fun findViews()

    abstract fun initViews()

    abstract fun initDatas()

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

}