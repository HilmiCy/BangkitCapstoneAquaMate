package com.example.aquamatesocialfish.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.aquamatesocialfish.adapter.UserReelAdapter
import com.example.aquamatesocialfish.databinding.FragmentMyReelsBinding
import com.example.aquamatesocialfish.models.ReelsUserModel
import com.example.aquamatesocialfish.utils.VIDIO_REEL
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class MyReelsFragment : Fragment() {

    private lateinit var myReelsBinding: FragmentMyReelsBinding
    private val contentReelList = mutableListOf<ReelsUserModel>()
    private lateinit var adapter: UserReelAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        myReelsBinding = FragmentMyReelsBinding.inflate(inflater, container, false)

        adapter = UserReelAdapter(contentReelList)

        myReelsBinding.rvMyReels.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        myReelsBinding.rvMyReels.adapter = adapter

        fetchReelsData()

        return myReelsBinding.root
    }

    private fun fetchReelsData() {
        val userId = Firebase.auth.currentUser?.uid
        if (userId != null) {
            Firebase.firestore.collection(userId + VIDIO_REEL).get()
                .addOnSuccessListener { snapshot ->
                    val tempList = mutableListOf<ReelsUserModel>()
                    for (document in snapshot.documents) {
                        val reelUser: ReelsUserModel? = document.toObject<ReelsUserModel>()
                        if (reelUser != null) {
                            tempList.add(reelUser)
                        } else {
                            Log.e("MyReelsFragment", "Failed to convert document to ReelsUserModel")
                        }
                    }
                    contentReelList.clear()
                    contentReelList.addAll(tempList)
                    adapter.notifyDataSetChanged()
                    Log.d("MyReelsFragment", "Data successfully loaded: ${contentReelList.size} items")
                }
                .addOnFailureListener { e ->
                    Log.e("MyReelsFragment", "Error fetching data", e)
                }
        } else {
            Log.e("MyReelsFragment", "User ID is null")
        }
    }
}