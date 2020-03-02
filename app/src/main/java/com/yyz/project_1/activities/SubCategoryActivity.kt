package com.yyz.project_1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import com.yyz.project_1.R
import com.yyz.project_1.adapters.CommonFragmentAdapter
import com.yyz.project_1.adapters.ProductAdapter
import com.yyz.project_1.app.Enpoint
import com.yyz.project_1.dbmanager.DBHelper
import com.yyz.project_1.fragments.CommonFragment
import com.yyz.project_1.itemdecoration.SpacesItemDecoration
import com.yyz.project_1.models.SubCategory
import com.yyz.project_1.models.SubcategoryList
import kotlinx.android.synthetic.main.activity_sub_category.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.menu_cart_layout.view.*
import java.util.ArrayList

class SubCategoryActivity : AppCompatActivity() , CommonFragment.OnFragmentInteractionListener{
    override fun onFragmentInteraction() {
        //Toast.makeText(this,"clicked",Toast.LENGTH_SHORT).show()
        updateCartCount()
    }


    lateinit var adapterSubCategory: CommonFragmentAdapter
    var myList: ArrayList<SubCategory> = ArrayList()
    var textViewCount : TextView? =null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category)
        setSubCategory()
        auth= FirebaseAuth.getInstance()

        setToolBar()
    }


    private fun setToolBar() {
        var toolbar =tool_bar

        //var subid = intent.getIntExtra("position",0)+1

        toolbar.title = "SubCategory"
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


    private fun setSubCategory(){
        //var id =intent?.getExtra("position")
        var requestQueue = Volley.newRequestQueue(this)
        var subid = intent.getIntExtra("position",0)+1
        //var url = "https://apolis-grocery.herokuapp.com/api/subcategory/"+subid.toString()
        var url = Enpoint.getSubCategory()+subid.toString()
        Log.d("sub_url",url)
        var stringRequest = StringRequest(Request.Method.GET,url,Response.Listener {
            var gson = GsonBuilder().create()
            var subCategoryList = gson.fromJson(it,SubcategoryList::class.java)

            myList=subCategoryList.data
            var fm=supportFragmentManager
            adapterSubCategory=CommonFragmentAdapter(fm)



            for (i in 0..myList.size-1){
                Log.d("catName",myList[i].subName)
                adapterSubCategory.addFragment(myList[i].subName,myList[i].subId)
                Log.d("subId",myList[i].subId.toString())
            }


            view_pager_subcategory.adapter=adapterSubCategory
            tab_layout_subcategory.setupWithViewPager(view_pager_subcategory)



        },Response.ErrorListener {

        })

        requestQueue.add(stringRequest)
        progress_bar.visibility = View.GONE




    }


    override fun onRestart() {
        super.onRestart()
        updateCartCount()
        //Toast.makeText(this,"restart",Toast.LENGTH_SHORT).show()
        view_pager_subcategory.adapter=adapterSubCategory
        tab_layout_subcategory.setupWithViewPager(view_pager_subcategory)
    }
}
