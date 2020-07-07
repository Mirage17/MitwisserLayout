package com.davidgrajales.mitwisserlayout

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.Toast
import com.davidgrajales.mitwisserlayout.model.Usuario
import com.davidgrajales.mitwisserlayout.model.UsuarioDAO
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_registro.*
import java.sql.Types.NULL
import java.text.SimpleDateFormat

import java.util.*

class Registro : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        b_save.setOnClickListener {
            val email = et_email.text.toString()
            val pass = et_pass.text.toString()
            val nname=et_nickname.text.toString()


            if (email.isEmpty() || pass.isEmpty() || nname.isEmpty()){
                Toast.makeText(this,"Faltan campos por llenar",Toast.LENGTH_LONG).show()
                    return@setOnClickListener
            }
            else{
                val usuario=Usuario(NULL,nname,email,pass)
                val usuarioDaAO:UsuarioDAO=SesionRoom.database.UsuarioDAO()
                usuarioDaAO.insertUsuario(usuario)

                et_email.setText("")
                et_nickname.setText("")
                et_pass.setText("")
            /*FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pass).addOnCompleteListener{
                   if(it.isSuccessful) return@addOnCompleteListener*/

                Toast.makeText(this, "Usuario registrado con exito",Toast.LENGTH_LONG).show()
                val intent: Intent = Intent(this, Login::class.java)
                startActivity(intent)


                Log.d("main","crated profile")
            }




        }
    }

}