package com.davidgrajales.mitwisserlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_registrarse.*


class Registrarse : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse)


        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

        b_save.setOnClickListener {
            val email = et_email.text.toString()
            val password = et_pass.text.toString()
            val name=et_nickname.text.toString()
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) {

                        //createUserInDatabase()
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
}