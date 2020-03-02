package com.yyz.project_1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.view.MenuItemCompat
import com.google.firebase.auth.FirebaseAuth
import com.yyz.project_1.R
import com.yyz.project_1.checkoutfragments.ConfirmFragment
import com.yyz.project_1.checkoutfragments.DeliveryFragment
import com.yyz.project_1.dbmanager.DBHelper
import com.yyz.project_1.fragments.AccountFragment
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.menu_cart_layout.view.*

class CheckOutActivity : AppCompatActivity() ,ConfirmFragment.OnFragmentInteractionListener{
    override fun onFragmentInteraction() {
        updateCartCount()
    }

    private lateinit var auth: FirebaseAuth
    var textViewCount : TextView? =null
    private var stackNumber:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)
        auth= FirebaseAuth.getInstance()
        setFragment()
        setToolBar()
    }



    private fun setFragment() {
        var fm = supportFragmentManager
        fm.beginTransaction().add(R.id.fragment_checkout_status, DeliveryFragment()).commit()
    }

    private fun setToolBar() {
        var toolbar =tool_bar

        toolbar.title = "Checkout"
        setSupportActionBar(toolbar)

        toolbar.inflateMenu(R.menu.my_menu)


        toolbar.setNavigationOnClickListener() {
            onBackPressed()


        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }




    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.my_menu,menu)

        var item = menu.findItem(R.id.action_cart)
        MenuItemCompat.setActionView(item,R.layout.menu_cart_layout)
        var view = MenuItemCompat.getActionView(item)
        textViewCount = view.text_view__count_menu
        view.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
        updateCartCount()


        return super.onCreateOptionsMenu(menu)
    }

    fun updateCartCount(){
        var dbHelper = DBHelper(this)
        var count = dbHelper.getCartCount()

        if(count ==0){
            textViewCount?.visibility= View.GONE
        }else{
            textViewCount?.visibility= View.VISIBLE
            textViewCount?.text = count.toString()
        }
    }

    override fun onBackPressed() {
        var fm = supportFragmentManager
        stackNumber = fm.backStackEntryCount
        Log.d("stackNumber",stackNumber.toString())
        if (stackNumber < 3){
            super.onBackPressed()
        }else{
            finish()
            startActivity(Intent(this@CheckOutActivity,CategoryActivity::class.java))
            //finish()
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_home -> {
                startActivity(Intent(this, CategoryActivity::class.java))
            }
            R.id.action_cart -> {
                startActivity(Intent(this, CartActivity::class.java))
            }
            R.id.action_settings -> {
                startActivity(Intent(this, AccountActivity::class.java))
            }
            R.id.action_log_out-> {
                auth.signOut()
                //finish()
                startActivity(Intent(this,Login2Activity::class.java))
                finish()
            }
            R.id.action_favorite ->{
                startActivity(Intent(this,MyFavoriteProductActivity::class.java))
            }
            R.id.action_order_history ->{
                startActivity(Intent(this,OrderActivity::class.java))
            }
            else->{}
        }
        return super.onOptionsItemSelected(item)
    }
}

