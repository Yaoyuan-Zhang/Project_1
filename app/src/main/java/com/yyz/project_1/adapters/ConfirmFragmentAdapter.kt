package com.yyz.project_1.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.squareup.picasso.Picasso
import com.yyz.project_1.R
import com.yyz.project_1.app.Config
import com.yyz.project_1.models.CartItem
import kotlinx.android.synthetic.main.confirmfragment_product_adapter.view.*

class ConfirmFragmentAdapter (var mContext: Context, var myList:ArrayList<CartItem>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = LayoutInflater.from(mContext).inflate(R.layout.confirmfragment_product_adapter,parent,false)

        var cartItem = myList.get(position)
        var image_path = Config.IMAGE_URL + cartItem.image

        view.text_title_confirm.text = cartItem.productName
        view.text_price_confirm.text = "$"+cartItem.price.toString()
        view.text_count_confirm.text = "x"+cartItem.count.toString()



       // Picasso.with(mContext).load()
        Picasso
            .with(mContext)
            .load(image_path)
            .into(view.image_confirm)
        return view


    }

    override fun getItem(position: Int): Any {
        return myList.get(position)
   }

    override fun getItemId(position: Int): Long {
        return position.toLong()
   }

    override fun getCount(): Int {
        return myList.size
   }
}