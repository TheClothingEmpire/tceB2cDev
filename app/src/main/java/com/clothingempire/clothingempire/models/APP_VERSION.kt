package com.clothingempire.clothingempire.models

import android.os.Parcel
import android.os.Parcelable

data class APP_VERSION(
    val updatedDate:Long=0,
    val version:Int=0,
    val updatedBy:String=""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readInt(),
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(updatedDate)
        parcel.writeInt(version)
        parcel.writeString(updatedBy)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<APP_VERSION> {
        override fun createFromParcel(parcel: Parcel): APP_VERSION {
            return APP_VERSION(parcel)
        }

        override fun newArray(size: Int): Array<APP_VERSION?> {
            return arrayOfNulls(size)
        }
    }

}
