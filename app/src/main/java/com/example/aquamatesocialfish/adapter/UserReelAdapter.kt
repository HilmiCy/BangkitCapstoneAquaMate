package com.example.aquamatesocialfish.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.aquamatesocialfish.databinding.ItemRvMypostBinding
import com.example.aquamatesocialfish.models.ReelsUserModel

class UserReelAdapter(
    private val contentReelList: List<ReelsUserModel>
) : RecyclerView.Adapter<UserReelAdapter.ViewHolder>() {

    inner class ViewHolder(val viewholderbinding: ItemRvMypostBinding) :
        RecyclerView.ViewHolder(viewholderbinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewholderbinding = ItemRvMypostBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(viewholderbinding)
    }

    override fun getItemCount(): Int {
        return contentReelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val videoReelsUrl = contentReelList[position].videoReelsUrl
        Glide.with(holder.itemView.context)
            .load(videoReelsUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.viewholderbinding.postImage)
    }
}