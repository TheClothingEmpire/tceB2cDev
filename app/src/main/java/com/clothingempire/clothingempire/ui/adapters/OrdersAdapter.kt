package com.clothingempire.clothingempire.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.clothingempire.clothingempire.databinding.ActivityCheckOutBinding
import com.clothingempire.clothingempire.databinding.ItemListLayoutBinding
import com.clothingempire.clothingempire.ui.activities.MyOrderDetailsActivity
import com.clothingempire.clothingempire.ui.fragments.OrdersFragment
import com.clothingempire.clothingempire.utils.Constants
import com.myshoppal.models.Order
import java.util.ArrayList

class OrdersAdapter(val fragment: OrdersFragment, val orderList: ArrayList<Order>)
    :RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {
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
        val item=orderList[position]
        holder.ibDelete.visibility= View.GONE
        Constants().loadImage(fragment,item.image,holder.ivImage)
        holder.tvTitle.text=item.title
        holder.tvPrice.text=item.total_amount
        holder.itemView.setOnClickListener {
            val intent= Intent(fragment.requireContext(),MyOrderDetailsActivity::class.java)
            intent.putExtra(Constants.ORDERS,item)
            fragment.startActivity(intent)
        }
        holder.ibDelete.visibility=View.GONE
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

}
