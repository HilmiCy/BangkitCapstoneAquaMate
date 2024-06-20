package com.example.aquamatesocialfish.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.aquamatesocialfish.R
import com.example.aquamatesocialfish.adapter.MyPostViewAdapter
import com.example.aquamatesocialfish.adapter.UserReelAdapter
import com.example.aquamatesocialfish.databinding.FragmentMyReelsBinding
import com.example.aquamatesocialfish.models.PostUserModel
import com.example.aquamatesocialfish.models.ReelsUserModel
import com.example.aquamatesocialfish.utils.VIDIO_REEL
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class MyReelsFragment : Fragment() {

    private lateinit var myReelsBinding: FragmentMyReelsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myReelsBinding = FragmentMyReelsBinding.inflate(inflater, container, false)

        val contentReelList = ArrayList<ReelsUserModel>()
        val adapter = UserReelAdapter(requireContext(),contentReelList )

        // Setup RecyclerView
        myReelsBinding.rvMyReels.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        myReelsBinding.rvMyReels.adapter = adapter

        // Fetch data from Firestore
        val userId = Firebase.auth.currentUser?.uid
        if (userId != null) {
            Firebase.firestore.collection(userId + VIDIO_REEL).get()
                .addOnSuccessListener { snapshot ->
                    val tempList = ArrayList<ReelsUserModel>()
                    for (document in snapshot.documents) {
                        val reelUser : ReelsUserModel = document.toObject<ReelsUserModel>()!!
                        if (reelUser != null) {
                            tempList.add(reelUser)
                        } else {
                            Log.e("MyPostFragment", "Failed to convert document to PostUserModel")
                        }
                    }
                    contentReelList.addAll(tempList)
                    adapter.notifyDataSetChanged()
                    Log.d("MyPostFragment", "Data successfully loaded: ${contentReelList.size} items")
                }
                .addOnFailureListener { e ->
                    Log.e("MyPostFragment", "Error fetching data", e)
                }
        } else {
            Log.e("MyPostFragment", "User ID is null")
        }

        return myReelsBinding.root
    }

    companion object {

    }
}