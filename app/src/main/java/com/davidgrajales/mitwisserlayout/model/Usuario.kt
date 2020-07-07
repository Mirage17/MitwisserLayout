package com.davidgrajales.mitwisserlayout.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "info_usuario")
class Usuario (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id:Int,
    @ColumnInfo(name="nickname") val name: String,
    @ColumnInfo(name="correo") val correo: String,
    @ColumnInfo(name="password") val password:String
)