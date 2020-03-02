package com.yyz.project_1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.view.MenuItemCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.yyz.project_1.R
import com.yyz.project_1.dbmanager.DBHelper
import com.yyz.project_1.fragments.AccountFragment
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.menu_cart_layout.view.*

class AccountActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var textViewCount : TextView? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        auth = FirebaseAuth.getInstance()

        setToolBar()
        setFragment()
        setBottomNavigation()
    }

    private fun setBottomNavigation() {
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation_category)
        val resource = this.resources
        val csl = resource.getColorStateList(R.color.navigation_menu_item_color)
        bottomNavigation.setItemTextColor(csl)
        bottomNavigation.menu.getItem(0).setChecked(true)




        //bottomNavigation.setOnNavigationItemReselectedListener()
        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_page-> {
                    startActivity(Intent(this,CategoryActivity::class.java))

                    return@OnNavigationItemSelectedListener true
                }
                R.id.account_page -> {

                    startActivity(Intent(this,AccountActivity::class.java))

                    return@OnNavigationItemSelectedListener true
                }
                R.id.cart_page -> {

                    startActivity(Intent(this,CartActivity::class.java))

                    return@OnNavigationItemSelectedListener true
                }

            }
            false
        }

        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }


    private fun setFragment() {
        var fm = supportFragmentManager
        fm.beginTransaction().add(R.id.fragment_account,AccountFragment()).commit()
    }

    private fun setToolBar() {
        var toolbar =tool_bar

        toolbar.title = "Account"
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

                startActivity(Intent(this,Login2Activity::class.java))
                finish()

            }
            R.id.action_order_history ->{
                startActivity(Intent(this,OrderActivity::class.java))
            }

            R.id.action_favorite ->{
                startActivity(Intent(this,MyFavoriteProductActivity::class.java))
            }
            else->{}
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onRestart() {
        super.onRestart()
        setToolBar()
        setBottomNavigation()

    }
}
