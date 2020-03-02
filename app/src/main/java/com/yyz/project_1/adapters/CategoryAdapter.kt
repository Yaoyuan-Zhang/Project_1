package com.yyz.project_1.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yyz.project_1.R
import com.yyz.project_1.app.Config
import com.yyz.project_1.models.Category
import kotlinx.android.synthetic.main.category_view_adapter.view.*

class CategoryAdapter(var mContext: Context,var myList:ArrayList<Category> ):RecyclerView.Adapter<CategoryAdapter.MyViewHolder>(){
    var listener: OnAdapterClickListener? = null


    override fun getItemCount(): Int {
        return myList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var category = myList.get(position)
        holder.bindView(category,position)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        var view = LayoutInflater.from(mContext).inflate(R.layout.category_view_adapter,parent,false)
        var myViewHolder = MyViewHolder(view)
        return myViewHolder

    }


    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindView(category: Category,position: Int){
            itemView.image_view_category
            itemView.text_view_category.text =  category.catName
            Picasso
                .with(mContext)
                .load(Config.IMAGE_URL+category.catImage)
                .into(itemView.image_view_category)

            //Log.d("image?","http://rjtmobile.com/grocery/images/"+myList.get(position).catImage)
            itemView.setOnClickListener{
                listener?.onItemClicked(position)
            }
        }

    }



    fun setData(list: ArrayList<Category>) {
        myList = list
        notifyDataSetChanged()
    }


    interface OnAdapterClickListener{
        fun onItemClicked(position: Int)
    }

    fun setOnAdapterClickListner(onAdapterClickListener: OnAdapterClickListener){
        listener = onAdapterClickListener
    }

}