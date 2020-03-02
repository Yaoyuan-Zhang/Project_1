package com.yyz.project_1.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.yyz.project_1.R
import com.yyz.project_1.adapters.MyFavoriteAdapter
import com.yyz.project_1.dbmanager.DBHelper
import com.yyz.project_1.models.FavoriteProduct
import kotlinx.android.synthetic.main.activity_my_favorite_product.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.menu_cart_layout.view.*

class MyFavoriteProductActivity : AppCompatActivity(),MyFavoriteAdapter.OnDetailListener{



    lateinit var favoriteAdapter : MyFavoriteAdapter
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    var myList : ArrayList<FavoriteProduct> = ArrayList()
    private lateinit var auth: FirebaseAuth
    var textViewCount : TextView? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_favorite_product)

        firebaseDatabase= FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("favorite_list")


        setToolBar()
        setFavorite()
    }

    private fun setFavorite() {
        var sharedPreferences = getSharedPreferences("loginuser", MODE_PRIVATE)
        var user_email= sharedPreferences!!.getString("lastuser","")
        //var user_email_string = user_email.toString()
        var user_email_string = user_email!!.substringBefore(".")


        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){

                    for(h in p0.children){

                        var favoriteProduct = h.getValue(FavoriteProduct::class.java)

                        if (favoriteProduct!!.user.equals(user_email)){


                            myList.add(favoriteProduct!!)

                        }

                    }










                }
                //listView.

                favoriteAdapter =  MyFavoriteAdapter(this@MyFavoriteProductActivity,myList)
                favoriteAdapter.setDetailListner(this@MyFavoriteProductActivity)
                favoriteAdapter.setData(myList)
                favorite_list_favorite.layoutManager = LinearLayoutManager(this@MyFavoriteProductActivity)
                favorite_list_favorite.adapter = favoriteAdapter




            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })

        databaseReference.onDisconnect()



    }




    private fun setToolBar() {
        var toolbar =tool_bar

        toolbar.title = "My Favorite"
        setSupportActionBar(toolbar)

        toolbar.inflateMenu(R.menu.my_menu)

        //toolbar.home


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
                finish()
                startActivity(Intent(this,Login2Activity::class.java))
                //finish()
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


    override fun onDetail(position: Int) {
        var favoriteProduct = myList.get(position)
        var intent = Intent(this, ProductDetailActivity::class.java)

        var product= favoriteProduct.likeProduct
        intent.putExtra("product_subID",product.subId)
        intent.putExtra("product_name",product.productName)

       startActivity(intent)


   }

    override fun onRestart() {
        super.onRestart()
        setToolBar()
        myList=ArrayList()
        setFavorite()
    }





}
