package com.example.aquamatesocialfish.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import androidx.viewpager2.adapter.FragmentViewHolder

class ViewPagerAdapter(managerFragment: FragmentManager)
    : FragmentPagerAdapter(managerFragment, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        val listTittle = mutableListOf<String>()
        val listFragment = mutableListOf<Fragment>()
    override fun getCount(): Int {
        return listFragment.size
    }

    override fun getItem(position: Int): Fragment {
        return listFragment.get(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listTittle.get(position)
    }

    fun AddFragment(fragment: Fragment, title: String){
        listFragment.add(fragment)
        listTittle.add(title)
    }
}