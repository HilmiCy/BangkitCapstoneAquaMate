package com.example.aquamatesocialfish.adapter

import android.content.Context
import android.content.Intent
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

class AllPostAdapter(var context: Context, var allPostlist: ArrayList<PostUserModel>): RecyclerView.Adapter<AllPostAdapter.MyHolder>(){

    inner class  MyHolder(var binding : ItemViewHomeBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var binding = ItemViewHomeBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyHolder(binding)
    }

    override fun getItemCount(): Int {
        return allPostlist.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        try {
            Firebase.firestore.collection(USER_COLLECTION)
                .document(allPostlist.get(position).uid).get()
                .addOnSuccessListener {
                    var user = it.toObject<UserModel>()
                    Glide.with(context).load(user!!.image).placeholder(R.drawable.user_avatar)
                        .into(holder.binding.profileImage)
                    holder.binding.userName.text = user.name
                }

        }catch (e:Exception){

        }
        Glide.with(context).load(allPostlist!!.get(position).contentUrl).placeholder(R.drawable.user_avatar)
            .into(holder.binding.ivPost)
        try {
            val timeAgo = TimeAgo.using(allPostlist.get(position).time.toLong())
            holder.binding.etPostTime.text = timeAgo
        } catch (e: Exception){
            holder.binding.etPostTime.text = ""
        }

        holder.binding.btnShare.setOnClickListener {
            var share = Intent(Intent.ACTION_SEND)
            share.type = "text/plain"
            share.putExtra(Intent.EXTRA_TEXT,allPostlist.get(position).contentUrl)
            context.startActivity(share)
        }
        holder.binding.etPostText.text = allPostlist.get(position).contentCaption
        holder.binding.btnLike.setOnClickListener {
            holder.binding.btnLike.setImageResource(R.drawable.like_user)
        }
    }
}