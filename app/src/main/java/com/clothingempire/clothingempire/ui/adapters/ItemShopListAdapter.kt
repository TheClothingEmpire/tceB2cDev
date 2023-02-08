package com.clothingempire.clothingempire.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.clothingempire.clothingempire.databinding.ItemShopLayoutBinding
import com.clothingempire.clothingempire.models.Shop
import com.clothingempire.clothingempire.ui.activities.CheckOutActivity
import com.clothingempire.clothingempire.ui.activities.ShopActivity
import com.clothingempire.clothingempire.utils.Constants
import com.clothingempire.clothingempire.utils.FirebaseClass

class ItemShopListAdapter(val activity: ShopActivity,val list: ArrayList<Shop>):
    RecyclerView.Adapter<ItemShopListAdapter.viewHolder>() {
    class viewHolder(binding:ItemShopLayoutBinding):RecyclerView.ViewHolder(binding.root) {
        val title=binding.tvShopFullName
        val gst=binding.tvGst
        val address=binding.tvAddressDetails
        val contact=binding.tvContactNumber
        val delete=binding.delete

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(ItemShopLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val item=list[position]
        holder.title.text=item.name
        holder.gst.text=item.gst
        holder.address.text=item.location
        holder.contact.text=item.contactNumber
        holder.itemView.setOnClickListener {
            val mSelectedShopID=item.documentId
            val intent=Intent(activity,CheckOutActivity::class.java)
            intent.putExtra(Constants.SELECT_SHOP,mSelectedShopID)
            activity.startActivity(intent)
            activity.finish()
        }
        holder.delete.setOnClickListener {
            FirebaseClass().deleteShopByGST(activity,item.gst)
        }
        if(activity.mSelectShop){
            holder.delete.visibility= View.GONE
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}