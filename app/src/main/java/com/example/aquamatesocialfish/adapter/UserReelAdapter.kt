package com.example.aquamatesocialfish.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.aquamatesocialfish.databinding.ItemRvMypostBinding
import com.example.aquamatesocialfish.models.PostUserModel
import com.example.aquamatesocialfish.models.ReelsUserModel
import com.squareup.picasso.Picasso

class UserReelAdapter(
    var context: Context,
    var contentReelList: ArrayList<ReelsUserModel>
) : RecyclerView.Adapter<UserReelAdapter.ViewHolder>() {

    inner class ViewHolder(var viewholderbinding: ItemRvMypostBinding) :
        RecyclerView.ViewHolder(viewholderbinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewholderbinding = ItemRvMypostBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(viewholderbinding)
    }

    override fun getItemCount(): Int {
        return contentReelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(contentReelList.get(position).videoReelsUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.viewholderbinding.postImage)
    }
}