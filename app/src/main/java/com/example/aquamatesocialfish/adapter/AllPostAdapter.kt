package com.example.aquamatesocialfish.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aquamatesocialfish.R
import com.example.aquamatesocialfish.databinding.ItemViewHomeBinding
import com.example.aquamatesocialfish.models.PostUserModel
import com.example.aquamatesocialfish.models.UserModel
import com.example.aquamatesocialfish.utils.USER_COLLECTION
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class AllPostAdapter(var context: Context, var allPostlist: ArrayList<PostUserModel>) : RecyclerView.Adapter<AllPostAdapter.MyHolder>() {

    inner class MyHolder(var binding: ItemViewHomeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding = ItemViewHomeBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyHolder(binding)
    }

    override fun getItemCount(): Int {
        return allPostlist.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val post = allPostlist[position]

        if (post.uid.isNullOrEmpty()) {
            Log.e("AllPostAdapter", "Invalid UID: ${post.uid}")
            return
        }

        Firebase.firestore.collection(USER_COLLECTION)
            .document(post.uid).get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject<UserModel>()
                if (user != null) {
                    Glide.with(context)
                        .load(user.image)
                        .placeholder(R.drawable.user_avatar)
                        .into(holder.binding.profileImage)
                    holder.binding.userName.text = user.name
                } else {
                    Log.e("AllPostAdapter", "User data is null for uid: ${post.uid}")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("AllPostAdapter", "Failed to get user data", exception)
            }

        Glide.with(context)
            .load(post.contentUrl)
            .placeholder(R.drawable.user_avatar)
            .into(holder.binding.ivPost)

        holder.binding.etPostText.text = post.contentCaption

        try {
            val timeAgo = TimeAgo.using(post.time.toLong())
            holder.binding.etPostTime.text = timeAgo
        } catch (e: Exception) {
            Log.e("AllPostAdapter", "Failed to parse time", e)
            holder.binding.etPostTime.text = ""
        }

        holder.binding.btnShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, post.contentUrl)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share via"))
        }
    }
}