package com.yyz.project_1.checkoutfragments


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils

import com.yyz.project_1.R
import com.yyz.project_1.activities.CategoryActivity
import kotlinx.android.synthetic.main.fragment_thank_you.*
import kotlinx.android.synthetic.main.fragment_thank_you.view.*

/**
 * A simple [Fragment] subclass.
 */
class ThankYouFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_thank_you, container, false)

        val animation = AnimationUtils.loadAnimation(activity,R.anim.rotate)
        view.image_thank_you.startAnimation(animation)

        /*var handler = Handler()
        handler.postDelayed(
            {
                activity!!.finish()
                activity!!.startActivity(Intent(activity, CategoryActivity::class.java))

            },5000)*/
        var handler = Handler()

        var thread =Thread(){
            kotlin.run {
                view.image_thank_you.startAnimation(animation)
                for (i in 1..6){
                    Thread.sleep(1000)
                    handler.post {

                        view.thankyou_text.text = "Return to home page in ${6-i} second "
                    }

                }
                activity!!.finish()
                activity!!.startActivity(Intent(activity, CategoryActivity::class.java))
            }
        }
        thread.start()






        return view
    }


}
