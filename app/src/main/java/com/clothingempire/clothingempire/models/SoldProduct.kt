package com.clothingempire.clothingempire.models

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

data class SoldProduct(
    val user_id: String = "",
    val title: String = "",
    val price: String = "",
    val variant: ArrayList<Variant> = ArrayList(),
    val image: String = "",
    val order_id: String = "",
    val order_date: Long = 0L,
    val sub_total_amount: String = "",
    val shipping_charge: String = "",
    val total_amount: String = "",
    val shopID:String = "",
    var id: String = "",
    var fcmToken:String=""
):Parcelable {
    @RequiresApi(Build.VERSION_CODES.M)
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(Variant)!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(user_id)
        parcel.writeString(title)
        parcel.writeString(price)
        parcel.writeTypedList(variant)
        parcel.writeString(image)
        parcel.writeString(order_id)
        parcel.writeLong(order_date)
        parcel.writeString(sub_total_amount)
        parcel.writeString(shipping_charge)
        parcel.writeString(total_amount)
        parcel.writeString(shopID)
        parcel.writeString(id)
        parcel.writeString(fcmToken)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SoldProduct> {

        override fun createFromParcel(parcel: Parcel): SoldProduct {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                SoldProduct(parcel)
            } else {
                return SoldProduct()
            }
        }

        override fun newArray(size: Int): Array<SoldProduct?> {
            return arrayOfNulls(size)
        }
    }
}