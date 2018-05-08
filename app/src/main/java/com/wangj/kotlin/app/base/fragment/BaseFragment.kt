package com.wangj.kotlin.app.base.fragment

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle2.components.support.RxFragment
import com.wangj.kotlin.app.engine.networks.HttpTransFormer

/**
 * 当前类注释：BaseFragment </p>
 * Author: LeonWang </p>
 * Created: 2018/5/4 - 10:45 </p>
 * Desc: </P>
 * Copyright (C)  2018 lijiawangjun@gmail.com
 */
abstract class BaseFragment : RxFragment() {

    protected var mVisiable = true
    protected var mPrepared = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutRedId(), container, false) as View
        findViews(view)
        initViews()
        mPrepared = true
        lazyLoad()
        initComplete()
        return view
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            mVisiable = true
            onVisiable()
        } else {
            mVisiable = false
            onInVisiable()
        }
    }

    @LayoutRes
    abstract fun layoutRedId(): Int

    abstract fun findViews(view: View)

    abstract fun initViews()

    /**
     * 初始化完成
     * <p>
     * 非懒加载页面初始化完成
     */
    abstract fun initComplete()

    /**
     * 不可见
     */
    protected fun onInVisiable() {

    }

    /**
     * 可见
     */
    protected fun onVisiable() {
        lazyLoad()
    }

    /**
     * 懒加载
     */
    private fun lazyLoad() {
        if (!mPrepared || !mVisiable) {
            return
        }
        lazyData()
    }

    /**
     * 懒加载页面初始化完成
     */
    abstract fun lazyData()


    fun <T> bindResponseTransformer(): HttpTransFormer<T> {
        return HttpTransFormer(this.bindToLifecycle())
    }

}