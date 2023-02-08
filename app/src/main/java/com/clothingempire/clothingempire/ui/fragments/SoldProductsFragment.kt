package com.clothingempire.clothingempire.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.clothingempire.clothingempire.R
import com.clothingempire.clothingempire.databinding.FragmentProductsBinding

import com.clothingempire.clothingempire.databinding.FragmentSoldProductsBinding
import com.clothingempire.clothingempire.models.SoldProduct
import com.clothingempire.clothingempire.ui.activities.AddProductsActivity
import com.clothingempire.clothingempire.ui.activities.SettingsActivity
import com.clothingempire.clothingempire.ui.adapters.SoldProductsAdapter
import com.clothingempire.clothingempire.utils.FirebaseClass
import java.lang.Exception


class SoldProductsFragment : BaseFragment(){
    var _binding:FragmentSoldProductsBinding?=null
    val binding get()=_binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       loadSoldProduct()
        _binding = FragmentSoldProductsBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        loadSoldProduct()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSoldProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }
    fun loadSoldProduct(){
        //customProgressDialogOpener("Loading sold Products")
        FirebaseClass().getSoldProducts(this)
        try{

        }catch (e:Exception){
            Log.e("SoldProductFragment",e.message.toString())
        }
    }

    fun getSoldProductListSuccess(orderList: ArrayList<SoldProduct>) {
        Log.e("SoldProductList",orderList.toString())
        customProgressDialogCloser()
        if(orderList.size>0){
            Log.e("SoldProductList",orderList.size.toString())

            binding.rvSoldProductItems.visibility=View.VISIBLE
            binding.tvNoSoldProductsFound.visibility=View.GONE
            val adapter=SoldProductsAdapter(this,orderList)
            binding.rvSoldProductItems.layoutManager=LinearLayoutManager(this.requireContext(),
                LinearLayoutManager.VERTICAL,false)
            binding.rvSoldProductItems.adapter=adapter
        }else{
            binding.rvSoldProductItems.visibility=View.GONE
            binding.tvNoSoldProductsFound.visibility=View.VISIBLE
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sold_products_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {

            R.id.action_settings -> {
                startActivity(Intent(activity, SettingsActivity::class.java))

            }
        }
        return super.onOptionsItemSelected(item)
    }



}