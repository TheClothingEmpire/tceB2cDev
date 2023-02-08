package com.clothingempire.clothingempire.ui.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.clothingempire.clothingempire.databinding.ItemListLayoutBinding
import com.clothingempire.clothingempire.models.Product
import com.clothingempire.clothingempire.ui.activities.AddProductsActivity
import com.clothingempire.clothingempire.ui.activities.ProductDetailsActivity
import com.clothingempire.clothingempire.ui.fragments.BaseFragment
import com.clothingempire.clothingempire.ui.fragments.ProductsFragment
import com.clothingempire.clothingempire.utils.Constants

class ProductsAdaptor(val productList:ArrayList<Product>,private var fragment: BaseFragment):RecyclerView.Adapter<ProductsAdaptor.ViewHolder>() {
    inner class ViewHolder(binding:ItemListLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        val tvItemName=binding.tvItemName
        val tvItemPrice=binding.tvItemPrice
        val ivItemImage=binding.ivItemImage
        val ibDeleteProduct=binding.ibDeleteProduct
        val llProduct=binding.llProductDetails
        val ibUpdateProduct=binding.ibUpdateProduct


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemListLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product=productList[position]
        Constants().loadImage(fragment,product.image[0],holder.ivItemImage)
        holder.tvItemName.text=product.title
        holder.tvItemPrice.text=product.price.toString()
        holder.ibDeleteProduct.setOnClickListener {
            (fragment as ProductsFragment).deleteProductByID(product.id)
        }
        holder.itemView.setOnClickListener {
            val intent= Intent(fragment.context, ProductDetailsActivity::class.java)
            intent.putExtra(Constants.PRODUCTS,product.id)
            fragment.startActivity(intent )
        }
        holder.ibUpdateProduct.setOnClickListener{
            try {
                val intent= Intent(fragment.context, AddProductsActivity::class.java)
                intent.putExtra(Constants.PRODUCTS,product)
                fragment.startActivity(intent)
            }catch (e:Exception){
                Log.e("UpdateProduct",e.message.toString())
            }
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}