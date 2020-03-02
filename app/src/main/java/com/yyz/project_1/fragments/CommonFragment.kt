package com.yyz.project_1.fragments


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder

import com.yyz.project_1.R
import com.yyz.project_1.activities.ProductDetailActivity
import com.yyz.project_1.adapters.ProductAdapter
import com.yyz.project_1.adapters.ProductAdapterGrid
import com.yyz.project_1.app.Enpoint
import com.yyz.project_1.itemdecoration.SpacesItemDecoration
import com.yyz.project_1.models.Product
import com.yyz.project_1.models.ProductList
import kotlinx.android.synthetic.main.fragment_common.view.*

/**
 * A simple [Fragment] subclass.
 */
class CommonFragment : Fragment(),ProductAdapter.OnAdapterClickListener,ProductAdapterGrid.OnAdapterClickListener{

    private var listener: OnFragmentInteractionListener? = null
    var sorted = 0
    interface OnFragmentInteractionListener {

        fun onFragmentInteraction()
    }




    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnFragmentInteractionListener){
            listener=context
        }
    }

    override fun onDetach(){
        super.onDetach()
        listener = null
    }






    override fun onCountChangeClicked() {
        //Toast.makeText(activity,"fragment!",Toast.LENGTH_SHORT).show()
        listener?.onFragmentInteraction()

    }

    override fun onItemClicked(position: Int) {
        var intent = Intent(activity, ProductDetailActivity::class.java)
        var product= myList.get(position)
        //intent.putExtra("product",product)
        intent.putExtra("product_subID",product.subId)
        intent.putExtra("product_name",product.productName)
        //activity.
        startActivity(intent)

    }


    var subId:Int?= null
    lateinit var productAdapter:ProductAdapter
    lateinit var productAdapterGrid: ProductAdapterGrid
    var myList: ArrayList<Product> = ArrayList()
    var isLinearLayout = true




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.fragment_common, container, false)
        var mContext = context



        var requestQueue = Volley.newRequestQueue(mContext)
        var url = "https://apolis-grocery.herokuapp.com/api/products/"+subId.toString()
        Log.d("product_url",url)
        var stringRequest = StringRequest(Request.Method.GET,url, Response.Listener {
            var gson = GsonBuilder().create()
            var productList = gson.fromJson(it,ProductList::class.java)

            myList=productList.data
            Log.d("myList",myList.toString())

            productAdapter = ProductAdapter(mContext!!,myList)
            productAdapterGrid= ProductAdapterGrid(mContext!!,myList)

            myList.sortBy { it._id }
            view.sort_by_default.setBackgroundResource(R.drawable.sort_circle)
            view.sort_by_productname.setBackgroundResource(R.drawable.badge_circle)
            view.sort_by_price.setBackgroundResource(R.drawable.badge_circle)

            var ifshowoption = false
            view.sort_by_default.visibility=View.GONE
            view.sort_by_price.visibility=View.GONE
            view.sort_by_productname.visibility=View.GONE

            view.sort_by_what.setOnClickListener {
                if(ifshowoption==false){
                    view.sort_by_default.visibility=View.VISIBLE
                    view.sort_by_price.visibility=View.VISIBLE
                    view.sort_by_productname.visibility=View.VISIBLE
                    ifshowoption=true
                }else{
                    view.sort_by_default.visibility=View.GONE
                    view.sort_by_price.visibility=View.GONE
                    view.sort_by_productname.visibility=View.GONE
                    ifshowoption=false
                }
            }





            view.sort_by_default.setOnClickListener {
                myList.sortBy { it._id }
               // view.sort_by_default.setBackground(R.drawable.sort_circle)
                //view.sort_by_default.setBackgroundColor(Color.LTGRAY)
                view.sort_by_default.setBackgroundResource(R.drawable.sort_circle)
                view.sort_by_productname.setBackgroundResource(R.drawable.badge_circle)
                view.sort_by_price.setBackgroundResource(R.drawable.badge_circle)
                //view.sort_by_price.setBackgroundColor(Color.WHITE)
                //view.sort_by_productname.setBackgroundColor(Color.WHITE)
                productAdapter.setData(myList)
                productAdapterGrid.setData(myList)
            }
            //view.sort_by_price.inflatem
            view.sort_by_price.setOnClickListener {
                myList.sortBy { it.price }
                view.sort_by_price.setBackgroundResource(R.drawable.sort_circle)
                view.sort_by_productname.setBackgroundResource(R.drawable.badge_circle)
                view.sort_by_default.setBackgroundResource(R.drawable.badge_circle)
                productAdapter.setData(myList)
                productAdapterGrid.setData(myList)
            }

            view.sort_by_productname.setOnClickListener {
                myList.sortByDescending { it.price }
                view.sort_by_productname.setBackgroundResource(R.drawable.sort_circle)
                view.sort_by_default.setBackgroundResource(R.drawable.badge_circle)
                view.sort_by_price.setBackgroundResource(R.drawable.badge_circle)
                productAdapter.setData(myList)
                productAdapterGrid.setData(myList)
            }

            /*view.sort_by_default.setOnClickListener {
                if (sorted%3==0){
                    myList.sortBy { it.price }
                    productAdapter.setData(myList)
                    view.sort_by_default.text = "Sort by: Price"
                }else if(sorted%3==1){
                    myList.sortBy { it.productName }
                    productAdapter.setData(myList)
                    view.sort_by_default.text = "Sort by: Product Name"
                }else{
                    myList.sortBy { it._id }
                    productAdapter.setData(myList)
                    view.sort_by_default.text = "Sort by: Default"
                }


                sorted = sorted+1
            }*/

            view.recycler_view_product.layoutManager=LinearLayoutManager(mContext)
            view.recycler_view_product.adapter=productAdapter
            productAdapter.setOnAdapterClickListner(this)
            productAdapter.setData(myList)
            productAdapterGrid.setData(myList)

            view.change_view.setOnClickListener {
                if(isLinearLayout == false){
                    view.recycler_view_product.layoutManager=LinearLayoutManager(mContext)
                    view.recycler_view_product.adapter=productAdapter
                    productAdapter.setOnAdapterClickListner(this)

                    isLinearLayout=true
                }else{
                    view.recycler_view_product.layoutManager=GridLayoutManager(mContext,2)
                    view.recycler_view_product.adapter=productAdapterGrid
                    productAdapterGrid.setOnAdapterClickListner(this)
                    isLinearLayout=false
                }
            }




        },Response.ErrorListener {

        })


        requestQueue.add(stringRequest)






        return view
    }











    companion object {
        @JvmStatic
        fun newInstance(subId:Int)=
            CommonFragment().apply {
                arguments=Bundle().apply{
                    putInt("SubId",subId)


                }
            }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            subId=it.getInt("SubId")

        }
    }


}
