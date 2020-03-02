package com.yyz.project_1.checkoutfragments


import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.*

import com.yyz.project_1.R
import com.yyz.project_1.activities.CategoryActivity
import com.yyz.project_1.adapters.ConfirmFragmentAdapter
import com.yyz.project_1.dbmanager.DBHelper
import com.yyz.project_1.models.CartItem
import com.yyz.project_1.models.CartItemList
import com.yyz.project_1.models.Product
import com.yyz.project_1.ordermodels.Order
import com.yyz.project_1.ordermodels.ShippingAddress
import com.yyz.project_1.ordermodels.SummaryProduct
import kotlinx.android.synthetic.main.fragment_confirm.view.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class ConfirmFragment : Fragment() {
    //var itemList = CartItemList?()

    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference_address: DatabaseReference
    lateinit var databaseReference_items: DatabaseReference
    lateinit var databaseReference_order: DatabaseReference
    lateinit var orderAddress: ShippingAddress


    var myList: ArrayList<CartItem> = ArrayList()
    lateinit var ConfirmItemAdapter: ConfirmFragmentAdapter


    var paymentafterdelivery:Double =0.0

    //var myList: ArrayList<Category> = ArrayList()
    var summaryProductList : ArrayList<SummaryProduct> =ArrayList()



    private var listener: OnFragmentInteractionListener? = null
    interface OnFragmentInteractionListener {

        fun onFragmentInteraction()
    }




    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnFragmentInteractionListener){
            listener=context
        }
    }

    override fun onDetach(){
        super.onDetach()
        listener = null
    }








    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        var view =inflater.inflate(R.layout.fragment_confirm, container, false)


        firebaseDatabase= FirebaseDatabase.getInstance()
        databaseReference_address = firebaseDatabase.getReference("user_address")
        databaseReference_items = firebaseDatabase.getReference("items")
        databaseReference_order = firebaseDatabase.getReference("order")


        var sharedPreferences = activity!!.getSharedPreferences("loginuser",Context.MODE_PRIVATE)
        var user_email= sharedPreferences!!.getString("lastuser","")
        //var user_email_string = user_email.toString()
        var user_email_string = user_email!!.substringBefore(".")


        databaseReference_items.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    for (h in p0.children){
                        if (h.key.equals(user_email_string)){
                            var itemList = h.getValue(CartItemList::class.java)
                            Log.d("itemList",itemList.toString())
                            for (cartItem in itemList!!.data){
                                var productName = cartItem.productName
                                var price = cartItem.price
                                var count = cartItem.count
                                summaryProductList.add(SummaryProduct(productName,price,count))
                            }
                            Log.d("summaryProductList",summaryProductList.toString())
                        }
                    }

                }

            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d("databaseerror",p0.message)

            }


        })





        var sp = activity!!.getSharedPreferences("PAYMENT", Context.MODE_PRIVATE)
        var deliveryway = sp.getString("deliver","")
        var paymentway = sp.getString("payment","")
        var orderStatus =""

        if(deliveryway.equals("delivery")){
            databaseReference_address.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()){
                        for (h in p0.children){
                            var shippingAddress = h.getValue(ShippingAddress::class.java)
                            if (shippingAddress!!.user_email.equals(user_email)){
                                orderAddress=shippingAddress
                                view.final_address.text =  "Address:\n${shippingAddress.address1}\n${shippingAddress.address2}\n${shippingAddress.city},${shippingAddress.State} ${shippingAddress.zipcode}"
                            }
                        }

                    }

                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.d("databaseerror",p0.message)

                }


            })
            var paymentcount = sp.getString("paymentcount","")
             paymentafterdelivery = paymentcount!!.toDouble() + 3.99
            view.total_payment.text="Total : $ " + String.format("%.2f",paymentafterdelivery) + "(delivery fee :$3.99)"
            orderStatus = "Shipped"




        }else{
            orderAddress = ShippingAddress("","","","","","")
            view.final_address.text ="Address : Pick up"
            var paymentcount = sp.getString("paymentcount","")
            paymentafterdelivery = paymentcount!!.toDouble()
            view.total_payment.text="Total : $ $paymentcount"
            orderStatus = "Waiting for Pickup"
        }

        //var paymentmethod = sp.getString("payment","")


        view.payment_method.text = "Pay by: $paymentway"
        var inorder =""

        if(paymentway == "credit card"){
            view.card_information.visibility = View.VISIBLE
            //var cardnumber = view.number_card.text.toString()
            //inorder = cardnumber.substring(0,4)+"xxxxxxxx"+cardnumber.substring(12,15)

        }else{
            view.card_information.visibility=View.GONE

        }



        var mContext = context
        var dbHelper = DBHelper(mContext!!)
        var cursor = dbHelper.readRecord()




            cursor!!.moveToFirst()

            //myList.add(cursor.getColumnIndex())
            var catid = (cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_SUBID)))
            var productName = (cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PRODUCTNAME)))
            var price = cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_PRICE))
            var image = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IMAGE))
            var count = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_COUNT))
            var cartItem = CartItem(catid, productName, image, price, count)
            myList.add(cartItem)

            while (cursor.moveToNext()) {
                var catid = (cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_SUBID)))
                var productName =
                    (cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PRODUCTNAME)))
                var price = cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_PRICE))
                var image = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IMAGE))
                var count = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_COUNT))
                var cartItem = CartItem(catid, productName, image, price, count)
                myList.add(cartItem)
            }
            cursor.close()





            ConfirmItemAdapter = ConfirmFragmentAdapter(mContext, myList)

            view.product_views.adapter = ConfirmItemAdapter









        view.place_order.setOnClickListener {
            var mContext=context
            var orderId =getNow()+"_"+user_email_string
            Log.d("orderId",orderId)


            //paymentway = paymentway +
            if(paymentway == "credit card") {
                //view.card_information.visibility = View.VISIBLE
                var cardnumber = view.number_card.text.toString()
                inorder = cardnumber.substring(0, 4) + "xxxxxxxx" + cardnumber.substring(12, 16)
            }
            paymentway = paymentway + inorder


            var order = Order(orderId,orderStatus,paymentafterdelivery,paymentway!!,summaryProductList,orderAddress,user_email)
            databaseReference_order.child(order.orderId).setValue(order)

            var dbHelper = DBHelper(mContext!!)
            dbHelper.deleteAllRecord()
            //activity!!.startActivity(Intent(mContext,CategoryActivity::class.java))

            listener?.onFragmentInteraction()

            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_checkout_status,ThankYouFragment()).addToBackStack("").commit()

        }




        return view
    }


    fun getNow(): String {
        if (android.os.Build.VERSION.SDK_INT >= 24){
            return SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        }else{
            var tms = Calendar.getInstance()
            return tms.get(Calendar.YEAR).toString() + "-" + tms.get(Calendar.MONTH).toString() + "-" + tms.get(Calendar.DAY_OF_MONTH).toString() + " " + tms.get(Calendar.HOUR_OF_DAY).toString() + ":" + tms.get(Calendar.MINUTE).toString() +":" + tms.get(Calendar.SECOND).toString()
        }

    }


}
