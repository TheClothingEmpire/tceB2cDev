package com.clothingempire.clothingempire.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.clothingempire.clothingempire.databinding.ActivityShopBinding
import com.clothingempire.clothingempire.databinding.ItemShopLayoutBinding
import com.clothingempire.clothingempire.models.Shop
import com.clothingempire.clothingempire.ui.adapters.ItemShopListAdapter
import com.clothingempire.clothingempire.utils.Constants
import com.clothingempire.clothingempire.utils.FirebaseClass
import java.util.ArrayList

class ShopActivity :BaseActivity() {
    var _binding:ActivityShopBinding?=null
    val binding get() =_binding!!
    var mSelectShop:Boolean=false
    var mSelectedShopID:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvAddAddress.setOnClickListener {
            startActivity(Intent(this,ShopSignIn::class.java))

        }
        if(intent.hasExtra(Constants.SELECT_SHOP)){
            mSelectShop=intent.getBooleanExtra(Constants.SELECT_SHOP,false)

        }
        if(mSelectShop){

        }
        setupActionBar(binding.toolbarAddressListActivity,"Shop List")
        
    }

    override fun onResume() {
        super.onResume()
        getShopList()
    }

    fun deleteShopSuccess() {
        customProgressDialogCloser()
        getShopList()
    }

    private fun getShopList() {
        customProgressDialogOpener("Loading ShopList")
        FirebaseClass().getShopList(this)
    }


    fun getShopListSuccess(shopList: ArrayList<Shop>) {
        customProgressDialogCloser()
        //binding.tvNoAddressFound.visibility-View.GONE
        if(shopList.size>0){
            binding.rvAddressList.visibility=View.VISIBLE
            binding.tvNoAddressFound.visibility=View.GONE
            val adapter=ItemShopListAdapter(this,shopList)
            binding.rvAddressList.layoutManager=LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false)
            binding.rvAddressList.adapter=adapter
        }else{
            binding.rvAddressList.visibility=View.GONE
            binding.tvNoAddressFound.visibility=View.VISIBLE
        }
    }
}