package com.yyz.project_1.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.yyz.project_1.R
import com.yyz.project_1.app.Config
import com.yyz.project_1.models.FavoriteProduct
import kotlinx.android.synthetic.main.favorite_product_adapter.view.*

class MyFavoriteAdapter(var mContext: Context, var myList: ArrayList<FavoriteProduct>):
    RecyclerView.Adapter<MyFavoriteAdapter.MyViewHolder>() {
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference


    var listener: OnDetailListener? = null
    interface OnDetailListener{
        fun onDetail(position: Int)
    }

    fun setDetailListner(onDetailListener:OnDetailListener){
        listener = onDetailListener
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.favorite_product_adapter,parent,false)
        var myViewHolder=MyViewHolder(view)

        return myViewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var favorite = myList.get(position)
        holder.bindView(favorite,position)

    }

    override fun getItemCount(): Int {
        return myList.size

    }

    fun setData(list: ArrayList<FavoriteProduct>) {
        myList = list
        notifyDataSetChanged()
    }



    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindView(favorite: FavoriteProduct,position: Int){
            var product = favorite.likeProduct
            itemView.title_favorite.text = product.productName




            /*itemView.dislike_favorite.setOnClickListener {
                listener?.onRemove(position)
                //myList.removeAt(position)

            }*/
            itemView.view_detail.setOnClickListener {
                listener?.onDetail(position)
            }


            Picasso
                .with(mContext)
                .load(Config.IMAGE_URL+product.image)
                .into(itemView.image_favorite)

        }
    }
}
