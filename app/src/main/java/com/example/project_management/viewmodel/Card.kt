package com.example.project_management.viewmodel

import android.os.Parcel
import android.os.Parcelable

data class Card (
    val name: String = "",
    val createBy: String = "",
    val assignedTo: ArrayList<String> = ArrayList(),
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!,
        source.createStringArrayList()!!
    )

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest){
        writeString(name)
        writeString(createBy)
        writeStringList(assignedTo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Card> {
        override fun createFromParcel(parcel: Parcel): Card {
            return Card(parcel)
        }

        override fun newArray(size: Int): Array<Card?> {
            return arrayOfNulls(size)
        }
    }
}