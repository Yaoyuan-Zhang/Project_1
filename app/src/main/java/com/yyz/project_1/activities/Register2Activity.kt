package com.yyz.project_1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.yyz.project_1.R
import kotlinx.android.synthetic.main.activity_register2.*

class Register2Activity : AppCompatActivity() {
    private lateinit var auth :FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register2)
        auth = FirebaseAuth.getInstance()


        register_button_1.setOnClickListener {
            var email = register_email.text.toString()
            var password = register_password.text.toString()

            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, object :
                OnCompleteListener<AuthResult> {
                override fun onComplete(task: Task<AuthResult>) {
                    if(task.isSuccessful){
                        Toast.makeText(this@Register2Activity,"Register successfully，Returning to the login page",Toast.LENGTH_SHORT).show()
                        var handler = Handler()
                        handler.postDelayed(
                            {

                                startActivity(Intent(this@Register2Activity,Login2Activity::class.java))

                            },3000)
                        //Toast.makeText(this,"successful",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@Register2Activity,task.toString(), Toast.LENGTH_SHORT).show()

                    }
                }

            })
        }

        register_login.setOnClickListener {
            startActivity(Intent(this,Login2Activity::class.java))
        }
    }
}
