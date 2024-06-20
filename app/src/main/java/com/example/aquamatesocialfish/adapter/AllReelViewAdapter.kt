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

class AllReelViewAdapter (context : Context, var allReelList : ArrayList<ReelsUserModel>) : RecyclerView.Adapter<AllReelViewAdapter.ViewHolder>() {

    inner class ViewHolder (var allreelbinding: ItemViewReelsBinding):
        RecyclerView.ViewHolder(allreelbinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var allreelbinding = ItemViewReelsBinding.inflate(LayoutInflater
            .from(parent.context), parent, false)
        return ViewHolder(allreelbinding)
    }

    override fun getItemCount(): Int {
        return allReelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(allReelList.get(position).profileImageReel).placeholder(R.drawable.user_avatar).into(holder.allreelbinding.ivProfile)
        holder.allreelbinding.tvCaptionTemplate.setText(allReelList.get(position).contentCaption)
        holder.allreelbinding.vvReels.setVideoPath(allReelList.get(position).videoReelsUrl)
        holder.allreelbinding.vvReels.setOnPreparedListener {
            holder.allreelbinding.progressBar.visibility = View.GONE
            holder.allreelbinding.vvReels.start()
        }
    }
}