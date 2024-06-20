package com.example.aquamatesocialfish.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aquamatesocialfish.R
import com.example.aquamatesocialfish.databinding.ItemFollowStoryBinding
import com.example.aquamatesocialfish.models.UserModel

class FollowStoryAdapter (var context: Context, var storyFollowList: ArrayList<UserModel>): RecyclerView.Adapter<FollowStoryAdapter.viewHolder>() {
    inner class viewHolder ( var followStoryBinding: ItemFollowStoryBinding) : RecyclerView.ViewHolder(followStoryBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var followStoryBinding = ItemFollowStoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return viewHolder(followStoryBinding)
    }

    override fun getItemCount(): Int {
       return storyFollowList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        Glide.with(context).load(storyFollowList[position].image)
            .placeholder(R.drawable.user_avatar).into(holder.followStoryBinding.ivStory)

        holder.followStoryBinding.tvUsernameStory.text = storyFollowList.get(position).name
    }
}