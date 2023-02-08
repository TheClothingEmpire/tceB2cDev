package com.clothingempire.clothingempire.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.clothingempire.clothingempire.R
import com.clothingempire.clothingempire.databinding.ActivityLogInBinding
import com.clothingempire.clothingempire.models.User
import com.clothingempire.clothingempire.utils.Constants
import com.clothingempire.clothingempire.utils.FirebaseClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class LogInActivity : BaseActivity(),View.OnClickListener {
    var mUserUpdated:Long=0;
    private lateinit var auth: FirebaseAuth
    companion object{
        const val TAG="LoginActivity"
    }
    var binding:ActivityLogInBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding= ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        binding?.btnLogin?.setOnClickListener(this)
        binding?.tvRegister?.setOnClickListener(this)
        binding?.tvForgotPassword?.setOnClickListener(this)

    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.tv_forgot_password->{
                startActivity(Intent(this,ForgotPasswordActivity::class.java))
            }
            R.id.tv_register->{
                val intent=Intent(this,RegisterActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_login->{
                logInUser()
            }

        }
    }
    private fun validateRegisterDetails(): Boolean {
        return when {

            TextUtils.isEmpty(binding?.etEmail?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(binding?.etPassword?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {

                true
            }
        }
    }
    private fun logInUser(){
        if(validateRegisterDetails()){
            customProgressDialogOpener("Authenticating user")
            val email=binding?.etEmail?.text.toString().trim { it <= ' ' }
            val password=binding?.etPassword?.text.toString()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseClass().getUserByUserId(this)

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        customProgressDialogCloser()
                        showErrorSnackBar("Login Failed.", true)

                    }
                }
        }
    }
    fun signInSuccess(loginUser:User){
        Log.d(TAG, "signInWithEmail:success")
        mUserUpdated=loginUser.userUpdated
        //val user = auth.currentUser
        val intent=if(mUserUpdated >0){
            if(loginUser.userType=="A"){
                Intent(this, DashboardActivity::class.java)
            }else{
                Intent(this,WSDashboardActivity::class.java)
            }
        }else{
            Intent(this,UpdateUserProfileActivity::class.java)
        }
        intent.putExtra(Constants.USER_PARCELABLE,loginUser)
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener{ token ->
                Log.e("fcmToken",token)
                if(loginUser.fcmToken!=token)
                {
                    updateFCMToken(token)
                }
            }

        startActivity(intent)
        finish()
        customProgressDialogCloser()

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