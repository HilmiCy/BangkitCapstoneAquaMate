package com.example.aquamatesocialfish.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aquamatesocialfish.R
import com.example.aquamatesocialfish.databinding.ItemSearchRvBinding
import com.example.aquamatesocialfish.models.UserModel
import com.example.aquamatesocialfish.utils.FOLLOW_USER
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SearchUserAdapter(var context: Context, var searchList: ArrayList<UserModel>) : RecyclerView.Adapter<SearchUserAdapter.ViewHolder>() {

    inner class ViewHolder(var searchBinding: ItemSearchRvBinding) : RecyclerView.ViewHolder(searchBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val searchBinding = ItemSearchRvBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(searchBinding)
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentUser = Firebase.auth.currentUser
        if (currentUser == null) {
            // Handle error case where currentUser is null
            return
        }

        val user = searchList[position]

        // Load user image
        Glide.with(context)
            .load(user.image)
            .placeholder(R.drawable.user_avatar)
            .into(holder.searchBinding.ivStory)

        // Set username
        holder.searchBinding.tvUsername.text = user.name

        // Set initial follow status
        checkFollowStatus(currentUser.uid, user, holder)

        holder.searchBinding.btnFollow.setOnClickListener {
            toggleFollowStatus(currentUser.uid, user, holder)
        }
    }

    private fun checkFollowStatus(currentUserId: String, user: UserModel, holder: ViewHolder) {
        Firebase.firestore.collection("$currentUserId$FOLLOW_USER")
            .whereEqualTo("email", user.email)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    holder.searchBinding.btnFollow.text = "Follow"
                    holder.searchBinding.btnFollow.tag = false // Not following
                } else {
                    holder.searchBinding.btnFollow.text = "Unfollow"
                    holder.searchBinding.btnFollow.tag = true // Following
                }
            }
            .addOnFailureListener { exception ->
                // Log error if any
                Log.e("SearchUserAdapter", "Error checking follow status: ", exception)
            }
    }

    private fun toggleFollowStatus(currentUserId: String, user: UserModel, holder: ViewHolder) {
        val isFollow = holder.searchBinding.btnFollow.tag as Boolean

        if (isFollow) {
            // Unfollow user
            Firebase.firestore.collection("$currentUserId$FOLLOW_USER")
                .whereEqualTo("email", user.email)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        document.reference.delete()
                            .addOnSuccessListener {
                                holder.searchBinding.btnFollow.text = "Follow"
                                holder.searchBinding.btnFollow.tag = false
                            }
                            .addOnFailureListener { exception ->
                                Log.e("SearchUserAdapter", "Error unfollowing user: ", exception)
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("SearchUserAdapter", "Error finding follow document: ", exception)
                }
        } else {
            // Follow user
            val followRef = Firebase.firestore.collection("$currentUserId$FOLLOW_USER").document()
            followRef.set(user)
                .addOnSuccessListener {
                    holder.searchBinding.btnFollow.text = "Unfollow"
                    holder.searchBinding.btnFollow.tag = true
                }
                .addOnFailureListener { exception ->
                    Log.e("SearchUserAdapter", "Error following user: ", exception)
                }
        }
    }
}
