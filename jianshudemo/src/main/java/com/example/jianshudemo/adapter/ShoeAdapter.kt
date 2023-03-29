package com.example.jianshudemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.jianshudemo.activity.DetailActivity
import com.example.jianshudemo.databinding.ShoeRecyclerItemBinding
import com.example.jianshudemo.dp.data.Shoe

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * ShoeAdapter
 * @author liujianming
 * @date 2023-02-16
 */
class ShoeAdapter constructor(val context: Context) : PagingDataAdapter<Shoe, ShoeAdapter.ViewHolder>(ShoeDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shoe = getItem(position)
        holder.apply {
            bind(onCreateListener(shoe!!.id, shoe.imageUrl), shoe)
            itemView.tag = shoe
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ShoeRecyclerItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    /**
     * Holder的点击事件
     */
    private fun onCreateListener(id: Long, url: String): View.OnClickListener {
        return View.OnClickListener {
            val transitionName = "${id}-${url}"
            it.transitionName = transitionName
//            DetailActivity.start(context, id, it as ConstraintLayout, transitionName)
        }
    }

    class ViewHolder(private val binding: ShoeRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listener: View.OnClickListener, item: Shoe) {
            binding.apply {
                this.listener = listener
                this.shoe = item
                executePendingBindings()
            }
        }
    }
}