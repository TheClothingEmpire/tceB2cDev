package com.clothingempire.clothingempire.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager

import com.clothingempire.clothingempire.databinding.ActivitySoldProductsDetailsBinding
import com.clothingempire.clothingempire.models.Shop
import com.clothingempire.clothingempire.models.SoldProduct
import com.clothingempire.clothingempire.ui.adapters.VariantAdaptor
import com.clothingempire.clothingempire.utils.Constants
import com.clothingempire.clothingempire.utils.FirebaseClass
import java.text.SimpleDateFormat
import java.util.*

class SoldProductsDetailsActivity : BaseActivity() {
    var _binding:ActivitySoldProductsDetailsBinding?=null
    val binding get()=_binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivitySoldProductsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(intent.hasExtra(Constants.SOLD_PRODUCTS)){
            val item=intent.getParcelableExtra<SoldProduct>(Constants.SOLD_PRODUCTS)!!
            binding.tvProductItemName.text=item.title
            binding.tvProductItemPrice.text=item.price
            binding.tvSoldProductDetailsId.text=item.id
            // Date Format in which the date will be displayed in the UI.
            val dateFormat = "dd MMM yyyy HH:mm"
            // Create a DateFormatter object for displaying date in specified format.
            val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
            val calendar: Calendar = Calendar.getInstance()
            calendar.timeInMillis = item.order_date
            binding.tvSoldProductDetailsDate.text= formatter.format(calendar.time)
            val adapter=VariantAdaptor(this,item.variant)
            binding.rvSoldProductsVariant.adapter=adapter
            binding.rvSoldProductsVariant.layoutManager=LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false)
            binding.tvSoldProductShippingCharge.text=item.shipping_charge
            binding.tvSoldProductSubTotal.text=item.sub_total_amount
            binding.tvSoldProductTotalAmount.text=item.total_amount
            Constants().loadImage(this,item.image,binding.ivProductItemImage)
            FirebaseClass().getShopByID(this,item.shopID)
        }
    }

    fun getShopSuccess(shop:Shop) {
        //customProgressDialogCloser()
        binding.tvSoldDetailsFullName.text=shop.name
        binding.tvSoldDetailsAddress.text=shop.location
        binding.tvSoldDetailsAdditionalNote.text=shop.gst
        binding.tvSoldDetailsMobileNumber.text=shop.contactNumber


    }
}