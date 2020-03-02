package com.yyz.project_1.adapters

import android.app.AlertDialog
import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yyz.project_1.R
import com.yyz.project_1.app.Config
import com.yyz.project_1.dbmanager.DBHelper
import com.yyz.project_1.models.CartItem
import com.yyz.project_1.models.Product
import kotlinx.android.synthetic.main.button_click_inside.view.*
import kotlinx.android.synthetic.main.category_view_adapter.view.*
import kotlinx.android.synthetic.main.product_view_vadapter.view.*


class ProductAdapter(var mContext: Context, var myList:ArrayList<Product> ): RecyclerView.Adapter<ProductAdapter.MyViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.product_view_vadapter,parent,false)
        var myViewHolder = MyViewHolder(view)
        return myViewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var product = myList.get(position)
        holder.bindView(product,position)

    }


    var listener: OnAdapterClickListener? = null


    override fun getItemCount(): Int {
        return myList.size
    }




    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindView(product: Product,position: Int){
            var count:Int

            var dbHelper = DBHelper(mContext)
            if(dbHelper.isItemInCart(product.productName)){
                var cursor = dbHelper.QueryRecord(product.productName)
                cursor!!.moveToFirst()

                count = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_COUNT))
                cursor.close()
                itemView.add_action_subcategory.visibility = View.GONE
                itemView.button_layout.visibility = View.VISIBLE
                itemView.text_view_quantity.text = count.toString()

            }else{
                count = 0
                itemView.add_action_subcategory.visibility = View.VISIBLE
                itemView.button_layout.visibility = View.GONE
            }

            Log.d("already_count",count.toString())





            itemView.image_product
            itemView.title_prodcut.text=product.productName
            itemView.image_product.setOnClickListener {
                val dialog = AlertDialog.Builder(mContext)
                dialog.setMessage(product.description).create().show()
            }
            itemView.currentprice_product.text="$"+product.price.toString()
            itemView.originprice_product.text="$"+product.mrp.toString()
            itemView.originprice_product.paint.setFlags(Paint. STRIKE_THRU_TEXT_FLAG )
            itemView.discount_product.text ="Save $"+String.format("%.2f",(product.mrp-product.price))


            itemView.add_action_subcategory.setOnClickListener {
                count = count+1
                itemView.add_action_subcategory.visibility = View.GONE
                itemView.button_layout.visibility = View.VISIBLE
                itemView.text_view_quantity.text = count.toString()
                var dbHelper = DBHelper(mContext)
                var cartItem = CartItem(product.subId,product.productName,product.image,product.price,count)
                dbHelper.insertRecord(cartItem)
                listener?.onCountChangeClicked()


            }


            itemView.button_quantity_minus.setOnClickListener {
                count = count -1
                if(count == 0){
                    itemView.add_action_subcategory.visibility = View.VISIBLE
                    itemView.button_layout.visibility = View.GONE
                    var dbHelper = DBHelper(mContext)
                    dbHelper.deleteRecord(product.productName)
                }else{
                    itemView.add_action_subcategory.visibility = View.GONE
                    itemView.button_layout.visibility = View.VISIBLE
                    itemView.text_view_quantity.text = count.toString()
                    var dbHelper = DBHelper(mContext)
                    var cartItem = CartItem(product.subId,product.productName,product.image,product.price,count)
                    dbHelper.updateRecord(cartItem)

                }
                listener?.onCountChangeClicked()


            }
            itemView.button_quantity_add.setOnClickListener {
                count = count+1
                itemView.text_view_quantity.text = count.toString()
                var dbHelper = DBHelper(mContext)
                var cartItem = CartItem(product.subId,product.productName,product.image,product.price,count)
                dbHelper.updateRecord(cartItem)
                //mContext.onCreate()
                listener?.onCountChangeClicked()


            }



            Picasso
                .with(mContext)
                .load(Config.IMAGE_URL+product.image)
                .into(itemView.image_product)
            itemView.setOnClickListener{
                listener?.onItemClicked(position)
            }

            //Log.d("count",count.toString())


        }

    }



    fun setData(list: ArrayList<Product>) {
        myList = list
        notifyDataSetChanged()
    }


    interface OnAdapterClickListener{
        fun onItemClicked(position: Int)
        fun onCountChangeClicked()
    }

    fun setOnAdapterClickListner(onAdapterClickListener: OnAdapterClickListener){
        listener = onAdapterClickListener
    }

}