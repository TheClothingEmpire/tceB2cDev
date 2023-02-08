package com.clothingempire.clothingempire.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager

import com.clothingempire.clothingempire.databinding.PopupRecyclerViewBinding
import com.clothingempire.clothingempire.ui.activities.BaseActivity
import com.clothingempire.clothingempire.ui.adapters.PopupDialogAdapter
import java.text.FieldPosition

abstract class PopupDialog(val activity:BaseActivity,
                           private var list:ArrayList<String> = ArrayList(),
                           private val title:String="",
                           private var mSelectedOption:String=""
 ):Dialog(activity){
    private var adapter:PopupDialogAdapter?=null
    private var binding:PopupRecyclerViewBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= PopupRecyclerViewBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {

        binding?.rvPopup?.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
       // adapter= PopupDialogAdapter(activity,list,mSelectedOption)
        //binding?.rvPopup?.adapter=adapter
        adapter!!.setOnclickListener(object :PopupDialogAdapter.onClickListener{
            override fun onclick(position: Int) {
                onItemSelected(list[position])
            }

            override fun onAddItem() {
                onAddItemSelected()
            }
        })
    }
   protected abstract fun onItemSelected(item:String)
   protected abstract fun onAddItemSelected()
}