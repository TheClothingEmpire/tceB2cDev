package com.clothingempire.clothingempire.ui.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.clothingempire.clothingempire.databinding.ItemVariantBinding
import com.clothingempire.clothingempire.models.Variant
import com.clothingempire.clothingempire.ui.activities.AddProductsActivity
import com.clothingempire.clothingempire.ui.activities.BaseActivity

class VariantAdaptor(val activity: BaseActivity, val list:ArrayList<Variant>):RecyclerView.Adapter<VariantAdaptor.ViewHolder>() {
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
        val item=list[position]
        Log.e("array",item.toString())
        holder.ivColor.setBackgroundColor(Color.parseColor("#"+item.value))
        holder.tvName.text=item.name
        holder.tvSize.text=item.size
        holder.tvQuantity.text=item.quantity
        if(activity is AddProductsActivity ){
            holder.ivDelete.setOnClickListener{
                activity.deleteItem(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}