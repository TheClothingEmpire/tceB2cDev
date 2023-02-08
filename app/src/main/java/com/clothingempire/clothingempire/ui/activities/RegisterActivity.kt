package com.clothingempire.clothingempire.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.clothingempire.clothingempire.R
import com.clothingempire.clothingempire.databinding.ActivityRegisterBinding
import com.clothingempire.clothingempire.models.User
import com.clothingempire.clothingempire.utils.Constants
import com.clothingempire.clothingempire.utils.FirebaseClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : BaseActivity() {
    var binding:ActivityRegisterBinding?=null
    private var userType:String?=null
    companion object{
        const val TAG="RegisterActivity"
    }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        userType="A"
        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        setupActionBar()
        binding?.btnRegister?.setOnClickListener {
            registerUser()
        }
        binding?.rgUserType?.setOnCheckedChangeListener{ _,checkId->
            if(checkId==R.id.rb_administrator){
                userType="A"
            }else if(checkId==R.id.rb_standard_user){
                userType="S"
            }
        }

        binding?.tvLogin?.setOnClickListener {
            val intent= Intent(this,LogInActivity::class.java)
            startActivity(intent)
            finish()
        }

    }



    private fun setupActionBar() {
        var toolbar_register_activity=binding?.toolbarRegisterActivity!!
        setSupportActionBar(toolbar_register_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_primary_color_back_24dp)
        }

        toolbar_register_activity.setNavigationOnClickListener { onBackPressed() }
    }
    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding?.etFirstName?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(binding?.etLastName?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(binding?.etEmail?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(binding?.etPassword?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            TextUtils.isEmpty(binding?.etConfirmPassword?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_confirm_password),
                    true
                )
                false
            }
            binding?.etPassword?.text.toString().trim { it <= ' ' }.length<6->{
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_password_minimum_length),
                    true
                )
                false
            }
            binding?.etPassword?.text.toString().trim { it <= ' ' }
                    != binding?.etConfirmPassword?.text.toString()
                .trim { it <= ' ' } -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_password_and_confirm_password_mismatch),
                    true
                )
                false
            }
            !binding?.cbTermsAndCondition!!.isChecked -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_agree_terms_and_condition),
                    true
                )
                false
            }


            else -> {
                showErrorSnackBar("Your details are valid.", false)
                true
            }
        }
    }

    private fun registerUser(){
        if(validateRegisterDetails()){
            customProgressDialogOpener("Registering User")
            auth = Firebase.auth
            val email=binding?.etEmail?.text.toString().trim { it <= ' ' }
            val firstName=binding?.etFirstName?.text.toString().trim { it <= ' ' }
            val lastname=binding?.etLastName?.text.toString().trim { it <= ' ' }

            val password=binding?.etConfirmPassword?.text.toString()
                .trim { it <= ' ' }
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { task ->

                        Log.d(TAG, "createUserWithEmail:success")
                        val uid=FirebaseAuth.getInstance().uid!!
                        val user = User(
                            id=uid,
                            firstName = firstName,
                            lastName = lastname,
                            email=email,
                            creationDate=System.currentTimeMillis(),
                            userType =userType!!

                        )
                        FirebaseClass().registerUser(this,user)
                        val intent=Intent(this,LogInActivity::class.java)
                        intent.putExtra(Constants.UID,user)
                        startActivity(intent)
                        finish()
                    }.addOnFailureListener {exception->
                        // If sign in fails, display a message to the user.
                        Log.w(TAG,  exception.message.toString())
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_LONG).show()
                        showErrorSnackBar(exception.message.toString(), true)
                    }
                    customProgressDialogCloser()
                }

        }

    fun userRegistrationsuccess() {
        Toast.makeText(this, " usr has successfully registered please log in to continue",
            Toast.LENGTH_LONG).show()
        customProgressDialogCloser()
            //FirebaseAuth.getInstance().signOut()
    }

}