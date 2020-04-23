package com.ziwenl.ninegridview.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ziwenl.ninegridview.R
import com.ziwenl.ninegridview.utils.NetImageUtil
import kotlinx.android.synthetic.main.item_content.view.*

/**
 * PackageName : com.ziwenl.ninegridview.ui.adapter
 * Author : Ziwen Lan
 * Date : 2020/4/13
 * Time : 11:27
 * Introduction :
 */
class MainAdapter(var data: List<String>) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_content, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        //TODO 给单图item单独设置一个viewType,解决滚动时的高度跳动问题
        val count = position % 10
        if (count == 1) {
            return position
        } else {
            return 0
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val count = position % 10
        holder.itemView.tv_content.text = String.format("第%s条item，图片数量%s", position, count)
        holder.itemView.gv_picture.setUrls(NetImageUtil.getUrls(count))
        holder.itemView.gv_picture.setCallback { index, urls ->
            //TODO 图片点击事件
            Toast.makeText(holder.itemView.context, "点击图片下标为$index", Toast.LENGTH_SHORT).show()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}