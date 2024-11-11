package com.example.systemtrakingtransport;

import android.os.Parcel
import android.os.Parcelable

data class Vehicle(
    var id: Long? = null,
    var brand: String,
    var model: String,
    var year: String,
    var type: String
)
    // : Parcelable {
//    constructor(parcel: Parcel) : this(
//        parcel.readValue(Long::class.java.classLoader) as? Long,
//        parcel.readString() ?: "",
//        parcel.readString() ?: "",
//        parcel.readString() ?: "",
//        parcel.readString() ?: ""
//    )
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeValue(id)
//        parcel.writeString(brand)
//        parcel.writeString(model)
//        parcel.writeString(year)
//        parcel.writeString(type)
//    }
//
//    override fun describeContents(): Int = 0
//
//    companion object CREATOR : Parcelable.Creator<Vehicle> {
//        override fun createFromParcel(parcel: Parcel): Vehicle = Vehicle(parcel)
//        override fun newArray(size: Int): Array<Vehicle?> = arrayOfNulls(size)
//    }
//}
