package com.yyz.project_1.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth

import com.yyz.project_1.R
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account.view.*

/**
 * A simple [Fragment] subclass.
 */
class AccountFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_account, container, false)

        auth = FirebaseAuth.getInstance()

        view.user_email_account.text=auth.currentUser?.email
        view.user_name_account.text=auth.currentUser?.displayName

        view.change.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_account,AccountChangeFragment()).commit()
        }



        return view
    }


}
