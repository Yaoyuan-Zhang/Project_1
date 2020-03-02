package com.yyz.project_1.adapters

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.yyz.project_1.fragments.CommonFragment

class CommonFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm){
    //private var mList = ArrayList<String> ()
    private var mList: ArrayList<String> = ArrayList()
    private var mFragmentList: ArrayList<Fragment> = ArrayList()


    fun addFragment(catName:String,subId:Int){
        mFragmentList.add(CommonFragment.newInstance(subId))
        mList.add(catName)
        //Log.d("mList.size",mList.toString())

    }




    override fun getItem(position: Int): Fragment {
        return mFragmentList.get(position)


    }

    override fun getCount(): Int {
        Log.d("mList.getCount",mList.toString())

        return mList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        // Log.d("title",mList.get(position).subName)
        return mList.get(position)
    }


}