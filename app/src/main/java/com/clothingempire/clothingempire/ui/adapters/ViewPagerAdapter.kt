package com.clothingempire.clothingempire.ui.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.clothingempire.clothingempire.R
import com.clothingempire.clothingempire.ui.activities.BaseActivity
import com.clothingempire.clothingempire.utils.Constants
import java.util.*
import kotlin.collections.ArrayList


class ViewPagerAdapter(// Context object
    var context: BaseActivity, // Array of images
    var images: ArrayList<String>
) :
    PagerAdapter() {


    // Layout Inflater
    var mLayoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        // return the number of images
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // inflating the item.xml
        val itemView = mLayoutInflater.inflate(R.layout.item, container, false)

        // referencing the image view from the item.xml file
        val imageView = itemView.findViewById<View>(R.id.imageViewMain) as ImageView
        val recyclerView: RecyclerView =itemView.findViewById(R.id.rv_image_position)
        recyclerView.adapter=ImagePositionAdapter(context,images,position)
        recyclerView.layoutManager=
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)

        // setting the image in the imageView
        //Constants().loadImage(context,images[position],imageView)
        Glide.with(context)
            .load(images[position])
            .fitCenter()
            .placeholder(R.drawable.ic_baseline_downloading_24)
            .into(imageView)



        // Adding the View
        Objects.requireNonNull(container).addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}
class ViewPagerAdapterUri(// Context object
    var context: BaseActivity, // Array of images
    var images: ArrayList<Uri>
) :
    PagerAdapter() {


    // Layout Inflater
    var mLayoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        // return the number of images
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // inflating the item.xml
        val itemView = mLayoutInflater.inflate(R.layout.item, container, false)

        // referencing the image view from the item.xml file
        val imageView = itemView.findViewById<View>(R.id.imageViewMain) as ImageView
        val recyclerView: RecyclerView =itemView.findViewById(R.id.rv_image_position)
        val array=kotlin.collections.ArrayList<String>()
        for(i in images){
            array.add(" ")
        }
        recyclerView.adapter=ImagePositionAdapter(context,array,position)
        recyclerView.layoutManager=
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)

        // setting the image in the imageView
        //Constants().loadImage(context,images[position],imageView)
        Glide.with(context)
            .load(images[position])
            .fitCenter()
            .placeholder(R.drawable.ic_user_placeholder)
            .into(imageView)



        // Adding the View
        Objects.requireNonNull(container).addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}
