package com.clothingempire.clothingempire.ui.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.bumptech.glide.Glide
import com.clothingempire.clothingempire.R
import com.clothingempire.clothingempire.databinding.ActivityUpdateUserPorfileBinding
import com.clothingempire.clothingempire.models.User
import com.clothingempire.clothingempire.utils.Constants
import com.clothingempire.clothingempire.utils.FirebaseClass
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UpdateUserProfileActivity : BaseActivity(),View.OnClickListener {
    var binding:ActivityUpdateUserPorfileBinding?=null
    var mUser:User?=null
    private var fbsImage:String?=null
    private var saveImage: Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityUpdateUserPorfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)


        if(intent.hasExtra(Constants.USER_PARCELABLE)){
            mUser=intent.getParcelableExtra(Constants.USER_PARCELABLE)
        }
        if(mUser!!.userUpdated>0){
            binding?.tvTitle?.text="Update User Profile"
        }else{
            binding?.tvTitle?.text="Complete User Profile"
        }
        Glide.with(this)
            .load(mUser!!.image)
            .fitCenter()
            .placeholder(R.drawable.ic_user_placeholder)
            .into(binding?.ivUserPhoto!!)
        binding?.etFirstName?.isEnabled=false
        binding?.etFirstName?.setText(mUser?.firstName)
        binding?.etLastName?.isEnabled=false
        binding?.etLastName?.setText(mUser?.lastName)
        binding?.etEmail?.isEnabled=false
        binding?.etEmail?.setText(mUser?.email)
        binding?.etMobileNumber?.setText((mUser?.mobile).toString())
        if(mUser?.gender=="male"){
            binding?.rbMale?.isChecked = true
        }else{
            binding?.rbFemale?.isChecked=true
        }
        binding?.tvShop?.setOnClickListener (this)
        binding?.ivUserPhoto?.setOnClickListener(this)
        binding?.btnSubmit?.setOnClickListener(this)
    }

    private fun uploadImage(){

        customProgressDialogOpener("please wait")
        if(saveImage!=null){
            val sref:StorageReference=
                FirebaseStorage.getInstance()
                    .reference.child("userImage"+System.currentTimeMillis()
                            +"."+getFileExtension(saveImage))
            sref.putFile(saveImage!!)
                .addOnSuccessListener { taskSnapshot ->
                    Log.e(
                        "Firebase Image URL",
                        taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                    )
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                            uri -> Log.i("imageUri",uri.toString())
                        fbsImage=uri.toString()
                        updateFirestoreUser()
                    }
                    customProgressDialogCloser()
                }.addOnFailureListener{
                        exception->
                    Log.i("image",exception.message.toString())
                    //Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                    customProgressDialogCloser()
                }
        }else{
            //Toast.makeText(this, "no image found", Toast.LENGTH_SHORT).show()
            updateFirestoreUser()
            startActivity(intent)
        }

    }
    private fun getFileExtension(uri: Uri?):String?{
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri!!))
    }
    private fun updateFirestoreUser(){
        val userHashMap=HashMap<String,Any>()

        if(fbsImage!=null){
            userHashMap[Constants.IMAGE]=fbsImage!!
            //Toast.makeText(this, "inside update"+fbsImage, Toast.LENGTH_SHORT).show()

        }
        if(binding?.rbMale?.isChecked!!){
            userHashMap[Constants.GENDER]="male"
        }else{
            userHashMap[Constants.GENDER]="female"
        }
        userHashMap[Constants.LAST_UPDATED]=System.currentTimeMillis()
        userHashMap[Constants.USER_UPDATED]=1
        userHashMap[Constants.MOBILE]=(binding?.etMobileNumber?.text.toString()).toLong()
        FirebaseClass().updateFirestoreUser(this,userHashMap)
        setResult(Activity.RESULT_OK)
        startActivity(Intent(this,DashboardActivity::class.java))
    }


    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.iv_user_photo->Constants().choosePhotoFromGallery(this)
            R.id.tv_shop->{
                var intent= Intent(this,ShopSignIn::class.java)
                intent.putExtra(Constants.USER_PARCELABLE,mUser)
                startActivity(intent)
            }
            R.id.btn_submit->{
                if(binding?.etMobileNumber?.text.toString().isNotEmpty()){
                        uploadImage()
                    startActivity(Intent(this,DashboardActivity::class.java))
                    finish()
                    }else{
                    Toast.makeText(this, "enter mobile number", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== Constants.Gallery){
            if(data!=null){
                saveImage=data.data
                Glide.with(this)
                    .load(saveImage)
                    .fitCenter()
                    .placeholder(R.drawable.ic_user_placeholder)
                    .into(binding?.ivUserPhoto!!)



            }
        }

    }
}