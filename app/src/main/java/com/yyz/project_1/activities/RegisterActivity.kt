package com.yyz.project_1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.yyz.project_1.R
import com.yyz.project_1.app.Enpoint
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        button_register_login.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }

        button_register_register.setOnClickListener {

            registerNewUser()



        }


    }

    private fun registerNewUser() {
        var firstName = name_register.text.toString()
        var email = email_register.text.toString()
        var password = password_register.text.toString()
        var mobile = mobile_register.text.toString()

        var params = HashMap<String, String>()
        params.put("email", email)
        params.put("password", password)
        params.put("firstName", firstName)
        params.put("mobile", mobile)

        var data = JSONObject(params as Map<*, *>)



        var requestQueue = Volley.newRequestQueue(this)

        var url = Enpoint.getRegister()

        var request = JsonObjectRequest(Request.Method.POST,
            url,
            data,
            Response.Listener {
                Toast.makeText(this,"Register successfully，Returning to the login page",Toast.LENGTH_SHORT).show()
                var handler =Handler()
                handler.postDelayed(
                    {

                        startActivity(Intent(this,LoginActivity::class.java))

                    },3000)
            },
            Response.ErrorListener {
                Toast.makeText(this, "Already exist, please try again",Toast.LENGTH_SHORT).show()
            })

        requestQueue.add(request)
    }
}
