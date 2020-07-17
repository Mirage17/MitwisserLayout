package com.davidgrajales.mitwisserlayout.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Usuario (
    val id:String?,
    val nickName: String,
    val correo: String,
    val password:String,
    val urlPicture:String
):Parcelable{
    constructor():this("","","","","")
}
