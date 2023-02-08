package com.clothingempire.clothingempire.ui.activities

import android.app.Dialog
import android.graphics.Color
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.clothingempire.clothingempire.R
import com.clothingempire.clothingempire.databinding.PopupAddItemBinding
import com.clothingempire.clothingempire.databinding.ProgressDialogLayoutBinding
import com.clothingempire.clothingempire.models.CartVariant
import com.google.android.material.snackbar.Snackbar



open class BaseActivity : AppCompatActivity() {

    var CustomProgressbuilder: Dialog?=null
    private var doublePressToExit=false

    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.colorSnackBarError
                )
            )
        }else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.colorSnackBarSuccess
                )
            )
        }
        snackBar.show()
    }
    fun customProgressDialogOpener(text:String){
        CustomProgressbuilder= Dialog(this)

        CustomProgressbuilder?.show()
        val binding=ProgressDialogLayoutBinding.inflate(layoutInflater)
        CustomProgressbuilder?.setContentView(binding.root)
        CustomProgressbuilder?.setCanceledOnTouchOutside(false)

        binding.tvProgress.text = text



    }
    fun customProgressDialogCloser(){
        if(CustomProgressbuilder !=null){
            Log.e("customCloser","closed")
            CustomProgressbuilder?.dismiss()
        }
    }
     fun setupActionBar(toolBar:Toolbar,text: String) {
        var toolbar_register_activity=toolBar
        setSupportActionBar(toolbar_register_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_primary_color_back_24dp)
            //actionBar.title=text
        }

        toolbar_register_activity.setNavigationOnClickListener { onBackPressed() }
    }

    fun doubleBackToExit(){
        if(doublePressToExit){
            super.onBackPressed()
            return
        }
        this.doublePressToExit=true
        Toast.makeText(this, "press exit button once again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({this.doublePressToExit=false},2500)
    }

    // END
}
// END