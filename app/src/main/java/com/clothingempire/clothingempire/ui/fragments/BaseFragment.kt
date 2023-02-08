package com.clothingempire.clothingempire.ui.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.clothingempire.clothingempire.R
import com.clothingempire.clothingempire.databinding.ProgressDialogLayoutBinding

open class BaseFragment : Fragment() {
    private  var customProgressbuilder: Dialog?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base_frag_ment, container, false)
    }
    fun customProgressDialogOpener(text:String){
        customProgressbuilder= Dialog(requireContext())

        customProgressbuilder?.show()
        val binding=ProgressDialogLayoutBinding.inflate(layoutInflater)
        customProgressbuilder?.setContentView(binding.root)
        customProgressbuilder?.setCanceledOnTouchOutside(false)

        binding.tvProgress.text = text



    }
    fun customProgressDialogCloser() {
        customProgressbuilder?.dismiss()
    }



}