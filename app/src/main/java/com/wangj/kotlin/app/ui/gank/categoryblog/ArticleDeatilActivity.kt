package com.wangj.kotlin.app.ui.gank.categoryblog

import com.wangj.kotlin.app.base.activity.BaseBrowerActivity


val ARTICLE_URL = "article_url"


/**
 * 文章详情
 */
class ArticleDeatilActivity : BaseBrowerActivity() {

    protected lateinit var webviewUrl: String

    override fun initViews() {
        super.initViews()
        val bundle = intent.extras
        if (bundle != null) {
            webviewUrl = bundle.getString(ARTICLE_URL)
        }
    }

    override fun initDatas() {
        mWebView.loadUrl(webviewUrl)
    }
}
