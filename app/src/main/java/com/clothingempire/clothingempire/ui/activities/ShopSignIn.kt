package com.clothingempire.clothingempire.ui.activities

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.clothingempire.clothingempire.R
import com.clothingempire.clothingempire.databinding.ActivityShopSignInBinding
import com.clothingempire.clothingempire.models.Shop
import com.clothingempire.clothingempire.models.User
import com.clothingempire.clothingempire.utils.Constants
import com.clothingempire.clothingempire.utils.FirebaseClass
import com.google.firebase.auth.FirebaseAuth

class ShopSignIn : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private var binding:ActivityShopSignInBinding?=null
    private var newShopFlag:Boolean?=null
    private var mUser:User?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        if(intent.hasExtra(Constants.USER_PARCELABLE)){
            mUser=intent.getParcelableExtra(Constants.USER_PARCELABLE)
        }
        super.onCreate(savedInstanceState)
        binding= ActivityShopSignInBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupActionBar(binding!!.toolbarShopSignInActivity,"Shop Register")
        newShopFlag=true
        binding?.rgShop?.setOnCheckedChangeListener { _, checkedID ->
            if(checkedID== R.id.rb_new_Shop){
                setNewShopView()
            }else if(checkedID== R.id.rb_existing_Shop){
                setExistingShop()
            }
        }
        binding?.btnRegister?.setOnClickListener {
            //Toast.makeText(this, "inside update button listener", Toast.LENGTH_SHORT).show()
            if(newShopFlag!!){

                registerShop()
            }else{
                Toast.makeText(this, "${binding?.etShopGst?.text.toString()}", Toast.LENGTH_SHORT).show()
                assignShopToUser(binding?.etShopGst?.text.toString())
                finish()
            }
        }
    }
    private fun setExistingShop() {
        binding?.tilShopName?.visibility= View.GONE
        binding?.tilShopConfirmPassword?.visibility= View.GONE
        binding?.tilShopContact?.visibility= View.GONE
        newShopFlag=false

    }

    private fun setNewShopView() {
        binding?.tilShopName?.visibility= View.VISIBLE
        binding?.tilShopConfirmPassword?.visibility= View.VISIBLE
        binding?.tilShopContact?.visibility= View.VISIBLE
        newShopFlag=true

    }
    private fun validateShopDetails(): Boolean {
        //Toast.makeText(this, "shop validate started", Toast.LENGTH_SHORT).show()
        return when {
            TextUtils.isEmpty(binding?.etShopContact?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_shop_contact), true)
                false
            }

            TextUtils.isEmpty(binding?.etShopName?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_shop_name), true)
                false
            }

            TextUtils.isEmpty(binding?.etShopGst?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_gst_number), true)
                false
            }

            TextUtils.isEmpty(binding?.etShopPassword?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_shop_password), true)
                false
            }

            TextUtils.isEmpty(binding?.etShopConfirmPassword?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_confirm_shop_password),
                    true
                )
                false
            }
            binding?.etShopPassword?.text.toString().trim { it <= ' ' }.length<6->{
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_shop_password_minimum_length),
                    true
                )
                false
            }
            binding?.etShopPassword?.text.toString().trim { it <= ' ' }
                    != binding?.etShopConfirmPassword?.text.toString()
                .trim { it <= ' ' } -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_shop_password_and_confirm_password_mismatch),
                    true
                )
                false
            }


            else -> {

                true
            }
        }
    }
    fun registerShop(){
        //Toast.makeText(this, "register shop", Toast.LENGTH_SHORT).show()
        if(validateShopDetails()){
            customProgressDialogOpener("saving shop details")
            val gst=binding?.etShopGst?.text.toString().trim { it <= ' ' }
            val location=binding?.etShopLocation?.text.toString().trim { it <= ' ' }
            val shopPassword=binding?.etShopPassword?.text.toString().trim { it <= ' ' }
            val shopName=binding?.etShopName?.text.toString().trim { it <= ' ' }
            val contact=binding?.etShopContact?.text.toString().trim { it <= ' ' }

            val shop= Shop(name=shopName,
                gst = gst,
                password = shopPassword,
                location = location,
                contactNumber = contact,
                createdBy = FirebaseAuth.getInstance().uid!!
            )

            if(FirebaseClass().validateShop(this,shop)){
                assignShopToUser(shop.gst)


            }
            this.finish()

        }
    }
    fun assignShopToUser(gst:String){
        //auth = Firebase.auth
        val uid=FirebaseAuth.getInstance().uid

        val uidHashMap=HashMap<String,Any>()
        val array=mUser?.shopList!!
        array.add(gst)
        uidHashMap[Constants.SHOP_LIST]=array

        Log.e("assignShopToUser",uidHashMap[Constants.ID].toString())
        FirebaseClass().updateFirestoreUser(this,uidHashMap)
        val sharedPreferences=this.getSharedPreferences(
            Constants.CLOTHING_EMPIRE_SHARED_PREFERENCES,
            Context.MODE_PRIVATE)

        customProgressDialogCloser()
        finish()

    }
}