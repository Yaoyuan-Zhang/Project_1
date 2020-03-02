package com.yyz.project_1.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.yyz.project_1.R
import com.yyz.project_1.dbmanager.DBHelper
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login2.*

class Login2Activity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

        auth = FirebaseAuth.getInstance()
        login_password.setTransformationMethod(PasswordTransformationMethod.getInstance())

        var sp = getSharedPreferences("loginuser", Context.MODE_PRIVATE)
        var ifremembered = sp.getBoolean("ifremembered",false)

        if(ifremembered==true){
            var email = sp.getString("remembered_email","")
            var password = sp.getString("remembered_password","")
            login_email.setText(email)
            login_password.setText(password)
            remember_checkbox.isChecked=true

        }





        login_button.setOnClickListener {
            var email:String = login_email.text.toString()
            var password:String = login_password.text.toString()

            if(email.equals("") || password.equals("")){
                Toast.makeText(applicationContext, "Login Failed", Toast.LENGTH_SHORT).show()
            }else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                        override fun onComplete(p0: Task<AuthResult>) {
                            if (p0.isSuccessful) {

                                //var sp = getSharedPreferences("loginuser", Context.MODE_PRIVATE)
                                var lastuser = sp.getString("lastuser","")

                                if(!email.equals(lastuser)){
                                    sp.edit().putString("lastuser",email).commit()
                                    var dbHelper = DBHelper(this@Login2Activity)
                                    dbHelper.deleteAllRecord()
                                }

                                if(remember_checkbox.isChecked){
                                    sp.edit().putString("remembered_email",email).commit()
                                    sp.edit().putString("remembered_password",password).commit()
                                    sp.edit().putBoolean("ifremembered",true).commit()
                                }else{
                                    sp.edit().putBoolean("ifremembered",false).commit()
                                }



                                startActivity(
                                    Intent(
                                        this@Login2Activity,
                                        CategoryActivity::class.java
                                    )
                                )
                                finish()


                               // var sp = getSharedPreferences("loginuser", Context.MODE_PRIVATE)

                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "Login Failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }


                    })
            }

        }

        login_register.setOnClickListener {
            remember_checkbox.isChecked=false
            sp.edit().putBoolean("ifremembered",false).commit()
            startActivity(Intent(this@Login2Activity, Register2Activity::class.java))

        }



    }



    override fun onPause() {
        super.onPause()
        if(remember_checkbox.isChecked==false){
            var sp = getSharedPreferences("loginuser", Context.MODE_PRIVATE)
            sp.edit().putBoolean("ifremembered",false).commit()
        }
        Log.d("login2 Onpasue",remember_checkbox.isChecked.toString())
    }

//    var inputLayoutEmail :TextInputLayout=findViewById(R.id.login_email_input)
   /* fun validateEmail(): Boolean{
        if (login_email.getText().toString().trim().isEmpty()) {
            inputLayoutEmail.error = "Yes"
            return false;
        } else {
            inputLayoutEmail.isErrorEnabled = false

        }

        return true;
    }
*/




}
