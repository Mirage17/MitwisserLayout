package com.davidgrajales.mitwisserlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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
            val nickname=et_nickname.text.toString()
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) {

                        //createUserInDatabase()
                        GuardarEnDatabase(nickname,email,password,"prueba")
                        ShowMessage("User successfuly created")
                        onBackPressed()
                    } else {

                        ShowMessage(task.exception!!.message.toString())


                    }


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