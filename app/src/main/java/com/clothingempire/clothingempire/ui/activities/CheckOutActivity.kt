package com.clothingempire.clothingempire.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.clothingempire.clothingempire.R
import com.clothingempire.clothingempire.databinding.ActivityCheckOutBinding
import com.clothingempire.clothingempire.databinding.ActivityShopBinding
import com.clothingempire.clothingempire.models.CartItem
import com.clothingempire.clothingempire.models.Shop
import com.clothingempire.clothingempire.models.User
import com.clothingempire.clothingempire.ui.adapters.CartListAdapter
import com.clothingempire.clothingempire.utils.Constants
import com.clothingempire.clothingempire.utils.FirebaseClass
import com.myshoppal.models.Order
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.util.ArrayList
import kotlin.system.measureTimeMillis

class CheckOutActivity :BaseActivity() {
    var _binding: ActivityCheckOutBinding? = null
    val binding get() = _binding!!
    var mCartList: ArrayList<CartItem>? = null
    var address: String = ""
    var mSubtotal = 0.0
    var mTotal = 0.0
    var gst = 5
    var mOrder: Order? = null
    private var mUser:User?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //SendNotificationToUserAsyncTask(mUser!!.fcmToken)
        Log.e("test101", "inside CheckoutActivity")
        _binding = ActivityCheckOutBinding.inflate(layoutInflater)
        setupActionBar(binding.toolbarCheckoutActivity, "Check out")
        setContentView(binding.root)
        if (intent.hasExtra(Constants.SELECT_SHOP)) {
            val id: String = intent.getStringExtra(Constants.SELECT_SHOP)!!
            getShopByID(id)
        }
        getCartList()
        binding.btnPlaceOrder.setOnClickListener {
            saveOder()
        }
        FirebaseClass().getUserByUserId(this)

    }

    fun getShopByID(id: String) {
        //customProgressDialogOpener("Loading")
        FirebaseClass().getShopByID(this, id)
    }

    fun getShopSuccess(shop: Shop) {
        customProgressDialogCloser()
        binding.tvCheckoutAddress.text = shop.location
        binding.tvCheckoutFullName.text = shop.name
        binding.tvMobileNumber.text = shop.contactNumber
        binding.tvCheckoutOtherDetails.text = shop.gst

        address = shop.documentId
    }

    fun getCartList() {
        customProgressDialogOpener("loading cart")
        FirebaseClass().getCartItems(this)
    }

    fun getCartListSuccess(cartList: ArrayList<CartItem>) {
        var total = 0.0

        var subTotal = 0.0
        mCartList = cartList
        for (i in cartList) {
            var quantity = 0
            val price = i.price.toFloat()
            for (j in i.cartVariant) {
                quantity += j.orderQuantity.toInt()

            }
            subTotal += quantity * price

        }
        mSubtotal = subTotal
        total = subTotal + gst * subTotal * 0.01
        mTotal = total
        binding.tvCheckoutSubTotal.text = subTotal.toString()
        binding.tvCheckoutTotalAmount.text = total.toString()
        binding.tvCheckoutShippingCharge.text = gst.toString()
        val adapter = CartListAdapter(this, cartList)
        customProgressDialogCloser()
        binding.rvCartListItems.adapter = adapter
        binding.rvCartListItems.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    }

    fun getCartListSuccess() {
        customProgressDialogCloser()

    }

    fun saveOder() {
        try {
            if (mCartList != null) {
                mOrder = Order(
                    FirebaseClass().getCurrentUserId(),
                    mCartList!!,
                    address,
                    "My order ${System.currentTimeMillis()}",
                    mCartList!![0].image,
                    System.currentTimeMillis(),
                    mSubtotal.toString(),
                    gst.toString(),
                    mTotal.toString()


                )
                customProgressDialogOpener("Saving Order")
                FirebaseClass().saveOder(this, mOrder!!)
            }
        } catch (e: Exception) {
            Log.e("errorOne", e.message.toString())
        }


    }

    fun saveOrderSuccess() {
        customProgressDialogCloser()
        val intent = Intent(this, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        //startActivity(intent)
        Log.e("test101", "inside saveOrderSuccess")

    finish()





    }

    fun updateAllDetails() {
        Log.e("errorOne", "inside update all details")
        FirebaseClass().updateAllDetails(this, mCartList!!, mOrder!!)
    }

    fun getUserSuccess(user:User) {
        mUser=user
    }



}

// END