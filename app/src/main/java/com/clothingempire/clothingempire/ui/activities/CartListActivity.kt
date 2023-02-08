package com.clothingempire.clothingempire.ui.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.clothingempire.clothingempire.databinding.ActivityCartListBinding
import com.clothingempire.clothingempire.databinding.PopupAddItemBinding
import com.clothingempire.clothingempire.models.CartItem
import com.clothingempire.clothingempire.models.CartVariant
import com.clothingempire.clothingempire.ui.adapters.CartListAdapter
import com.clothingempire.clothingempire.utils.Constants
import com.clothingempire.clothingempire.utils.FirebaseClass
import kotlin.collections.ArrayList

class CartListActivity : BaseActivity() {
    var _binding:ActivityCartListBinding?=null
    var mcVariant=ArrayList<CartItem>()
    var total=0.0
    val gst=5
    private val binding  get()=_binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        _binding= ActivityCartListBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        binding.btnCheckout.setOnClickListener {
            if(total>3000) {
                intent = Intent(this, ShopActivity::class.java)
                intent.putExtra(Constants.SELECT_SHOP, true)
                startActivity(intent)
                //finish()
                setupActionBar(binding.toolbarCartListActivity, "Cart List")
            }else{
                Toast.makeText(this, "Total should be more than Rs.3000 to check out", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getCartItems()
    }

    fun getCartItemsSuccess(cartList: ArrayList<CartItem>) {
        setupActionBar(binding.toolbarCartListActivity,"Cart List")
        var subTotal=0.0
        //customProgressDialogCloser()
        if(cartList.size>0){
            mcVariant=cartList
            binding.llCheckout.visibility=View.VISIBLE
            binding.rvCartItemsList.visibility=View.VISIBLE
            binding.tvNoCartItemFound.visibility=View.GONE
            val adapter=CartListAdapter(this,cartList)
            binding.rvCartItemsList.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.rvCartItemsList.adapter = adapter
            for(i in cartList){
                for(j in i.cartVariant){
                    val price=i.price.toFloat()
                    var quantity=j.orderQuantity.toInt()
                    subTotal+=(quantity*price)
                }


            }
            total=subTotal+gst*subTotal*0.01
            binding.tvShippingCharge.text=(gst*subTotal*0.01).toString()
            binding.tvSubTotal.text=subTotal.toString()
            binding.tvTotalAmount.text=total.toString()
        }else{
            binding.rvCartItemsList.visibility=View.GONE
            binding.tvNoCartItemFound.visibility=View.VISIBLE
        }
    }
    fun getCartItems(){
        FirebaseClass().getCartItems(this)
    }
    fun updateSuccess(){
        customProgressDialogCloser()
        getCartItems()
    }
    fun customDialog(
        variant: CartVariant,
        list: ArrayList<CartVariant>,
        position: Int,
        cartId: String
    ){
        var variant=variant
        var quantity=variant.orderQuantity.toInt()
        var stock=variant.quantity.toInt()
        val dialog= Dialog(this)
        val binding= PopupAddItemBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.setTitle(variant.name)
        binding.tvCartQuantity.text=quantity.toString()
        binding.ivSelectorColor.setBackgroundColor(Color.parseColor("#"+variant.value))
        if(quantity<stock){
            binding.ibAddCartItem.visibility=View.VISIBLE
        }else{
            binding.ibAddCartItem.visibility=View.GONE
        }
        if(quantity>0){
            binding.ibRemoveCartItem.visibility=View.VISIBLE
        }else{
            binding.ibRemoveCartItem.visibility=View.GONE
        }
        binding.ibAddCartItem.setOnClickListener {
            quantity++
            binding.tvCartQuantity.text=quantity.toString()
            if(quantity<stock){
                binding.ibAddCartItem.visibility=View.VISIBLE
            }else{
                binding.ibAddCartItem.visibility=View.GONE
            }
            if(quantity>0){
                binding.ibRemoveCartItem.visibility=View.VISIBLE
            }else{
                binding.ibRemoveCartItem.visibility=View.GONE
            }

        }
        binding.ibRemoveCartItem.setOnClickListener {
            quantity--
            binding.tvCartQuantity.text=quantity.toString()
            if(quantity<stock){
                binding.ibAddCartItem.visibility=View.VISIBLE
            }else{
                binding.ibAddCartItem.visibility=View.GONE
            }
            if(quantity>0){
                binding.ibRemoveCartItem.visibility=View.VISIBLE
            }else{
                binding.ibRemoveCartItem.visibility=View.GONE
            }
        }
        binding.tvSave.setOnClickListener {
            variant.orderQuantity=quantity.toString()
            list[position]=variant
            val hash=HashMap<String,Any>()
            hash[Constants.CART_VARIANT]=list
            FirebaseClass().updateCartList(this,cartId,hash)
            dialog.dismiss()

        }
        binding.tvCancel.setOnClickListener {
            dialog.cancel()
        }



        dialog.show()


    }
}