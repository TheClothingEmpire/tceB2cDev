package com.clothingempire.clothingempire.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.clothingempire.clothingempire.R
import com.clothingempire.clothingempire.databinding.ActivityMyOrderDetailsBinding
import com.clothingempire.clothingempire.models.Shop
import com.clothingempire.clothingempire.ui.adapters.CartListAdapter
import com.clothingempire.clothingempire.utils.Constants
import com.clothingempire.clothingempire.utils.FirebaseClass
import com.myshoppal.models.Order
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MyOrderDetailsActivity : BaseActivity() {
    var _binding:ActivityMyOrderDetailsBinding?=null
    private val binding get()=_binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityMyOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(intent.hasExtra(Constants.ORDERS)){
            val order=intent.getParcelableExtra<Order>(Constants.ORDERS)!!
            val shopID=order.address
            Toast.makeText(this, shopID, Toast.LENGTH_SHORT).show()
            try{
            FirebaseClass().getShopByID(this,shopID)}
            catch (e:Exception){
                Log.e("whatever", e.message.toString())
            }
            setupUI(order)
        }

    }
    private fun setupUI(orderDetails: Order) {

        binding.tvOrderDetailsId.text = orderDetails.title

        val dateFormat = "dd MMM yyyy HH:mm"
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = orderDetails.order_datetime

        val orderDateTime = formatter.format(calendar.time)
        binding.tvOrderDetailsDate.text = orderDateTime
        // END


        // START
        // Get the difference between the order date time and current date time in hours.
        // If the difference in hours is 1 or less then the order status will be PENDING.
        // If the difference in hours is 2 or greater then 1 then the order status will be PROCESSING.
        // And, if the difference in hours is 3 or greater then the order status will be DELIVERED.

        val diffInMilliSeconds: Long = System.currentTimeMillis() - orderDetails.order_datetime
        val diffInHours: Long = TimeUnit.MILLISECONDS.toHours(diffInMilliSeconds)
        Log.e("Difference in Hours", "$diffInHours")
        binding.tvOrderStatus.text = resources.getString(R.string.order_status_pending)
        binding.tvOrderStatus.setTextColor(
            ContextCompat.getColor(
                this@MyOrderDetailsActivity,
                R.color.pallete_five
            )
        )

        // END

        binding.rvMyOrderItemsList.layoutManager = LinearLayoutManager(this@MyOrderDetailsActivity)
        binding.rvMyOrderItemsList.setHasFixedSize(true)

        val cartListAdapter =
           CartListAdapter(this, orderDetails.items)
        binding.rvMyOrderItemsList.adapter = cartListAdapter


        binding.tvMyOrderDetailsAddress.text =orderDetails.address




        binding.tvOrderDetailsSubTotal.text = orderDetails.sub_total_amount
        binding.tvOrderDetailsShippingCharge.text = orderDetails.shipping_charge
        binding.tvOrderDetailsTotalAmount.text = orderDetails.total_amount
    }

    fun getShopSuccess(shop: Shop) {
        try{
            binding.tvMyOrderDetailsAddress.text=shop.location
            binding.tvMyOrderDetailsAdditionalNote.text=shop.gst
            binding.tvMyOrderDetailsFullName.text=shop.name
            binding.tvMyOrderDetailsMobileNumber.text=shop.contactNumber
        }catch (e:Exception){
            Log.e("logError",e.message.toString())
        }

    }
}