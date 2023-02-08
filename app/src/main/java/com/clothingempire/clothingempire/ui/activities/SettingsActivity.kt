package com.clothingempire.clothingempire.ui.activities

import android.content.Intent
import android.location.Address
import android.os.Bundle
import android.view.View
import com.clothingempire.clothingempire.R
import com.clothingempire.clothingempire.databinding.ActivitySettingsBinding
import com.clothingempire.clothingempire.models.User
import com.clothingempire.clothingempire.utils.Constants
import com.clothingempire.clothingempire.utils.FirebaseClass
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : BaseActivity() ,View.OnClickListener{
    var binding:ActivitySettingsBinding?=null
    var mUser: User?=null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding= ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupActionBar(binding!!.toolbarSettingsActivity,"Settings")
        getUserDetails()
        binding?.btnLogout?.setOnClickListener(this)
        binding?.tvEdit?.setOnClickListener(this)
        binding?.llAddress?.setOnClickListener(this)
    }
    fun getUserDetails(){

        FirebaseClass().getUserByUserId(this)

    }
    fun userDetailSuccess(user:User){

        mUser=user
        Constants().loadImage(this,user.image,binding?.ivUserPhoto!!)
        binding?.tvName?.text="${user.firstName} ${user.lastName}"
        binding?.tvGender?.text=user.gender
        binding?.tvEmail?.text=user.email
        binding?.tvMobileNumber?.text="${user.mobile}"

    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_logout->{
                FirebaseAuth.getInstance().signOut()
                val intent=Intent(this,LogInActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            R.id.tv_edit->{
                val intent=Intent(this,UpdateUserProfileActivity::class.java)
                intent.putExtra(Constants.USER_PARCELABLE,mUser)
                startActivity(intent)
            }
            R.id.ll_address->startActivity(Intent(this,ShopActivity::class.java))
        }
    }
}