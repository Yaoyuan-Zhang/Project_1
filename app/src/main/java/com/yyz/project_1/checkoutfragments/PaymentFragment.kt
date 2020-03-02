package com.yyz.project_1.checkoutfragments


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.yyz.project_1.R
import kotlinx.android.synthetic.main.fragment_payment.view.*

/**
 * A simple [Fragment] subclass.
 */
class PaymentFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view= inflater.inflate(R.layout.fragment_payment, container, false)

        //var count = 0

        view.button_payment.setOnClickListener {
            var  myList :ArrayList<String> = ArrayList()
            if(view.paypal_box.isChecked){
                myList.add("paypal")

                /*var uri = Uri.parse("https://www.paypal.com/")
                startActivityForResult(Intent(Intent.ACTION_VIEW,uri),200)*/
            }
            if(view.bankchecking_box.isChecked){
                myList.add("banck checking")
            }
            if(view.credit_box.isChecked){
                myList.add("credit card")
            }
            if(view.payinperson_box.isChecked){
                myList.add("pay in person")
            }
            if (myList.size != 1){
                Toast.makeText(context,"Please choose only one payment method",Toast.LENGTH_SHORT).show()
            }else{
                var sp = activity!!.getSharedPreferences("PAYMENT", Context.MODE_PRIVATE)
                sp.edit().putString("payment",myList[0]).commit()
                activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_checkout_status,ConfirmFragment()).addToBackStack("").commit()

            }





        }





        return view
    }




}
