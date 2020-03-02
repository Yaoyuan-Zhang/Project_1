package com.yyz.project_1.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.yyz.project_1.R
import com.yyz.project_1.app.Config
import com.yyz.project_1.app.Enpoint
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        password_login.setTransformationMethod(PasswordTransformationMethod.getInstance())

        var sp = getSharedPreferences(Config.PREF_FILE, Context.MODE_PRIVATE)


        button_register_login.setOnClickListener {
            var intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

        button_login_login.setOnClickListener {
            var email = email_login.text.toString()
            var password = password_login.text.toString()

            checkLogin(email,password)


        }


    }

    private fun checkLogin(email :String, password:String) {
        var param = HashMap<String,String>()
        param.put("email",email)
        param.put("password",password)

        var data = JSONObject(param as Map < *,*>)

        var url = Enpoint.getLogin()

        var requestQueue = Volley.newRequestQueue(this)

        var request = JsonObjectRequest(Request.Method.POST,
            url,
            data,
            Response.Listener{
                email_login.text.clear()
                password_login.text.clear()
                startActivity(Intent(this,CategoryActivity::class.java))

            },
            Response.ErrorListener {
                Toast.makeText(this,"Please check your email/password",Toast.LENGTH_LONG).show()
            })

        requestQueue.add(request)

    }
}
