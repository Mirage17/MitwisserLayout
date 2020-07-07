package com.davidgrajales.mitwisserlayout

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.davidgrajales.mitwisserlayout.model.UsuarioDAO
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registro.*

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        Thread.sleep(1000)

        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        b_logEnter.setOnClickListener {

           /* val email=et_email.text.toString()
            val usuarioDAO:UsuarioDAO=SesionRoom.database.UsuarioDAO()
            val usuario=usuarioDAO.buscarUsuario(email)*/

            //if(usuario!=null){
                val intent=Intent(this,MainActivity::class.java)
                startActivity(intent)
            //}
            //else{
                //Toast.makeText(this,"campos por llenar o incorrectos",Toast.LENGTH_LONG).show()
           // }
           /* val password=et_password.text.toString()
            val email=et_email.text.toString()
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            //.addOnCompleteListener()
            //.add*/


        }

        b_log2Register.setOnClickListener {



            val intent: Intent = Intent(this, Registro::class.java)
            startActivity(intent)
            finish()

        }

    }


}


