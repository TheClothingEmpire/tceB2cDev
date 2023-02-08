package com.clothingempire.clothingempire.ui.adapters

import android.app.Dialog
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clothingempire.clothingempire.R
import com.clothingempire.clothingempire.databinding.ItemCartLayoutBinding
import com.clothingempire.clothingempire.databinding.PopupRecyclerViewBinding
import com.clothingempire.clothingempire.models.CartItem
import com.clothingempire.clothingempire.ui.activities.BaseActivity
import com.clothingempire.clothingempire.ui.activities.CartListActivity
import com.clothingempire.clothingempire.ui.activities.CheckOutActivity
import com.clothingempire.clothingempire.ui.activities.MyOrderDetailsActivity
import com.clothingempire.clothingempire.utils.Constants
import com.clothingempire.clothingempire.utils.FirebaseClass
import java.lang.Exception

class CartListAdapter(var activity: BaseActivity,var list:ArrayList<CartItem>):
    RecyclerView.Adapter<CartListAdapter.ViewHolder>() {
    class ViewHolder(binding:ItemCartLayoutBinding):RecyclerView.ViewHolder(binding.root) {
        val ivCartItemImage=binding.ivCartItemImage
        val ivDeleteButton=binding.ibDeleteCartItem
        //val ibAddCartItem=binding.ibAddCartItem
        //val ibRemoveCartItem=binding.ibRemoveCartItem
        //val tvCartItemPrice=binding.tvCartItemPrice
        val tvCartItemTitle=binding.tvCartItemTitle

        val rvVariantList=binding.rvCartVariantList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCartLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=list[position]
        holder.ivDeleteButton.setOnClickListener {
            FirebaseClass().deleteCartItemByID(activity,item.id)
        }
        Constants().loadImage(activity, item.image, holder.ivCartItemImage)
       // holder.tvCartItemPrice.text =
        holder.tvCartItemTitle.text = item.title
        holder.rvVariantList.adapter=CartVariantAdaptor(activity,item.cartVariant,item.id)
        holder.rvVariantList.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)

         if(activity is CheckOutActivity){
            holder.ivDeleteButton.visibility=View.GONE

        }

        when(activity){


            is MyOrderDetailsActivity->{

                holder.ivDeleteButton.visibility= View.GONE
            }
        }
        holder.ivDeleteButton.setOnClickListener {
            FirebaseClass().deleteCartItemByID(activity,item.id)
        }




    }

    override fun getItemCount(): Int {
        return list.size
    }
}