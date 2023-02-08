package com.clothingempire.clothingempire.customwidgets

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView

class CEEditText(context: Context, attributeSet: AttributeSet)
    :AppCompatEditText(context,attributeSet) {
        init {
            this.applyFont()
        }
    private fun applyFont(){
        val typeFaceCustom:Typeface= Typeface.createFromAsset(context.assets,"Montserrat-Regular.ttf")
        typeface=typeFaceCustom
    }
}