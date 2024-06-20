package com.example.aquamatesocialfish.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aquamatesocialfish.R
import com.example.aquamatesocialfish.adapter.AllPostAdapter
import com.example.aquamatesocialfish.adapter.FollowStoryAdapter
import com.example.aquamatesocialfish.databinding.FragmentHomeBinding
import com.example.aquamatesocialfish.models.PostUserModel
import com.example.aquamatesocialfish.models.UserModel
import com.example.aquamatesocialfish.utils.FOLLOW_USER
import com.example.aquamatesocialfish.utils.POST
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var homebinding: FragmentHomeBinding
    private var allPostList = ArrayList<PostUserModel>()
    private lateinit var postAdapter: AllPostAdapter
    private var followStoryList = ArrayList<UserModel>()
    private lateinit var followStoryAdapter: FollowStoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homebinding = FragmentHomeBinding.inflate(inflater, container, false)
        postAdapter = AllPostAdapter(requireContext(),allPostList)
        homebinding.rvHome.layoutManager = LinearLayoutManager(requireContext())
        homebinding.rvHome.adapter = postAdapter

        followStoryAdapter = FollowStoryAdapter(requireContext(),followStoryList)
        homebinding.FollowStoryRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager
                .HORIZONTAL, false)
        homebinding.FollowStoryRv.adapter = followStoryAdapter

        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(homebinding.materialToolbarHome)

        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW_USER).get().addOnSuccessListener {
           var storyTempList = ArrayList<UserModel>()
            followStoryList.clear()
            for(i in it.documents){
                var storyUser : UserModel = i.toObject<UserModel>()!!
                storyTempList.add(storyUser)
            }
            followStoryList.addAll(storyTempList)
            followStoryAdapter.notifyDataSetChanged()
        }

        Firebase.firestore.collection(POST).get().addOnSuccessListener {
            var postTempList = ArrayList<PostUserModel>()
            allPostList.clear()
            for(i in it.documents){
                var allPost: PostUserModel = i.toObject<PostUserModel>()!!
                postTempList.add(allPost)
            }
            allPostList.addAll(postTempList)
            postAdapter.notifyDataSetChanged()
        }
        return homebinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}