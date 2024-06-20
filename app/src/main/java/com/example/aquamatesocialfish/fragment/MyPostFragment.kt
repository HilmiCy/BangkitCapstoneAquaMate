package com.example.aquamatesocialfish.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.aquamatesocialfish.adapter.MyPostViewAdapter
import com.example.aquamatesocialfish.databinding.FragmentMyPostBinding
import com.example.aquamatesocialfish.models.PostUserModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class MyPostFragment : Fragment() {

    private lateinit var myPostBinding: FragmentMyPostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myPostBinding = FragmentMyPostBinding.inflate(inflater, container, false)

        val contentList = ArrayList<PostUserModel>()
        val adapter = MyPostViewAdapter(requireContext(), contentList)

        myPostBinding.rvMypost.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        myPostBinding.rvMypost.adapter = adapter


        val userId = Firebase.auth.currentUser?.uid
        if (userId != null) {
            Firebase.firestore.collection(userId).get()
                .addOnSuccessListener { snapshot ->
                    val tempList = ArrayList<PostUserModel>()
                    for (document in snapshot.documents) {
                        val postUser = document.toObject<PostUserModel>()
                        if (postUser != null) {
                            tempList.add(postUser)
                        } else {
                            Log.e("MyPostFragment", "Failed to convert document to PostUserModel")
                        }
                    }
                    contentList.addAll(tempList)
                    adapter.notifyDataSetChanged()
                    Log.d("MyPostFragment", "Data successfully loaded: ${contentList.size} items")
                }
                .addOnFailureListener { e ->
                    Log.e("MyPostFragment", "Error fetching data", e)
                }
        } else {
            Log.e("MyPostFragment", "User ID is null")
        }

        return myPostBinding.root
    }
}