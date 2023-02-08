package com.clothingempire.clothingempire.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.clothingempire.clothingempire.ui.adapters.ProductsAdaptor
import com.clothingempire.clothingempire.R
import com.clothingempire.clothingempire.databinding.FragmentProductsBinding
import com.clothingempire.clothingempire.models.Product
import com.clothingempire.clothingempire.ui.activities.AddProductsActivity
import com.clothingempire.clothingempire.ui.activities.SettingsActivity
import com.clothingempire.clothingempire.utils.FirebaseClass


class ProductsFragment : BaseFragment(){


    private var _binding: FragmentProductsBinding? = null
    private  var mProductList:ArrayList<Product>?=null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        //getProductListFromDB()
        _binding = FragmentProductsBinding.inflate(layoutInflater)


    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        getProductListFromDB()


        return root
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_product_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        when(id){
            R.id.action_add_products->{
                val intent=Intent(activity, AddProductsActivity::class.java)
                startActivity(intent)
            }

        }
        return super.onOptionsItemSelected(item)
    }
    fun getProductListFromDB(){
        customProgressDialogOpener("loading Products")
        FirebaseClass().getProductByCreator(this)
    }
    fun getProductsSuccess(productList: ArrayList<Product>){
        mProductList=productList
        if(mProductList.isNullOrEmpty()){

            binding.rvMyProductItems.visibility=View.GONE
            binding.tvNoProductsFound.visibility=View.VISIBLE

        }else{
            binding.rvMyProductItems.visibility=View.VISIBLE
            binding.tvNoProductsFound.visibility=View.GONE
            binding.rvMyProductItems.layoutManager=LinearLayoutManager(this.requireContext())
            val adapter= ProductsAdaptor(mProductList!!,this)

            binding.rvMyProductItems.adapter=adapter
        }
        customProgressDialogCloser()
    }

    fun deleteProductByID(id: String) {
        val builder=AlertDialog.Builder(this.context)
        builder.setNegativeButton("cancel"){
           dialogInterface,which->
            dialogInterface.dismiss()
        }
        builder.setPositiveButton("Yes"){
            di,_->
                di.cancel()
                FirebaseClass().deleteProductByID(id,this)
        }
        builder.setTitle("Delete Product")
        builder.setMessage("Please confirm to delete the selected product")
        builder.setIcon(R.drawable.ic_baseline_error_24)
        val alertDialog=builder.create()
        alertDialog.show()
    }

    fun deleteProductSuccess() {
        getProductListFromDB()

    }

}