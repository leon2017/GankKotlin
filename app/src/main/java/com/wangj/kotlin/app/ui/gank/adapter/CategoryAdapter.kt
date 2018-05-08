package com.wangj.kotlin.app.ui.gank.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wangj.kotlin.app.R
import com.wangj.kotlin.app.entity.GankCategoryEntity
import com.wangj.kotlin.app.utils.getTime
import com.wangj.kotlin.app.utils.random
import kotlinx.android.synthetic.main.item_category.view.*
import kotlinx.android.synthetic.main.item_refresh_footer.view.*
import java.util.*

/**
 * 当前类注释：</p>
 * Author: LeonWang </p>
 * Created: 2018/5/7 - 12:22 </p>
 * Desc: </P>
 * Copyright (C)  2018 lijiawangjun@gmail.com
 */
class CategoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context
    var itemClick: (View, GankCategoryEntity) -> Unit = { _, _ -> }
    private var mData: ArrayList<GankCategoryEntity>

    //默认布局
    private val TYPE_ITEM = 1
    //footer布局
    private val TYPE_FOOTER = 2
    //当前加载状态，默认加载完成
    private var loadState = 2

    init {
        mData = ArrayList<GankCategoryEntity>()
    }

    companion object {
        //正在加载
        val LOADING = 1
        //加载完成
        public val LOADING_COMPLETE = 2
        //加载失败
        public val LOADING_ERROR = 3
    }

    fun setNewData(data: ArrayList<GankCategoryEntity>) {
        mData.clear()
        this.mData.addAll(data)
        notifyDataSetChanged()
    }

    fun setData(data: ArrayList<GankCategoryEntity>) {
        if (data.size > 0) {
            mData.addAll(data)
//        notifyDataSetChanged()
            notifyItemRangeInserted(itemCount - 1, data.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return if (viewType == TYPE_FOOTER) {
            val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_refresh_footer, parent, false)
            FooterHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false)
            CategoryViewHolder(view, itemClick)
        }
    }

    override fun getItemCount(): Int {
        return mData.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_ITEM) {
            val defVH = holder as CategoryViewHolder
            val categoryBean = mData[position]
            defVH.bindView(position, categoryBean)
        } else {
            val footerVH = holder as FooterHolder
            footerVH.bindView(position, loadState)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemCount == position + 1) {
            TYPE_FOOTER
        } else {
            TYPE_ITEM
        }
    }

    /**
     * 更新加载状态
     */
    fun setLoadStatus(loadStatus: Int) {
        this.loadState = loadStatus
        notifyDataSetChanged()
    }

    fun getLoadStatus(): Int {
        return this.loadState
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            val gridManager = manager as GridLayoutManager
            gridManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (getItemViewType(position) == TYPE_FOOTER) {
                        gridManager.spanCount
                    } else 1
                }
            }
        }
    }

    inner class CategoryViewHolder(view: View, private val itemClick: (View, GankCategoryEntity) -> Unit) :
            RecyclerView.ViewHolder(view) {

        @SuppressLint("SetTextI18n")
        fun bindView(position: Int, bean: GankCategoryEntity) {
            val lastPosition = itemCount
            with(bean) {
                with(itemView) {
                    var iconText = "无"
                    if (!TextUtils.isEmpty(who)) {
                        iconText = who.trim().substring(0, 1).toUpperCase()
                    }
                    tvIcon.text = iconText
                    tvIcon.setBackColor(randomColor())
                    val name = if (TextUtils.isEmpty(who)) "无名氏" else who
                    tvNameDate.text = "$name  ${getTime(createdAt)}"
                    tvSummary.text = desc
                    container.setOnClickListener {
                        itemClick(itemView, bean)
                    }
//                    Log.d("lastPosition", lastPosition.toString())
//                    Log.d("position", position.toString())
                    if (lastPosition == position + 2) {
                        divider.visibility = View.GONE
                    } else {
                        divider.visibility = View.VISIBLE
                    }
                    when (position) {
                        0 -> {
                            container.setBackgroundResource(R.drawable.shape_first_normal)
                        }
                        lastPosition - 2 -> {
                            container.setBackgroundResource(R.drawable.shape_last_normal)
                        }
                        else -> {
                            container.setBackgroundColor(Color.WHITE)
                        }
                    }
                }
            }
        }

        private fun randomColor(): Int {
            val red = (0..256).random()
            val green = (0..256).random()
            val blue = (0..256).random()
            return if (red == 0 && green == 0 && blue == 0) {
                randomColor()
            } else {
                Color.rgb(red, green, blue)
            }
        }
    }


    inner class FooterHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindView(position: Int, loadStatus: Int) {
            with(itemView) {
                when (loadStatus) {
                    LOADING -> {
                        pbLoading.visibility = View.VISIBLE
                        with(tvLoading) {
                            visibility = View.VISIBLE
                            text = "正在加载中"
                        }
                    }
                    LOADING_COMPLETE -> {
                        pbLoading.visibility = View.GONE
                        with(tvLoading) {
                            visibility = View.GONE
                        }
                    }
                    LOADING_ERROR -> {
                        pbLoading.visibility = View.GONE
                        with(tvLoading) {
                            visibility = View.VISIBLE
                            text = "加载失败"
                        }
                    }
                    else -> {
                    }
                }
            }
        }
    }
}