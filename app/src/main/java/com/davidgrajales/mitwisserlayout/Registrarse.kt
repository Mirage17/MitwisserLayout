package com.davidgrajales.mitwisserlayout

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.davidgrajales.mitwisserlayout.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_registrarse.*

val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
class Registrarse : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse)





        b_save.setOnClickListener {
            val email = et_email.text.toString()
            val password = et_pass.text.toString()
            val nickname = et_nickname.text.toString()
            val password2 = et_pass2.text.toString()
            if (password == password2) {
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                        this
                    ) { task ->
                        if (task.isSuccessful) {

                            //createUserInDatabase()
                            GuardarEnDatabase(nickname, email, password, "prueba")
                            ShowMessage("User successfuly created")
                            onBackPressed()
                        } else {

                            ShowMessage(task.exception!!.message.toString())
                        }
                    }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Las contraseñas no coinciden",
                    Toast.LENGTH_SHORT
                ).show()
            }



        }

    }

    /* private fun createUserInDatabase() {

     }*/
    private fun ShowMessage(msg:String){
        Toast.makeText(this,msg, Toast.LENGTH_LONG).show()
    }

    private  fun GuardarEnDatabase(nick:String, correo:String,pass:String,url:String){
        val database:FirebaseDatabase= FirebaseDatabase.getInstance()
        val myRef:DatabaseReference=database.getReference("usuario")
        val id=FirebaseAuth.getInstance().uid //String?= myRef.push().key
        val usuario=Usuario(
            id,
            nick,
            correo,
            pass,
            url

        )
        myRef.child(id!!).setValue(usuario)

    }
}