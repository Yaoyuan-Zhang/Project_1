package com.yyz.project_1.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacesItemDecoration(var space :Int) :RecyclerView.ItemDecoration(){

    //private var space :Int= 10
    fun SpacesItemDecoration(space :Int){
        this.space = space
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = space
        outRect.right = space
        outRect.bottom = space


    }

}