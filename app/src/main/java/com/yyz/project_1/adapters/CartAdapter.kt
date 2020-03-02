package com.yyz.project_1.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.squareup.picasso.Picasso
import com.yyz.project_1.R
import com.yyz.project_1.activities.ProductDetailActivity
import com.yyz.project_1.dbmanager.DBHelper
import com.yyz.project_1.models.CartItem
import kotlinx.android.synthetic.main.cart_view_adapter.view.*

class CartAdapter(var mContext: Context, var myList:ArrayList<CartItem>) : BaseAdapter(){

    var listener: OnCalculateClickListener? = null
    interface OnCalculateClickListener{
        fun onClicked(price: Double)
        fun onRemove(position: Int)
    }

    fun setOnCalculateClickListner(onCalculateClickListener:OnCalculateClickListener){
        listener = onCalculateClickListener
    }






    override fun getCount(): Int {
        return myList.size

    }

    override fun getItem(position: Int): Any {
        return myList.get(position)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }



    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = LayoutInflater.from(mContext).inflate(R.layout.cart_view_adapter,parent,false)
        view.text_title_list_view.text=myList.get(position).productName
        view.text_count_list_view.text=myList.get(position).count.toString()
        var thiscount =myList.get(position).count


        view.text_price_list_view.text="$"+String.format("%.2f",(myList.get(position).count*myList.get(position).price)).toString()
        var IMAGE_PATH = "http://rjtmobile.com/grocery/images/"
        var image_path = IMAGE_PATH+myList.get(position).image

        view.add_button.setOnClickListener {
            thiscount=thiscount+1
            var finalcount= thiscount-myList.get(position).count

            var dbHelper = DBHelper(mContext)
            var cartItem = myList.get(position)
            cartItem.count=thiscount
            dbHelper.updateRecord(cartItem)




            myList.get(position).count=thiscount
            var price = finalcount*myList.get(position).price
            listener?.onClicked(price)

            view.text_count_list_view.text=thiscount.toString()

            view.text_price_list_view.text="$"+String.format("%.2f",(thiscount*myList.get(position).price)).toString()

        }

        view.setOnClickListener {
            //Toast.makeText(mContext,"position:"+position.toString(), Toast.LENGTH_SHORT).show()
                    var intent = Intent(mContext, ProductDetailActivity::class.java)

                    var product= myList.get(position)
                    intent.putExtra("product_subID",product.subId)
                    intent.putExtra("product_name",product.productName)

                    mContext.startActivity(intent)


        }


        view.minus_button.setOnClickListener {



            if(thiscount!=0){
                thiscount=thiscount-1
                var finalcount= thiscount-myList.get(position).count

                var dbHelper = DBHelper(mContext)
                var cartItem = myList.get(position)
                cartItem.count=thiscount
                dbHelper.updateRecord(cartItem)





                myList.get(position).count=thiscount
                var price = finalcount*myList.get(position).price
                listener?.onClicked(price)


                view.text_count_list_view.text=thiscount.toString()

                view.text_price_list_view.text="$"+String.format("%.2f",(thiscount*myList.get(position).price)).toString()

                if(thiscount==0){

                    var catItem = myList.get(position)
                    var dbHelper =DBHelper(mContext)
                    dbHelper.deleteRecord(catItem.productName)
                    //Log.d("cartitem",catItem.productName)
                    //myList.remove()
                    //myList.remove(catItem)
                    listener?.onRemove(position)



                }

            }else{
                var dbHelper =DBHelper(mContext)
                dbHelper.deleteRecord(myList.get(position).productName)
            }




        }


        view.remove.setOnClickListener {
            var catItem = myList.get(position)
            var dbHelper =DBHelper(mContext)
            dbHelper.deleteRecord(catItem.productName)
            listener?.onRemove(position)
        }

        Picasso
            .with(mContext)
            .load(image_path)
            .into(view.image_list_view)

        return view
    }



}