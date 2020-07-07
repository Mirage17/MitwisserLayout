package com.davidgrajales.mitwisserlayout

import android.app.Application
import androidx.room.Room
import com.davidgrajales.mitwisserlayout.model.UsuarioDatabase

class SesionRoom:Application() {

    companion object{
        lateinit var database: UsuarioDatabase
    }

    override fun onCreate() {
        super.onCreate()
        SesionRoom.database=Room.databaseBuilder(
            this,
            UsuarioDatabase::class.java,
            "deudor_DB"
        ).allowMainThreadQueries().build()
    }

}