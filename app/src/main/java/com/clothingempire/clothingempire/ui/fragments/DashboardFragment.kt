package com.clothingempire.clothingempire.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.clothingempire.clothingempire.R
import com.clothingempire.clothingempire.databinding.FragmentDashboardBinding
import com.clothingempire.clothingempire.models.Product
import com.clothingempire.clothingempire.ui.activities.CartListActivity
import com.clothingempire.clothingempire.ui.activities.SettingsActivity
import com.clothingempire.clothingempire.ui.adapters.DashboardAdapter
import com.clothingempire.clothingempire.ui.adapters.ProductsAdaptor
import com.clothingempire.clothingempire.utils.FirebaseClass


class DashboardFragment : BaseFragment(){


    private var _binding: FragmentDashboardBinding? = null
    private  var mProductList:ArrayList<Product>?=null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        _binding = FragmentDashboardBinding.inflate(layoutInflater)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onResume() {
        super.onResume()
        getProductListFromDB()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        when(id){
            R.id.action_settings->{
                startActivity(Intent(activity,SettingsActivity::class.java))

            }
            R.id.action_cart ->{
                startActivity(Intent(activity,CartListActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun getProductListFromDB(){
        customProgressDialogOpener("loading Products")
        try{
            FirebaseClass().getAllProducts(this)
        }catch(e:Exception){
            Log.e("error",e.message.toString())
        }
    }
    fun getProductsSuccess(productList: ArrayList<Product>){
        mProductList=productList
        if(mProductList.isNullOrEmpty()){

            binding.rvDashboardItems .visibility=View.GONE
            binding.tvNoDashboardItemsFound.visibility=View.VISIBLE

        }else{
            binding.rvDashboardItems.visibility=View.VISIBLE
            binding.tvNoDashboardItemsFound.visibility=View.GONE
           binding.rvDashboardItems.layoutManager= GridLayoutManager(this.requireContext(),2)
            val adapter= DashboardAdapter(mProductList!!,this)

            binding.rvDashboardItems.adapter=adapter
        }
        customProgressDialogCloser()
    }
}