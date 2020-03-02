package com.yyz.project_1.checkoutfragments


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.yyz.project_1.R
import kotlinx.android.synthetic.main.activity_check_out.*
import kotlinx.android.synthetic.main.fragment_delivery.*
import kotlinx.android.synthetic.main.fragment_delivery.view.*

/**
 * A simple [Fragment] subclass.
 */
class DeliveryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_delivery, container, false)

        view.button_delivery.setOnClickListener {
            if(delivery_box.isChecked && pickup_box.isChecked){
                Toast.makeText(context,"Please choose one",Toast.LENGTH_SHORT).show()
            }else if (!delivery_box.isChecked && !pickup_box.isChecked){
                Toast.makeText(context,"Please choose one",Toast.LENGTH_SHORT).show()
            }else{
                if (delivery_box.isChecked){
                    var sp = activity!!.getSharedPreferences("PAYMENT", Context.MODE_PRIVATE)
                    var editor = sp.edit()
                    editor.putString("deliver","delivery")
                    editor.commit()

                    activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_checkout_status,AddressFragment()).addToBackStack("").commit()

                }else{
                    var sp = activity!!.getSharedPreferences("PAYMENT", Context.MODE_PRIVATE)
                    var editor = sp.edit()
                    editor.putString("address","pick up")
                    editor.putString("deliver","pickup")
                    editor.commit()

                    /*var uri = Uri.parse("https://www.google.com/maps/")
                    startActivityForResult(Intent(Intent.ACTION_VIEW,uri),200)

*/



                    activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_checkout_status,PaymentFragment()).addToBackStack("").commit()
                }
            }

        }



        //activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_checkout_status,AddressFragment()).commit()
        return view
    }


}
