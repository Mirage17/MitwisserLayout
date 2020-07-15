package com.davidgrajales.mitwisserlayout


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class Login : AppCompatActivity() {
    val mAuth:FirebaseAuth=FirebaseAuth.getInstance()
   /* override fun onStart() {
        super.onStart()
        val user =mAuth.currentUser

        if(user!=null)
            StartMain()
    }*/
    override fun onCreate(savedInstanceState: Bundle?) {

        Thread.sleep(1000)

        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        b_logEnter.setOnClickListener {

            val email =et_correo.text.toString()
            val password = et_pass.text.toString()
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) {
                        StartMain()

                    } else {

                        ShowMessage("Autentificación fallida")


                    }

                    // ...
                }

            }


        b_log2Register.setOnClickListener {


            StartRegister()

        }

    }

    private fun StartRegister() {

        startActivity(Intent(this, Registrarse::class.java))
    }

    private fun StartMain() {
        startActivity(Intent(this, MainActivity::class.java))

    }

    private fun ShowMessage(msg:String){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
    }

}


