package com.clothingempire.clothingempire.ui.activities

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.clothingempire.clothingempire.databinding.ActivitySplashBinding
import com.clothingempire.clothingempire.utils.FirebaseClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.clothingempire.clothingempire.models.User
import com.clothingempire.clothingempire.utils.Constants
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class SplashActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
// ...
// Initialize Firebase Auth

    private var binding:ActivitySplashBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding= ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val currentUser = auth.currentUser

        //Toast.makeText(this, currentUser.toString(), Toast.LENGTH_SHORT).show()
        Handler().postDelayed(
            {

                if(currentUser != null){
                    Log.e("test",FirebaseClass().getCurrentUserId())
                    FirebaseClass().getUserByUserId(this)
                    //startActivity(Intent(this, DashboardActivity::class.java))

                }else
                {
                    startActivity(Intent(this, LogInActivity::class.java))
                }



            },2500
        )
        val typeFace:Typeface= Typeface.createFromAsset(assets,"montserrat_bold.ttf")
        binding?.tvAppName?.typeface=typeFace
    }
    fun userTypeChecker(user:User){
        if(user.userType=="A"){
            startActivity(Intent(this, DashboardActivity::class.java))
        }else{
            startActivity(Intent(this,WSDashboardActivity::class.java))
        }
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener{ token ->
                Log.e("fcmToken",token)
                if(user.fcmToken!=token)
                {
                    updateFCMToken(token)
                }
            }
        finish()

    }
    fun updateFCMToken(token: String) {

        val userHashMap = HashMap<String, Any>()
        userHashMap[Constants.FCM_TOKEN] = token

        // Update the data in the database.
        // Show the progress dialog.

        //customProgressDialogOpener("please wait")
        FirebaseClass().updateFirestoreUser(this, userHashMap)
        FirebaseClass().updateShop(this, userHashMap)
    }
}