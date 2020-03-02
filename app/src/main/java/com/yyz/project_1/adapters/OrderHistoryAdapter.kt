package com.yyz.project_1.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yyz.project_1.R
import com.yyz.project_1.ordermodels.Order
import kotlinx.android.synthetic.main.order_history_view_adapter.view.*

class OrderHistoryAdapter(var mContext:Context, var myList: ArrayList<Order>):RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder>() {
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var order = myList.get(position)
        holder.bindView(order,position)

    }

    override fun getItemCount(): Int {
        return myList.size

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.order_history_view_adapter,parent,false)
        var myViewHolder=MyViewHolder(view)

        return myViewHolder

    }

    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindView(order: Order,position: Int){
            itemView.products_details.visibility=View.GONE
            var orderDetails = "Order Details:\n"
            var shippingAddress = order.shippingAddress

            itemView.order_amount_order_history.text = "Amount: $"+String.format("%.2f",order.amount)
            itemView.order_id_order_history.text = "OrderId: "+order.orderId
            itemView.order_payment_way_order_history.text ="Payment Method: " +order.payment

            var shippingAddress_text = "Address:\n${shippingAddress.address1}\n${shippingAddress.address2}\n${shippingAddress.city} ${shippingAddress.State} ${shippingAddress.zipcode}"

            itemView.order_shipping_address_order_history.text = shippingAddress_text
            itemView.order_status_order_history.text="Status:" +order.orderStatus

            for (item in order.products){

                orderDetails = orderDetails + "${item.productName}  $${item.price}    x${item.count}\n"
            }

            itemView.products_details.text = orderDetails
            Log.d("products",order.products.toString())

           itemView.setOnClickListener {
                if(itemView.products_details.visibility == View.GONE){
                    itemView.products_details.visibility=View.VISIBLE

                }else{
                    itemView.products_details.visibility=View.GONE
                }


            }

        }
    }
}