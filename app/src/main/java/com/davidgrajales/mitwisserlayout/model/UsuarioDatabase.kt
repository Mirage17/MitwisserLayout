package com.davidgrajales.mitwisserlayout.model

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = arrayOf(Usuario::class),version = 1)
abstract class UsuarioDatabase:RoomDatabase() {
    abstract fun UsuarioDAO():UsuarioDAO
}