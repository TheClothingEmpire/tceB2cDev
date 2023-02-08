package com.clothingempire.clothingempire.models

import android.os.Parcel
import android.os.Parcelable

data class User(
    var id:String="",
    val firstName:String="",
    val lastName:String="",
    val email:String="",
    val image:String="",
    val mobile:Long=0,
    var fcmToken:String="",
    val userType:String="",
    var shopList:ArrayList<String> =ArrayList(),
    val lastUpdated:Long=0,
    val creationDate:Long=0,
    val userUpdated:Long=0,
    val gender:String=""
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(email)
        parcel.writeString(image)
        parcel.writeLong(mobile)
        parcel.writeString(fcmToken)
        parcel.writeString(userType)
        parcel.writeStringList(shopList)
        parcel.writeLong(lastUpdated)
        parcel.writeLong(creationDate)
        parcel.writeLong(userUpdated)
        parcel.writeString(gender)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
