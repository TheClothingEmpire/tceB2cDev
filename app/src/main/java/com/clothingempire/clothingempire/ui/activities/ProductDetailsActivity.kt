package com.clothingempire.clothingempire.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import android.widget.ViewAnimator
import androidx.annotation.RequiresApi
import com.clothingempire.clothingempire.R
import com.clothingempire.clothingempire.databinding.ActivityProductDetailsBinding
import com.clothingempire.clothingempire.models.CartItem
import com.clothingempire.clothingempire.models.CartVariant
import com.clothingempire.clothingempire.models.Product
import com.clothingempire.clothingempire.ui.adapters.ViewPagerAdapter
import com.clothingempire.clothingempire.utils.Constants
import com.clothingempire.clothingempire.utils.FirebaseClass
import com.google.firebase.auth.FirebaseAuth

class ProductDetailsActivity : BaseActivity(),View.OnClickListener {
    var _binding:ActivityProductDetailsBinding?=null
    private val binding get() = _binding!!
    private var _mProduct:Product?=null
    private val mProduct get() = _mProduct!!
    private val cartArray:ArrayList<CartVariant> = ArrayList()
    private var cartItem:CartItem?=null
    @RequiresApi(33)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        _binding= ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try{
            setupActionBar(binding.toolbarProductDetailsActivity,"Product Details")
        }catch (e:java.lang.Exception){
            Log.e("setupActionBar",e.message.toString())
        }
        if(intent.hasExtra(Constants.PRODUCTS)){
            Toast.makeText(this, "loading product", Toast.LENGTH_SHORT).show()
            val prodID=intent.getStringExtra(Constants.PRODUCTS)!!
           getproductBYID(prodID)

        }
        //binding.btnAddToCart.visibility=View.VISIBLE
        if(intent.hasExtra(Constants.UID)){
            val createdBy=intent.getStringExtra(Constants.UID)
            if(FirebaseAuth.getInstance().uid==createdBy){
                binding.btnAddToCart.visibility=View.GONE
                binding.btnGoToCart.visibility=View.GONE
            }else{
                binding.btnAddToCart.visibility=View.VISIBLE
                binding.btnGoToCart.visibility=View.GONE

            }
        }else{
            binding.btnAddToCart.visibility=View.VISIBLE
            binding.btnGoToCart.visibility=View.GONE

        }

        binding.btnGoToCart.setOnClickListener(this)
        binding.btnAddToCart.setOnClickListener(this)

    }

    private fun getproductBYID(id:String) {
        customProgressDialogOpener("loading Product details")
        FirebaseClass().getProductByID(this,id)

        //Toast.makeText(this, "getting product", Toast.LENGTH_SHORT).show()
    }

    fun getProductSuccess(product: Product) {
        _mProduct=product
        //Toast.makeText(this, mProduct.title, Toast.LENGTH_SHORT).show()
        //Constants().loadImage(this,mProduct.image[0],binding.ivProductDetailImage)
        val mViewPagerAdapter = ViewPagerAdapter(this, mProduct.image)
        binding!!.viewPagerMain.adapter=mViewPagerAdapter
        binding.tvProductDetailsDescription.text=mProduct.description
        binding.tvProductDetailsPrice.text="Rs."+mProduct.price.toString()
        binding.tvProductDetailsVariantsCount.text=mProduct.variants.size.toString()
        binding.tvProductDetailsTitle.text=mProduct.title
        //binding.btnAddToCart.visibility=View.GONE
        try{
            checkIfProductInCart()
        }catch (e:Exception){
            Log.e("fuckToo",e.message.toString())
        }
        for(variant in mProduct.variants){
           val  cart=CartVariant(
               type = variant.type,
               name=variant.name,
               size = variant.size,
               orderQuantity = "0",
               quantity = variant.quantity,
               value = variant.value
           )
            cartArray.add(cart)

        }
        customProgressDialogCloser()
         cartItem=CartItem(
            user_id = FirebaseAuth.getInstance().uid!!,
            product_id = mProduct.id,
            mProduct.title,
            mProduct.price.toString(),
            mProduct.image[0],
            cartArray
            //mProduct.stockAvailable.toString()

        )
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_add_to_cart->addToCart()
            R.id.btn_go_to_cart->{
                startActivity(Intent(this,CartListActivity::class.java))
                finish()
            }
        }
    }
    private fun checkIfProductInCart(){
        FirebaseClass().checkIfProductInCart(this,mProduct.id)
    }
    private fun addToCart() {
        FirebaseClass().addItemToCart(this,cartItem!!)
    }
    fun addToCartSuccess(){
        customProgressDialogCloser()
        binding.btnGoToCart.visibility=View.VISIBLE
        binding.btnAddToCart.visibility=View.GONE
    }


}