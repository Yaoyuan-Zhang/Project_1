package com.yyz.project_1.checkoutfragments


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.*

import com.yyz.project_1.R
import com.yyz.project_1.ordermodels.ShippingAddress
import kotlinx.android.synthetic.main.fragment_address.*
import kotlinx.android.synthetic.main.fragment_address.view.*

/**
 * A simple [Fragment] subclass.
 */
class AddressFragment : Fragment() {

    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_address, container, false)

        firebaseDatabase= FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("user_address")

        view.check_last_address.visibility=View.GONE


        var sharedPreferences = activity!!.getSharedPreferences("loginuser",Context.MODE_PRIVATE)
        var user_email= sharedPreferences!!.getString("lastuser","")
        //var user_email_string = user_email.toString()
        var user_email_string = user_email!!.substringBefore(".")



        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    for (h in p0.children){
                        var shippingAddress = h.getValue(ShippingAddress::class.java)
                        if (shippingAddress!!.user_email.equals(user_email)){
                            view.check_last_address.visibility=View.VISIBLE
                            //Log.d("user_emial",shippingAddress.user_email)
                            view.check_last_address.text = "${shippingAddress.address1}\n${shippingAddress.address2}\n${shippingAddress.city},${shippingAddress.State} ${shippingAddress.zipcode}"
                            //view.check_last_address.text="${shippingAddress.user_email}\n"
                        }
                    }

                }

            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d("databaseerror",p0.message)

            }


        })





        view.address_submit.setOnClickListener {


            if (check_last_address.isChecked==false){
                //new_address_linear.visibility = View.VISIBLE


                var address_1 = view.address_1.text.toString()
                var address_2 = view.address_2.text.toString()
                var city = view.city.text.toString()
                var state = view.state.text.toString()
                var zipcode = view.zip_code.text.toString()

                if(address_1 == "" || city=="" ||state=="" || zipcode==""){
                    Toast.makeText(activity,"invalid address, please check again!",Toast.LENGTH_SHORT).show()
                }
                else{
                    firebaseDatabase= FirebaseDatabase.getInstance()
                    databaseReference = firebaseDatabase.getReference("user_address")


                    var shippingAddress = ShippingAddress(user_email.toString(),address_1,address_2,city,state,zipcode)
                    databaseReference.child(user_email_string).setValue(shippingAddress)

                    activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_checkout_status,PaymentFragment()).addToBackStack("").commit()

                }





            }else{
                activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_checkout_status,PaymentFragment()).addToBackStack("").commit()

            }










            /*var sp = activity!!.getSharedPreferences("PAYMENT", Context.MODE_PRIVATE)
            var editor = sp.edit()


            editor.putString("address_1",address_1)
            editor.putString("address_2",address_2)
            editor.putString("city",city)
            editor.putString("state",state)
            editor.putString("zipcode",zipcode)

            editor.commit()
*/




        }

        return view
    }


}
