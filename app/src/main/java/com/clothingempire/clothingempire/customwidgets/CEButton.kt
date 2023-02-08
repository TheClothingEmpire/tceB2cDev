package com.clothingempire.clothingempire.customwidgets

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.setPadding
import com.clothingempire.clothingempire.R

class CEButton(context: Context, attributeSet: AttributeSet)
    :AppCompatButton(context,attributeSet) {
        init {
            this.applyFont()
        }
    private fun applyFont(){
        val typeFaceCustom:Typeface= Typeface.createFromAsset(context.assets,"Montserrat-Regular.ttf")
        typeface=typeFaceCustom
        setBackgroundResource(R.drawable.shape_button_rounded)
        setPadding(10,0,10,0)
    }
}