package com.clothingempire.clothingempire.ui.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.clothingempire.clothingempire.databinding.ItemListLayoutBinding
import com.clothingempire.clothingempire.models.SoldProduct
import com.clothingempire.clothingempire.ui.activities.SoldProductsDetailsActivity
import com.clothingempire.clothingempire.ui.fragments.SoldProductsFragment
import com.clothingempire.clothingempire.utils.Constants
import java.util.ArrayList

class SoldProductsAdapter(val fragment: SoldProductsFragment, val list: ArrayList<SoldProduct>)
    :RecyclerView.Adapter<SoldProductsAdapter.ViewHolder>() {
    inner class ViewHolder(binding: ItemListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val tvTitle=binding.tvItemName
        val tvPrice=binding.tvItemPrice
        val ivImage=binding.ivItemImage
        val ibDelete=binding.ibDeleteProduct

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemListLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=list[position]
        holder.ibDelete.visibility= View.GONE
        Constants().loadImage(fragment,item.image,holder.ivImage)
        holder.tvTitle.text=item.id
        holder.tvPrice.text=item.total_amount
        Log.e("SoldProductList",item.title)
        holder.itemView.setOnClickListener {
            val intent= Intent(fragment.requireContext(),SoldProductsDetailsActivity::class.java)
            intent.putExtra(Constants.SOLD_PRODUCTS,item)
            fragment.startActivity(intent)
        }
        }

    override fun getItemCount(): Int {
        return list.size
    }
}

