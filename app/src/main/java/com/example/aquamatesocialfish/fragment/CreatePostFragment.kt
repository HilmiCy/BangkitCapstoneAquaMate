package com.example.aquamatesocialfish.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aquamatesocialfish.R
import com.example.aquamatesocialfish.databinding.FragmentCreatePostBinding
import com.example.aquamatesocialfish.view.AllRealsActivity
import com.example.aquamatesocialfish.view.PostActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CreatePostFragment : BottomSheetDialogFragment() {
    private lateinit var binding:FragmentCreatePostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreatePostBinding.inflate(inflater,container,false)

        binding.ivAddPost.setOnClickListener {
            activity?.startActivity(Intent(requireContext(),PostActivity::class.java))
        }

        binding.ivAddReels.setOnClickListener {
            activity?.startActivity(Intent(requireContext(),AllRealsActivity::class.java))
        }

        return binding.root
    }

    companion object {

    }
}