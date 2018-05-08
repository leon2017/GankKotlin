package com.wangj.kotlin.app.ui.gank.categoryblog

import com.trello.rxlifecycle2.android.FragmentEvent
import com.wangj.kotlin.app.engine.networks.ApiException
import com.wangj.kotlin.app.engine.networks.ResponseObserver
import com.wangj.kotlin.app.entity.BaseEntity
import com.wangj.kotlin.app.entity.GankCategoryEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * 当前类注释：</p>
 * Author: LeonWang </p>
 * Created: 2018/5/7 - 18:13 </p>
 * Desc: </P>
 * Copyright (C)  2018 lijiawangjun@gmail.com
 */
class CategoryPresenter(view: CategoryContract.View, model: CategoryContract.Model) :
        CategoryContract.Presenter(view, model) {

    /**
     * 请求分类数据
     */
    override fun startRequstList() {
        mModel.requestCategoryData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mView.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(object : ResponseObserver<BaseEntity<ArrayList<GankCategoryEntity>>>() {
                    override fun error(e: ApiException) {
                        mView.loadError(e)
                    }

                    override fun onComplete() {
                        mView.loadComplete(mModel.getCurPage())
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: BaseEntity<ArrayList<GankCategoryEntity>>) {
                        if (t.results.isNotEmpty()) {
                            mView.setCategoryList(mModel.getCurPage(), t.results)
                        } else {
                            mView.loadError(ApiException("暂无数据", ApiException.DATA_EMPTY))
                        }
                    }
                })
    }
}