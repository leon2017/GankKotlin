package com.wangj.kotlin.app.ui.launch

import android.annotation.SuppressLint
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.wangj.kotlin.app.R
import com.wangj.kotlin.app.base.activity.BaseActivity
import com.wangj.kotlin.app.ui.gank.adapter.CategoryPagerAdapter
import com.wangj.kotlin.app.ui.gank.categoryblog.CategoryBloaFragment
import com.wangj.kotlin.app.utils.ThemeUtil


class MainActivity : BaseActivity() {

    private lateinit var mTabLayout: TabLayout
    private lateinit var mViewPage: ViewPager
    protected lateinit var mToolbar: Toolbar

    override fun layoutResId(): Int {
        return R.layout.activity_main
    }

    override fun findViews() {
        mTabLayout = findViewById(R.id.tabLayout)
        mViewPage = findViewById(R.id.view_pager)
        mToolbar = findViewById(R.id.toolbar)
    }

    @SuppressLint("RestrictedApi")
    override fun initViews() {
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun initDatas() {
        val categoryArray = resources.getStringArray(R.array.GankCategoryArray)
        var fragments = ArrayList<Fragment>()
        for (index in categoryArray.indices) {
            fragments.add(CategoryBloaFragment.newInstance(index))
        }
        var adapter = CategoryPagerAdapter(supportFragmentManager, categoryArray, fragments)
        mViewPage.adapter = adapter
        mTabLayout.setupWithViewPager(mViewPage)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_theme -> {
                ThemeUtil.init(this).changeTheme()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}
