package com.clothingempire.clothingempire.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.clothingempire.clothingempire.databinding.ActivityMainBinding
import com.clothingempire.clothingempire.models.User
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity() {
    private var binding:ActivityMainBinding?=null
    var muMser: User?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        binding?.button?.setOnClickListener {
            //Toast.makeText(this, FirebaseAuth.getInstance().uid!!, Toast.LENGTH_SHORT).show()
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this,LogInActivity::class.java))


        }

    }
    fun setUser(user: User){
        muMser=user
        Toast.makeText(this,"  set user" +
                " ${user.firstName}", Toast.LENGTH_SHORT).show()

    }
}