package com.clothingempire.clothingempire.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

import com.clothingempire.clothingempire.databinding.FragmentOrdersBinding
import com.clothingempire.clothingempire.ui.adapters.OrdersAdapter
import com.clothingempire.clothingempire.utils.FirebaseClass
import com.myshoppal.models.Order
import java.util.ArrayList


class OrdersFragment : BaseFragment() {

    private var _binding: FragmentOrdersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onResume() {
        super.onResume()
        getOrdersList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun getOrdersList(){
        customProgressDialogOpener("Loading Orders")
        FirebaseClass().getOrderList(this)
    }

    fun getOrderListSuccess(orderList: ArrayList<Order>) {
        customProgressDialogCloser()
        if(orderList.size>0){
            val adapter= OrdersAdapter(this,orderList)
            binding.rvMyOrderItems.layoutManager=LinearLayoutManager(this.requireContext(),
                LinearLayoutManager.VERTICAL,false)
            binding.rvMyOrderItems.adapter=adapter
            binding.tvNoOrdersFound.visibility=View.GONE
            binding.rvMyOrderItems.visibility=View.VISIBLE
        }else{
            binding.tvNoOrdersFound.visibility=View.VISIBLE
            binding.rvMyOrderItems.visibility=View.GONE
        }
    }
}