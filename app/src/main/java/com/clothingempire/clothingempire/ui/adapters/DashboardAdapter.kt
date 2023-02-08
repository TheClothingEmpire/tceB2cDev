package com.clothingempire.clothingempire.ui.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup



import androidx.fragment.app.Fragment
import androidx.navigation.navOptions
import androidx.recyclerview.widget.RecyclerView
import com.clothingempire.clothingempire.databinding.ItemDashboardLayoutBinding
import com.clothingempire.clothingempire.models.Product
import com.clothingempire.clothingempire.ui.activities.ProductDetailsActivity
import com.clothingempire.clothingempire.ui.fragments.BaseFragment
import com.clothingempire.clothingempire.utils.Constants

class DashboardAdapter(val productList:ArrayList<Product>, val fragment:BaseFragment): RecyclerView.Adapter<DashboardAdapter.ViewHolder>(){
    inner class ViewHolder(binding:ItemDashboardLayoutBinding):RecyclerView.ViewHolder(binding.root) {
        val tvTitle=binding.tvDashboardItemTitle
        val tvPrice=binding.tvDashboardItemPrice
        val ivImage=binding.ivDashboardItemImage
        val llDash=binding.llDashboardItemDetails
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemDashboardLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product=productList[position]
        Constants().loadImage(fragment,product.image[0],holder.ivImage)
        holder.tvPrice.text=product.price.toString()
        holder.tvTitle.text=product.title
        holder.itemView.setOnClickListener {
            val intent= Intent(fragment.context,ProductDetailsActivity::class.java)
            intent.putExtra(Constants.PRODUCTS,product.id)
            intent.putExtra(Constants.UID,product.createdBy)
            fragment.startActivity(intent )
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}