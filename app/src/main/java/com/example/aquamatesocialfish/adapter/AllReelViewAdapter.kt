package com.example.aquamatesocialfish.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aquamatesocialfish.R
import com.example.aquamatesocialfish.databinding.ItemViewReelsBinding
import com.example.aquamatesocialfish.models.ReelsUserModel
import com.squareup.picasso.Picasso

class AllReelViewAdapter(private val context: Context, private val allReelList: ArrayList<ReelsUserModel>) :
    RecyclerView.Adapter<AllReelViewAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemViewReelsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemViewReelsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return allReelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reel = allReelList[position]

        Picasso.get().load(reel.profileImageReel).
        placeholder(R.drawable.user_avatar)
            .into(holder.binding.ivProfile)

        holder.binding.tvCaptionTemplate.text = reel.contentCaption

        holder.binding.vvReels.setVideoPath(reel.videoReelsUrl)
        holder.binding.vvReels.setOnPreparedListener {
            holder.binding.progressBar.visibility = View.GONE
            holder.binding.vvReels.start()
        }
    }
}