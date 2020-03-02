package com.yyz.project_1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.yyz.project_1.R
import com.yyz.project_1.dbmanager.DBHelper
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //var handler = Handler()



        var thread = Thread(){
            kotlin.run{
                for (i in 1..4){
                    Thread.sleep(1000)

                    runOnUiThread {
                        when(i){

                            1 -> {
                                text_view_splash.text =" 3"
                            }
                            2 -> {
                                text_view_splash.text =" 2"
                            }
                            3 -> {
                                text_view_splash.text ="1"
                            }

                        }
                    }
                }
                auth = FirebaseAuth.getInstance()

                if(auth.currentUser!=null){
                    startActivity(Intent(this,CategoryActivity::class.java))
                }else{


                    startActivity(Intent(this,Login2Activity::class.java))

                }


                finish()
            }
        }

        thread.start()



    }
}
