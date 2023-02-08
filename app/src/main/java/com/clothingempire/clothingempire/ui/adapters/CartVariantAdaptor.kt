package com.clothingempire.clothingempire.ui.adapters

import android.graphics.Color
import android.provider.SyncStateContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.clothingempire.clothingempire.databinding.ItemVariantBinding
import com.clothingempire.clothingempire.models.CartVariant
import com.clothingempire.clothingempire.ui.activities.BaseActivity
import com.clothingempire.clothingempire.ui.activities.CartListActivity
import com.clothingempire.clothingempire.utils.Constants
import com.clothingempire.clothingempire.utils.FirebaseClass

class CartVariantAdaptor(val activity: BaseActivity, val list: ArrayList<CartVariant>, val id: String):RecyclerView.Adapter<CartVariantAdaptor.ViewHolder>() {
    inner class ViewHolder(binding:ItemVariantBinding):RecyclerView.ViewHolder(binding.root){
        val tvName=binding.tvName
        val ivColor=binding.ivColor
        val tvSize=binding.tvSize
        val tvQuantity=binding.tvQuantity
        val ivDelete=binding.ivDeleteVariant
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemVariantBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        Log.e("array", item.toString())
        holder.itemView.setOnClickListener {
            (activity as CartListActivity).customDialog(item, list, position, id)
        }
            holder.ivColor.setBackgroundColor(Color.parseColor("#" + item.value))
            holder.tvName.text = item.name
            holder.tvSize.text = item.size
            holder.tvQuantity.text = item.orderQuantity
            holder.ivDelete.visibility = View.GONE

    }

    override fun getItemCount(): Int {
        return list.size
    }
}