package com.clothingempire.clothingempire.ui.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.clothingempire.clothingempire.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPasswordActivity : BaseActivity() {
    lateinit var auth:FirebaseAuth
    private var binding:ActivityForgotPasswordBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.btnResetPassword?.setOnClickListener {
            auth= Firebase.auth
            val email=binding?.etEmail?.text.toString().trim { it <= ' ' }
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task->
                if(task.isSuccessful){
                    showErrorSnackBar("Password reset link has been sent to your registered mail id. ", false)
                    Toast.makeText(this, "Password reset link has been sent to your registered mail id. ", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
                else{
                    Log.e("ResetPassword",task.exception?.message.toString())
                }
            }

        }
    }
}