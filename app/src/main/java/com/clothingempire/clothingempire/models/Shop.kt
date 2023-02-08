package com.clothingempire.clothingempire.models

import android.os.Parcel
import android.os.Parcelable

data class Shop(
    val name:String="",
    val gst:String="",
    val contactNumber:String="",
    val password:String="",
    val location:String="",
    val lastActive:Long=0,
    val creationDate:Long=0,
    var documentId:String="",
    val createdBy:String="",
    var fcmToken:String=""
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(gst)
        parcel.writeString(contactNumber)
        parcel.writeString(password)
        parcel.writeString(location)
        parcel.writeLong(lastActive)
        parcel.writeLong(creationDate)
        parcel.writeString(documentId)
        parcel.writeString(createdBy)
        parcel.writeString(fcmToken)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Shop> {
        override fun createFromParcel(parcel: Parcel): Shop {
            return Shop(parcel)
        }

        override fun newArray(size: Int): Array<Shop?> {
            return arrayOfNulls(size)
        }
    }
}
