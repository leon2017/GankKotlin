package com.wangj.kotlin.app.ui.gank.categoryblog

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.wangj.kotlin.app.R
import com.wangj.kotlin.app.base.fragment.BaseFragment
import com.wangj.kotlin.app.engine.networks.ApiException
import com.wangj.kotlin.app.entity.GankCategoryEntity
import com.wangj.kotlin.app.ui.gank.adapter.CategoryAdapter


/**
 * 当前类注释：技术文章分类</p>
 * Author: LeonWang </p>
 * Created: 2018/5/4 - 11:03 </p>
 * Desc: </P>
 * Copyright (C)  2018 lijiawangjun@gmail.com
 */
class CategoryBloaFragment : BaseFragment(), CategoryContract.View {

    private lateinit var mRefresh: SwipeRefreshLayout
    private lateinit var mList: RecyclerView
    private lateinit var mAdapter: CategoryAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mCategoryPresenter: CategoryPresenter
    //福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
    private val indexList = arrayOf("all", "Android", "iOS", "休息视频", "拓展资源", "前端", "福利")
    private var categoryIndex = "all"
    private lateinit var mModel: CategoryModel


    companion object {
        private val CATEGORY_INDEX = "category_index"
        fun newInstance(index: Int): Fragment {
            val fragment = CategoryBloaFragment()
            var bundle = Bundle()
            bundle.putInt(CATEGORY_INDEX, index)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun layoutRedId(): Int {
        return R.layout.fragment_category_blog
    }

    override fun findViews(view: View) {
        mRefresh = view.findViewById(R.id.swipe_refresh_layout)
        mList = view.findViewById(R.id.recyclerView)
        val bundle = arguments
        if (bundle != null) {
            val int = bundle.getInt(CATEGORY_INDEX)
            categoryIndex = indexList[int]
        }
    }

    override fun initViews() {
        mModel = CategoryModel()
        mModel.type = categoryIndex
        mCategoryPresenter = CategoryPresenter(this, mModel)
        mRefresh.setOnRefreshListener {
            mModel.page = 1
            mCategoryPresenter.startRequstList()
        }

        mList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val loadStatus = mAdapter.getLoadStatus()
                val lastPosition = mLayoutManager.findLastVisibleItemPosition();
                if ((loadStatus == CategoryAdapter.LOADING_COMPLETE || loadStatus == CategoryAdapter.LOADING_ERROR)
                        && !mRefresh.isRefreshing && lastPosition == mAdapter.itemCount - 1) {
                    mAdapter.setLoadStatus(CategoryAdapter.LOADING)
//                    val page = mModel.page
                    mModel.page++
                    mCategoryPresenter.startRequstList()
                }
            }
        })
    }

    override fun initComplete() {
        mAdapter = CategoryAdapter()
        mLayoutManager = LinearLayoutManager(activity)
        mList.layoutManager = mLayoutManager
        mList.adapter = mAdapter
        mCategoryPresenter.startRequstList()

        mAdapter.itemClick = { view, entity ->
            var intent = Intent(activity,ArticleDeatilActivity::class.java)
            var bundle = Bundle()
            bundle.putString(ARTICLE_URL,entity.url)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    override fun lazyData() {
    }

    override fun setCategoryList(page: Int, list: ArrayList<GankCategoryEntity>) {
        if (page == 1) {
            mAdapter.setNewData(list)
        } else {
            mAdapter.setData(list)
        }
    }

    override fun loadError(e: ApiException) {

    }

    override fun loadComplete(page: Int) {
        if (page == 1) {
            if (mRefresh.isRefreshing) {
                mRefresh.isRefreshing = false
            }
        } else {
            mAdapter.setLoadStatus(CategoryAdapter.LOADING_COMPLETE)
        }
    }
}