package com.example.myapplication.guolin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.guolin.testclass.Image

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * AlbumAdapter
 * @author liujianming
 * @date 2023-04-04
 */
class AlbumAdapter(private val context: Context, private val imageList: List<Image>,
                   private val checkedImages: MutableMap<String, Image>, private val imageSize: Int,
                   private val pickFiles: Boolean): RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val checkedView: ImageView = view.findViewById(R.id.checkedView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.album_image_item, parent, false)
        val holder = ViewHolder(view)
        if (pickFiles) {
            holder.itemView.setOnClickListener {
                val position = holder.layoutPosition
                val image = imageList[position]
                image.checked = !image.checked
                if (image.checked) {
                    checkedImages[image.uri.toString()] = image
                } else {
                    checkedImages.remove(image.uri.toString())
                }
                notifyItemChanged(position)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: AlbumAdapter.ViewHolder, position: Int) {
        holder.imageView.layoutParams.width = imageSize
        holder.imageView.layoutParams.height = imageSize

        val image = imageList[position]
        if (image.checked) {
            holder.checkedView.visibility = View.VISIBLE
        } else {
            holder.checkedView.visibility = View.INVISIBLE
        }
        val options = RequestOptions.placeholderOf(R.drawable.album_loading_bg).override(imageSize, imageSize)
        Glide.with(context).load(image.uri).apply(options).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}