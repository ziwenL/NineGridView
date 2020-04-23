package com.ziwenl.ninegridview.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziwenl.ninegridview.R
import com.ziwenl.ninegridview.ui.adapter.MainAdapter
import com.ziwenl.ninegridview.utils.GlideUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var mData: MutableList<String>
    private lateinit var mAdapter: MainAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlideUtil.init(this)

        mData = mutableListOf()
        mAdapter = MainAdapter(mData)
        rv_main.layoutManager = LinearLayoutManager(this)
        rv_main.adapter = mAdapter

        tv_get.setOnClickListener { v ->
            mData.add("")
            mData.add("")
            mData.add("")
            mData.add("")
            mData.add("")
            mData.add("")
            mData.add("")
            mData.add("")
            mData.add("")
            mData.add("")
            mData.add("")
            mData.add("")
            mData.add("")
            mData.add("")
            mData.add("")
            mData.add("")
            mData.add("")
            mData.add("")
            mAdapter.notifyDataSetChanged()
            tv_get.text = String.format("当前item数:%s\n点击获取更多", mData.size)
        }
    }
}
