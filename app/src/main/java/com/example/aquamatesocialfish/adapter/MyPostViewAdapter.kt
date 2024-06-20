package com.example.aquamatesocialfish.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aquamatesocialfish.databinding.ItemRvMypostBinding
import com.example.aquamatesocialfish.models.PostUserModel
import com.squareup.picasso.Picasso

class MyPostViewAdapter(
    var context: Context,
    var contentList: ArrayList<PostUserModel>
) : RecyclerView.Adapter<MyPostViewAdapter.ViewHolder>() {

    inner class ViewHolder(var viewholderbinding: ItemRvMypostBinding) :
        RecyclerView.ViewHolder(viewholderbinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewholderbinding = ItemRvMypostBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(viewholderbinding)
    }

    override fun getItemCount(): Int {
        return contentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl = contentList[position].contentUrl
        Log.d("MyPostViewAdapter", "Loading image from URL: $imageUrl")
        Picasso.get().load(imageUrl)
            .into(holder.viewholderbinding.postImage, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    Log.d("MyPostViewAdapter", "Image loaded successfully")
                }

                override fun onError(e: Exception?) {
                    Log.e("MyPostViewAdapter", "Error loading image", e)
                }
            })
    }
}