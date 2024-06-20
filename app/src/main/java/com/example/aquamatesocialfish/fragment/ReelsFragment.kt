package com.example.aquamatesocialfish.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aquamatesocialfish.R
import com.example.aquamatesocialfish.adapter.AllReelViewAdapter
import com.example.aquamatesocialfish.adapter.UserReelAdapter
import com.example.aquamatesocialfish.databinding.FragmentReelsBinding
import com.example.aquamatesocialfish.models.ReelsUserModel
import com.example.aquamatesocialfish.utils.VIDIO_REEL
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReelsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReelsFragment : Fragment() {

    private lateinit var reelsBinding : FragmentReelsBinding
    var allReellist = ArrayList<ReelsUserModel>()
    lateinit var reelAdapter : AllReelViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       reelsBinding = FragmentReelsBinding.inflate(inflater, container, false)
        reelAdapter = AllReelViewAdapter(requireContext(),allReellist)
        reelsBinding.reelsViewPager.adapter = reelAdapter
        Firebase.firestore.collection(VIDIO_REEL).get().addOnSuccessListener {
            var tempReelList  = ArrayList<ReelsUserModel>()
            allReellist.clear()
            for (i in it.documents){
                var allReel = i.toObject<ReelsUserModel>()
                tempReelList.add(allReel!!)
            }
            allReellist.addAll(tempReelList)
            allReellist.reverse()
            reelAdapter.notifyDataSetChanged()
        }
        return reelsBinding.root
    }

    companion object {

    }
}