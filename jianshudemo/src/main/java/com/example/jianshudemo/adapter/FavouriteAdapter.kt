package com.example.jianshudemo.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.jianshudemo.activity.DetailActivity
import com.example.jianshudemo.common.BaseConstant
import com.example.jianshudemo.databinding.FavouriteRecyclerItemBinding
import com.example.jianshudemo.dp.data.Shoe

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * FavouriteAdapter
 * @author liujianming
 * @date 2023-02-16
 */
class FavouriteAdapter constructor(val context: Context): ListAdapter<Shoe, FavouriteAdapter.FavouriteViewHolder>(ShoeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        return FavouriteViewHolder(
            FavouriteRecyclerItemBinding.inflate(
                LayoutInflater.from(parent.context)
                , parent
                , false
            )
        )
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val shoe = getItem(position)
        holder.apply {
            bind(onCreateListener(shoe.id), shoe)
            itemView.tag = shoe
        }
    }

    private fun onCreateListener(id: Long): View.OnClickListener {
        return View.OnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(BaseConstant.DETAIL_SHOE_ID, id)
            context.startActivity(intent)
        }
    }

    class FavouriteViewHolder(private val binding: FavouriteRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listener: View.OnClickListener, item: Shoe) {
            binding.apply {
                this.listener = listener
                this.shoe = item
                executePendingBindings()
            }
        }
    }
}