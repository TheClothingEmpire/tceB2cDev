package com.clothingempire.clothingempire.ui.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.clothingempire.clothingempire.databinding.ItemVariantSelecterBinding
import com.clothingempire.clothingempire.models.CartVariant
import com.clothingempire.clothingempire.ui.activities.BaseActivity
import com.clothingempire.clothingempire.ui.activities.CartListActivity
import com.clothingempire.clothingempire.ui.adapters.PopupDialogAdapter.*


class PopupDialogAdapter(var activity: BaseActivity,
                         var list:ArrayList<CartVariant>,
                         val cartId:String
                         ):RecyclerView.Adapter<ViewHolder>() {
    private var onClickListeners:onClickListener?=null
    inner class ViewHolder(binding:ItemVariantSelecterBinding):RecyclerView.ViewHolder(binding.root) {
        val ivBackground=binding.ivSelectorBackground
        val ivColor=binding.ivSelectorColor
        val tvSize=binding.tvSize
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemVariantSelecterBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val variant=list[position]
        holder.tvSize.text=variant.size
        if(variant.orderQuantity.toInt()>0){
            holder.ivBackground.visibility=View.VISIBLE
        }else{
            holder.ivBackground.visibility=View.INVISIBLE
        }
        try{
            holder.ivColor.setBackgroundColor(Color.parseColor("#"+variant.value))
        }catch (e:Exception){
            Log.e("background",e.message.toString())
        }



    }
    interface onClickListener{
        fun onclick(position: Int)
        fun onAddItem()
    }
    fun setOnclickListener(onClickListener: onClickListener){
        this.onClickListeners=onClickListener
    }

    override fun getItemCount(): Int {
        return list.size
    }

}