package com.clothingempire.clothingempire.customwidgets

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.clothingempire.clothingempire.R

class CETextView(context: Context, attributeSet: AttributeSet)
    :AppCompatTextView(context,attributeSet) {
        init {
            this.applyFont()
        }
    private fun applyFont(){
        val typeFaceCustom:Typeface= Typeface.createFromAsset(context.assets,"Montserrat-Regular.ttf")
        typeface=typeFaceCustom
        //setTextColor(resources.getColor(R.color.pallete_one))

    }
}