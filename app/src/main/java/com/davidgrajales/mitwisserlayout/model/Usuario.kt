package com.davidgrajales.mitwisserlayout.model

class Usuario (
    val id:String?,
    val nickName: String,
    val correo: String,
    val password:String
){
    constructor():this("","","","")
}
