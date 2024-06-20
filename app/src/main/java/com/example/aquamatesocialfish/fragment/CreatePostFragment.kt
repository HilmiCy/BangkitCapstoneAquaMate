package com.example.aquamatesocialfish.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        binding = FragmentCreatePostBinding.inflate(inflater,container,false)

        binding.tvAddPost.setOnClickListener {
            activity?.startActivity(Intent(requireContext(),PostActivity::class.java))
            activity?.finish()
        }

        binding.tvAddReels.setOnClickListener {
            activity?.startActivity(Intent(requireContext(),AllRealsActivity::class.java))
        }

        return binding.root
    }

    companion object {

    }
}