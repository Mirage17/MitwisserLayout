package com.davidgrajales.mitwisserlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.davidgrajales.mitwisserlayout.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_registrarse.*


class Registrarse : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse)


        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()


        b_save.setOnClickListener {
            val email = et_email.text.toString()
            val password = et_pass.text.toString()
            val nickname=et_nickname.text.toString()
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) {

                        //createUserInDatabase()
                        GuardarEnDatabase(nickname,email,password)
                        ShowMessage("Creación de usuario exitosa")
                        onBackPressed()
                    } else {

                        ShowMessage("Creasión de usuario fallida")


                    }


                }

        }

    }

    /* private fun createUserInDatabase() {

     }*/
    private fun ShowMessage(msg:String){
        Toast.makeText(this,msg, Toast.LENGTH_LONG).show()
    }

    private  fun GuardarEnDatabase(nick:String, correo:String,pass:String){
        val database:FirebaseDatabase= FirebaseDatabase.getInstance()
        val myRef:DatabaseReference=database.getReference("usuario")
        val id: String?= myRef.push().key
        val usuario=Usuario(
            id,
            nick,
            correo,
            pass
        )
        myRef.child(id!!).setValue(usuario)

    }
}