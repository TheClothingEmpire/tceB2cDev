package com.clothingempire.clothingempire.models

import android.os.Parcel
import android.os.Parcelable

data class CartVariant (
    val type:String="",
    val name:String="",
    val value:String="",
    val size:String="",
    var orderQuantity:String="",
    val quantity:String=""
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(name)
        parcel.writeString(value)
        parcel.writeString(size)
        parcel.writeString(orderQuantity)
        parcel.writeString(quantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartVariant> {
        override fun createFromParcel(parcel: Parcel): CartVariant {
            return CartVariant(parcel)
        }

        override fun newArray(size: Int): Array<CartVariant?> {
            return arrayOfNulls(size)
        }
    }
}
