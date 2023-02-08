package com.clothingempire.clothingempire.models

import android.os.Parcel
import android.os.Parcelable

data class Product(
    var id:String="",
    val title:String="",
    val description:String="",
    val createdBy:String="",
    val createdOn:Long=0L,
    val updatedOn:Long=0L,
    val price:Float=0.0F,
    val image:ArrayList<String> =ArrayList(),
    var minimumQuantity:Int=1,
    var maximumTime:Int=30,
    var quantityPerPacket:Int=1,
    var material:String="",
    var variants:ArrayList<Variant> =ArrayList(),


):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readLong(),
        parcel.readFloat(),
        parcel.createStringArrayList()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.createTypedArrayList(Variant)!!,

    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(createdBy)
        parcel.writeLong(createdOn)
        parcel.writeLong(updatedOn)
        parcel.writeFloat(price)
        parcel.writeStringList(image)
        parcel.writeInt(minimumQuantity)
        parcel.writeInt(maximumTime)
        parcel.writeInt(quantityPerPacket)
        parcel.writeString(material)
        parcel.writeTypedList(variants)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}
