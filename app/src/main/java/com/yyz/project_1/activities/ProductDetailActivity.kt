package com.yyz.project_1.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import com.yyz.project_1.R
import com.yyz.project_1.adapters.ProductAdapter
import com.yyz.project_1.app.Enpoint
import com.yyz.project_1.dbmanager.DBHelper
import com.yyz.project_1.models.CartItem
import com.yyz.project_1.models.FavoriteProduct
import com.yyz.project_1.models.Product
import com.yyz.project_1.models.ProductList
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.button_click_inside.*
import kotlinx.android.synthetic.main.button_click_inside.view.*
import kotlinx.android.synthetic.main.fragment_common.view.*
import kotlinx.android.synthetic.main.menu_cart_layout.view.*
import java.lang.StringBuilder

class ProductDetailActivity : AppCompatActivity() {

    var myList: ArrayList<Product> = ArrayList()
    var textViewCount : TextView? =null
    private lateinit var auth: FirebaseAuth
    var product_subId:Int?=0
    var product_name:String = ""



    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference_favorite: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        auth= FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference_favorite=firebaseDatabase.getReference("favorite_list")



        setToolBar()
        setProduct()



        cart_productdetail.setOnClickListener {
            startActivity(Intent(this,CartActivity::class.java))
        }

    }

    override fun onRestart() {
        super.onRestart()
        setProduct()
        setToolBar()
    }

    private fun setProduct(){
        var product : Product?=null
        var sharedPreferences = getSharedPreferences("loginuser", MODE_PRIVATE)
        var user_email= sharedPreferences!!.getString("lastuser","")
        //var user_email_string = user_email.toString()
        var user_email_string = user_email!!.substringBefore(".")



        var intent1 = intent
        var uri = intent1.data
        //Log.d("uri",uri.toString())
        if(uri!=null){
            Log.d("uri",uri.toString())
            product_subId = uri.getQueryParameter("product_subID")!!.toInt()
            product_name=uri.getQueryParameters("product_Name")[0]
            //Log.d("fromuri",product_subId_string+product_name_string)
        }else{
            product_subId= intent.getIntExtra("product_subID",0)

            product_name= intent.getStringExtra("product_name")
        }

        Log.d("here subid and sub name",product_subId.toString()+product_name)






        var requestQueue = Volley.newRequestQueue(this)
        var url = Enpoint.getProduct()+product_subId.toString()
        Log.d("product_url",url)
        var stringRequest = StringRequest(Request.Method.GET,url, Response.Listener {
            var gson = GsonBuilder().create()
            var productList = gson.fromJson(it, ProductList::class.java)

            myList=productList.data
            Log.d("myList",myList.toString())

            for (item in myList){
                Log.d("items",item.toString())
                Log.d("prodcut_name",product_name)
                Log.d("itemproduct_name",item.productName)
                if (item.productName.equals(product_name)){
                    product = item
                }
            }

            Log.d("product_118",product.toString())

            text_name_productdetail.text=product?.productName

            var pricetext=text_price_productdetail.text.toString()
            var priceString = product?.price.toString()
            text_price_productdetail.text="$"+priceString

            text_description_productdetail.text=product?.description
            discount_productdetail.text="Save $"+String.format("%.2f",(product!!.mrp-product!!.price))
            originprice_productdetail.text="$"+product?.mrp.toString()
            originprice_productdetail.paint.setFlags(Paint. STRIKE_THRU_TEXT_FLAG)

            Picasso.with(this).load("http://rjtmobile.com/grocery/images/"+product?.image).into(image_productActivity)


            var count:Int


            var dbHelper = DBHelper(this)
            if(dbHelper.isItemInCart(product!!.productName)){
                var cursor = dbHelper.QueryRecord(product!!.productName)
                cursor!!.moveToFirst()

                count = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_COUNT))
                cursor.close()
                add_action_subcategory.visibility = View.GONE
                button_layout.visibility = View.VISIBLE
                text_view_quantity.text = count.toString()

            }else{
                count = 0
                add_action_subcategory.visibility = View.VISIBLE
                button_layout.visibility = View.GONE
            }



            add_action_subcategory.setOnClickListener {
                count = count+1
                add_action_subcategory.visibility = View.GONE
                button_layout.visibility = View.VISIBLE
                text_view_quantity.text = count.toString()
                var cartItem = CartItem(product!!.subId,product!!.productName,product!!.image,product!!.price,count)
                dbHelper.insertRecord(cartItem)
                updateCartCount()

            }


            button_quantity_minus.setOnClickListener {
                count = count -1
                if(count == 0){
                    add_action_subcategory.visibility = View.VISIBLE
                    button_layout.visibility = View.GONE
                    dbHelper.deleteRecord(product!!.productName)
                }else{
                    add_action_subcategory.visibility = View.GONE
                    button_layout.visibility = View.VISIBLE
                    text_view_quantity.text = count.toString()
                    var cartItem = CartItem(product!!.subId,product!!.productName,product!!.image,product!!.price,count)
                    dbHelper.updateRecord(cartItem)

                }

                updateCartCount()

            }


            button_quantity_add.setOnClickListener {
                count = count+1
                text_view_quantity.text = count.toString()
                var cartItem = CartItem(product!!.subId,product!!.productName,product!!.image,product!!.price,count)
                dbHelper.updateRecord(cartItem)
                updateCartCount()
            }

            fun sendMail() {
                var intentemail = Intent(android.content.Intent.ACTION_SEND)
                intentemail.setType("plain/text")
                intentemail.setType("image/*")
                //intentemail.setType(Intent.EXTRA_MIME_TYPES)
                var subject = "share"
                intentemail.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);

               /* intentemail.putExtra(android.content.Intent.EXTRA_TEXT, Html
                    .fromHtml(StringBuilder()
                    .append("<a href=\\\"http://yyz/project_1/product_detail?product_subID=3&product_Name=Folgers Classic Roast Ground Coffee, Medium Roast\\\">my project</a>").toString()))
                //intentemail.putExtra(android.content.Intent.EXTRA_TEXT, "hi");
*/

                var sendtext = "Great product! Have a try!\n${product?.productName}\n${product?.price}"
                intentemail.putExtra(android.content.Intent.EXTRA_TEXT,sendtext)
               // var past_image = R.drawable.p_2 Par
                intentemail.putExtra(android.content.Intent.EXTRA_STREAM,R.drawable.p_2)
                startActivityForResult(Intent.createChooser(intentemail, "send"),1001);
            }

            /*fun sendSMS(){
                var smsToUri = Uri.parse("smsto:")
                var intentText = Intent(android.content.Intent.ACTION_SEND,smsToUri)
                //intentText.setType("plain/text")
                intentText.setType("vnd.android-dir/mms-sms")
                intentText.putExtra(android.content.Intent.EXTRA_TEXT, Html
                    .fromHtml(StringBuilder()
                        .append("<a href=\\\"yyz://project_1/product_detail?product_subID=3&product_Name=Folgers Classic Roast Ground Coffee, Medium Roast\\\">my project</a>").toString()))


                startActivityForResult(Intent.createChooser(intentText, "send"),1002);
            }*/
            var sp_fa = getSharedPreferences(user_email_string, Context.MODE_PRIVATE)

            var iflike = sp_fa.getBoolean(product_name,false)

            if(iflike ==false){
                favorite_select.setImageResource(R.drawable.ic_favorite)

            }else{
                favorite_select.setImageResource(R.drawable.ic_heart_24dp)


            }




            favorite_select.setOnClickListener {
                if(iflike ==false){
                    favorite_select.setImageResource(R.drawable.ic_heart_24dp)

                    sp_fa.edit().putBoolean(product_name,true).commit()


                    //databaseReference_favorite.child()

                    //sendSMS()

                    //dialog.
                    //var loveList =ArrayList<Product>


                    //var shippingAddress = ShippingAddress(user_email.toString(),address_1,address_2,city,state,zipcode)

                    var favoriteProduct = FavoriteProduct(product!!,user_email)
                    databaseReference_favorite.child(user_email_string+product!!._id).setValue(favoriteProduct)

                    iflike=true
                }else{
                    favorite_select.setImageResource(R.drawable.ic_favorite)
                    sp_fa.edit().putBoolean(product_name,false).commit()
                    databaseReference_favorite.child(user_email_string+product!!._id).removeValue()
                    iflike=false
                }

            }


            share_select.setOnClickListener {
                sendMail()
            }









        }, Response.ErrorListener {

        })

        requestQueue.add(stringRequest)
        progress_bar.visibility = View.GONE


    }




    private fun setToolBar() {
        var toolbar =tool_bar

        toolbar.title = "Product Detail"
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
