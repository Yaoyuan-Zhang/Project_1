package com.yyz.project_1.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

import com.yyz.project_1.R
import com.yyz.project_1.activities.CategoryActivity
import kotlinx.android.synthetic.main.fragment_account_change.view.*

/**
 * A simple [Fragment] subclass.
 */
class AccountChangeFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_account_change, container, false)
        auth=FirebaseAuth.getInstance()
        var user = auth.currentUser


        view.user_email_account.text=auth.currentUser?.email


        view.submit_changed.setOnClickListener {


            user?.updatePassword(view.password_account.text.toString())

            startActivity(Intent(activity,CategoryActivity::class.java))

        }





        return view
    }


}
