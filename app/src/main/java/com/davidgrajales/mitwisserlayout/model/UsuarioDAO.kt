package com.davidgrajales.mitwisserlayout.model

import androidx.room.*

@Dao
interface UsuarioDAO {

    @Insert
    fun insertUsuario(name:Usuario)

    @Query("SELECT * FROM info_usuario WHERE correo LIKE :email")
    fun buscarUsuario(email:String): Usuario

    @Update
    fun actualizarDeuda(usuario:Usuario)

    @Delete
    fun borrarUsuario(usuario:Usuario)

}