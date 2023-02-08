package com.clothingempire.clothingempire.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import com.bumptech.glide.Glide
import com.clothingempire.clothingempire.R
import com.clothingempire.clothingempire.ui.fragments.BaseFragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class Constants {
    companion object{
        const val PRODUCT_PARCEL: String="productParcel"
        const val VARIANT: String="variants"
        const val CART_VARIANT="cartVariant"
        const val LAST_UPDATED="lastUpdated"
        const val CREATION_DATE="creationDate"
        val SOLD_PRODUCTS: String="sold_products"
        val STOCK_AVAILABLE: String="stockAvailable"
        val ORDERS: String="orders"
        val SELECT_SHOP: String="selectShop"
        const val CART_QUANTITY="cart_quantity"
        const val CART_ITEMS="cart_items"
        const val PRODUCT_ID="product_id"
        const val USER_ID="user_id"
        const val PRODUCTS="products"
        const val USERS: String="users"
        const val UID="uid"
        const val SHOP="shop"
        const val GST="gst"
        const val ID="id"
        const val MOBILE="mobile"
        const val CREATED_BY:String="createdBy"
        const val SHOP_LIST="shopList"
        const val LOGGED_IN_USER_NAME:String="logged_in_username"
        const val CLOTHING_EMPIRE_SHARED_PREFERENCES:String="ClothEmpPref"
        const val USER_PARCELABLE="myUser"
        const val IMAGE="image"
        const val NAME="name"
        const val Gallery=1
        const val GENDER="gender"
        const val USER_UPDATED="userUpdated"

        // TODO (Step 1: Add the base url  and key params for sending firebase notification.)
        // START
        const val FCM_BASE_URL:String = "https://fcm.googleapis.com/fcm/send"
        const val FCM_AUTHORIZATION:String = "authorization"
        const val FCM_KEY:String = "key"
        const val FCM_SERVER_KEY:String = "AAAA2ihTCHs:APA91bH9ixLrcQc6ustzMlTvg4SOTXQplvUn9GV1a2mSgMY2r9oWmiet-f9m4bNsxdx4vdgh710e9nnk8_w_8bkXhDG-K8IoNm1eNcz5ZBJdGxQICn0klUlH1-RYKNDqdEnseoS-hnL2"
        const val FCM_KEY_TITLE:String = "title"
        const val FCM_KEY_MESSAGE:String = "message"
        const val FCM_KEY_DATA:String = "data"
        const val FCM_KEY_TO:String = "to"
        // END
        const val PROGEMANAG_PREFERENCES: String = "ProjemanagPrefs"
        const val FCM_TOKEN:String = "fcmToken"
        const val FCM_TOKEN_UPDATED:String = "fcmTokenUpdated"

    }

    fun choosePhotoFromGallery(activity: Activity) {
        // this function is used to upload local image from local disk to firebase(cloud)
        Dexter.withActivity(activity).withPermissions(
            //checking permissions
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) { /* ... */
                if(report!!.areAllPermissionsGranted()){
                   //once we confirm we got all the permissions we open the gallery Intent to load
                       //image from local gallery
                    val intent = Intent()

                    // setting type to select to be image
                    intent.type = "image/*"

                    // allowing multiple image to be selected
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                    intent.action = Intent.ACTION_GET_CONTENT
                    activity.startActivityForResult(
                        Intent.createChooser(intent, "Select Picture"),
                        Constants.Gallery
                    )
                 /*   val galleryIntent= Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activity.startActivityForResult(galleryIntent, Constants.Gallery)*/
                    //now onActivityResult( ) function will take us further
                    //
                    }else{
                    // we will get a toast if we don't get all required permissions
                    Toast.makeText(activity, "all permissions not granted", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                p1: PermissionToken?
            ) {
                showRationaleDialogPermissions(activity)
            }
        }).onSameThread().check()
    }
    private fun showRationaleDialogPermissions(activity:Activity) {
        // this function allows us to ask users to manually assign permissions if missing
        AlertDialog.Builder(activity).setMessage("please provide required"+
                " permissions in Application settings "+
                "to continue further")
            .setPositiveButton("go to settings") {_,_->
                try{
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri= Uri.fromParts("package",activity.packageName,null)
                    intent.data=uri
                    activity.startActivity(intent)
                }catch (e: ActivityNotFoundException){
                    e.printStackTrace()
                }
            }.setNegativeButton("Cancel"){
                    dialog,_->
                dialog.dismiss()
            }.show()
    }
    fun loadImage(activity: BaseFragment, imageUri:String, imageView: ImageView){
        // this function is used to load image from url to imageVIew in an activity
        Glide.with(activity)
            .load(imageUri)
            .fitCenter()
            .placeholder(R.drawable.ic_user_placeholder)
            .into(imageView)
    }
    fun loadImage(activity: Activity, imageUri:String, imageView: ImageView){
        // this function is used to load image from url to imageVIew in fragment
        Glide.with(activity)
            .load(imageUri)
            .fitCenter()
            .placeholder(R.drawable.ic_user_placeholder)
            .into(imageView)
    }
}