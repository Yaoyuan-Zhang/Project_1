package com.yyz.project_1.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.yyz.project_1.R
import com.yyz.project_1.adapters.CartAdapter
import com.yyz.project_1.dbmanager.DBHelper
import com.yyz.project_1.models.CartItem
import com.yyz.project_1.models.CartItemList
import com.yyz.project_1.models.Product
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.menu_cart_layout.view.*

class CartActivity : AppCompatActivity(),CartAdapter.OnCalculateClickListener {



    lateinit var chartItemAdapter: CartAdapter
    var cartItemList=CartItemList()
    var myList: ArrayList<CartItem> = ArrayList()
    var totalbefortax:Double= 0.0
    var tax:Double =0.0
    var totalaftertax:Double =0.0
    private lateinit var auth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)


        firebaseDatabase= FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("items")

        setToolBar()
        setBottomNavigation()
        auth= FirebaseAuth.getInstance()

        var dbHelper = DBHelper(this)
        var cursor = dbHelper.readRecord()


        if(cursor!!.count==0){
            Toast.makeText(this,"cart is empty", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
        else {
            cursor!!.moveToFirst()

            //myList.add(cursor.getColumnIndex())
            var catid =(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_SUBID)))
            var productName = (cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PRODUCTNAME)))
            var price = cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_PRICE))
            var image = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IMAGE))
            var count = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_COUNT))
            var cartItem = CartItem(catid,productName,image,price,count)
            myList.add(cartItem)

            while (cursor.moveToNext()) {
                var catid =(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_SUBID)))
                var productName = (cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PRODUCTNAME)))
                var price = cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_PRICE))
                var image = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IMAGE))
                var count = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_COUNT))
                var cartItem = CartItem(catid,productName,image,price,count)
                myList.add(cartItem)
            }
            cursor.close()





            chartItemAdapter = CartAdapter(this, myList)

            chartItemAdapter.setOnCalculateClickListner(this)






            list_view_chart.adapter = chartItemAdapter





            for (item in myList) {
                totalbefortax = totalbefortax + item.count * item.price
            }
            tax = totalbefortax*0.08
            totalaftertax = totalbefortax+tax

            total_before_cart.text = "Total before tax : $" + String.format("%.2f", totalbefortax)
            tax_cart.text = "Tax (8%) : $" +String.format("%.2f",tax)
            total_after_cart.text = "Total : $"+ String.format("%.2f",totalaftertax)

            var sp = getSharedPreferences("PAYMENT", Context.MODE_PRIVATE)
            sp.edit().putString("paymentcount",String.format("%.2f", totalaftertax)).commit()

            clear_button.setOnClickListener {
                dbHelper.deleteAllRecord()
                var intent = Intent(this, CategoryActivity::class.java)
                startActivity(intent)

            }
        }

        checkout_cart.setOnClickListener {
            var sharedPreferences = getSharedPreferences("loginuser",MODE_PRIVATE)
            var user_email= sharedPreferences!!.getString("lastuser","")
            var user_email_string = user_email!!.substringBefore(".")
            cartItemList.data=myList
            databaseReference.child(user_email_string).setValue(cartItemList)

            var sp = getSharedPreferences("itemamount", Context.MODE_PRIVATE)
            sp.edit().putFloat("itemamount",totalaftertax.toFloat()).commit()


            finish()
            startActivity(Intent(this,CheckOutActivity::class.java))
        }
        /*checkout_cart.setOnLongClickListener {
            Toast.makeText(this,"right",Toast.LENGTH_SHORT).show()
            return@setOnLongClickListener true
        }*/




    }


    private fun setToolBar() {
        var toolbar =tool_bar

        toolbar.title = "Cart"
        setSupportActionBar(toolbar)

        toolbar.inflateMenu(R.menu.my_menu)


        toolbar.setNavigationOnClickListener() {
            onBackPressed()


        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.cart_menu,menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.action_home -> {
                startActivity(Intent(this, CategoryActivity::class.java))
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


    override fun onRemove(position: Int) {
        //Toast.makeText(this,position.toString(),Toast.LENGTH_SHORT).show()
        myList.removeAt(position)
        chartItemAdapter = CartAdapter(this, myList)
        chartItemAdapter.setOnCalculateClickListner(this)
        list_view_chart.adapter = chartItemAdapter

    }

    override fun onClicked(price: Double) {
        totalbefortax = totalbefortax +price
        if(totalbefortax==-0.00){
            totalbefortax=0.00
        }

        tax = totalbefortax*0.08
        totalaftertax = totalbefortax+tax
        Log.d("price",price.toString())

        total_before_cart.text = "Total before tax : $"+ String.format("%.2f",totalbefortax)
        tax_cart.text = "Tax (8%) : $" +String.format("%.2f",tax)
        total_after_cart.text = "Total : $"+ String.format("%.2f",totalaftertax)
        var sp = getSharedPreferences("PAYMENT", Context.MODE_PRIVATE)
        sp.edit().putString("paymentcount",String.format("%.2f", totalaftertax)).commit()
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

}
