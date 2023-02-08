package com.clothingempire.clothingempire.customwidgets

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class BoldTextView(context: Context,attributeSet: AttributeSet)
    :AppCompatTextView(context,attributeSet) {
        init {
            this.applyFont()
        }
    private fun applyFont(){
        val typeFaceCustom:Typeface= Typeface.createFromAsset(context.assets,"montserrat_bold.ttf")
        typeface=typeFaceCustom
    }
}