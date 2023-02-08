package com.clothingempire.clothingempire.ui.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.clothingempire.clothingempire.R
import com.clothingempire.clothingempire.databinding.ItemImagePostionBinding

class ImagePositionAdapter(var context:Context,val image: ArrayList<String>,val selectedPosition: Int)
    :RecyclerView.Adapter<ImagePositionAdapter.ViewHolder>() {
    class ViewHolder(binding: ItemImagePostionBinding):RecyclerView.ViewHolder(binding.root) {
        val iv=binding.ivPosition

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemImagePostionBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e("scrollPostion", "$position  $selectedPosition")
        if(position==selectedPosition){

            holder.iv.setImageResource(R.drawable.image_postion_selected)
        }else{
            holder.iv.setImageResource(R.drawable.image_postion)
        }
    }

    override fun getItemCount(): Int {
        return image.size
    }
}