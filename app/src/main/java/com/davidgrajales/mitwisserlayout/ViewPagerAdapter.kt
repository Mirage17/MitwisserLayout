package com.davidgrajales.mitwisserlayout

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fa:FragmentActivity): FragmentStateAdapter(fa) {

    companion object{
        private  const val ARG_OBJECT="object"
    }
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
       return when(position){
            0->{ChatList()}

           else ->opciones()
       }

    }
}