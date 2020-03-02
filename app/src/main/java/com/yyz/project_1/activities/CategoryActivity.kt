package com.yyz.project_1.activities

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.youth.banner.loader.ImageLoader
import com.yyz.project_1.R
import com.yyz.project_1.adapters.CategoryAdapter
import com.yyz.project_1.app.Enpoint
import com.yyz.project_1.dbmanager.DBHelper
import com.yyz.project_1.itemdecoration.SpacesItemDecoration
import com.yyz.project_1.models.Category
import com.yyz.project_1.models.CategoryList
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.menu_cart_layout.view.*

class CategoryActivity : AppCompatActivity(),CategoryAdapter.OnAdapterClickListener {
    override fun onItemClicked(position: Int) {
        var intent = Intent(this, SubCategoryActivity::class.java)
        intent.putExtra("position",position)
        startActivity(intent)
    }


    lateinit var adapterCategory: CategoryAdapter
    var myList: ArrayList<Category> = ArrayList()
    var textViewCount : TextView? =null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        auth= FirebaseAuth.getInstance()

        setImageSlider()
        setToolBar()
        setCategoryAdapter()
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

    private fun setCategoryAdapter() {
        adapterCategory = CategoryAdapter(this,myList)
        adapterCategory.setOnAdapterClickListner(this)

        recycler_view_category.layoutManager= LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recycler_view_category.adapter = adapterCategory


        var space = 12
        recycler_view_category.addItemDecoration(SpacesItemDecoration(space));


        setCategory()

    }

    private fun setCategory() {

        var requestQueue = Volley.newRequestQueue(this)
        var url = Enpoint.getCategory()
        var stringRequest = StringRequest(
            Request.Method.GET,
            url,
            Response.Listener {
                Log.d("category data", it.toString())

                var gson = GsonBuilder().create()
                var categoryList = gson.fromJson(it.toString(), CategoryList::class.java)
                Log.d("categoryList",categoryList.toString())

                myList = categoryList.data
                Log.d("myList",myList.toString())
                adapterCategory.setData(myList)


            },
            Response.ErrorListener {
                Log.e("error", it.message)
            })
        requestQueue.add(stringRequest)

    }

    private fun setToolBar() {
        var toolbar =tool_bar

        toolbar.title = "Home"
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
            textViewCount?.visibility=View.GONE
        }else{
            textViewCount?.visibility=View.VISIBLE
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
                //finish()
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

    private fun setImageSlider() {
        val bannerArray = arrayListOf<Int>(R.drawable.p_1,R.drawable.p_2,R.drawable.p_3)
        banner_category
            .setImages(bannerArray)
            .isAutoPlay(true)
            .setDelayTime(3000)
            .setImageLoader(object: ImageLoader(){
                override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
                    imageView?.setImageResource(path as Int)
                }
            })
        banner_category.setIndicatorGravity(BannerConfig.CENTER)
        banner_category.setBannerAnimation(Transformer.Default)
        banner_category.start()
    }

    override fun onRestart() {
        super.onRestart()
        setToolBar()
        setBottomNavigation()
    }
}
