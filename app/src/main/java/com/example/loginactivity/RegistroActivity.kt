package com.example.loginactivity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_logueo.*
import kotlinx.android.synthetic.main.activity_registro.*

class RegistroActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        auth = FirebaseAuth.getInstance()

        bnRegistrar.setOnClickListener {
            var correo=""
            var contrasenia=""
            var repiteContrasenia=""

            correo = etCorreoRe.text.toString()
            contrasenia = etContraseñaRe.text.toString()
            repiteContrasenia = etRepiteContraseñaRe.text.toString()



            if(correo!="" && contrasenia!="" && repiteContrasenia!=""){
                if(contrasenia==repiteContrasenia){
                    if(contrasenia.length >=6 && repiteContrasenia.length>=6){

                        auth.createUserWithEmailAndPassword(correo, contrasenia)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    startActivity(Intent(this,LogueoActivity::class.java))
                                    Toast.makeText(this,"Te has registado con éxito!",Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(this,"El registro ha fallado. Intentalo de nuevo.",Toast.LENGTH_SHORT).show()

                                }

                                // ...
                            }

                        /*var intent = Intent(this,LogueoActivity::class.java)
                        intent.putExtra("correo",correo)
                        intent.putExtra("contrasenia",contrasenia)
                        setResult(Activity.RESULT_OK,intent)
                        Toast.makeText(this,"Te has registado con éxito!",Toast.LENGTH_SHORT).show()
                        finish()*/
                    }else{
                        Toast.makeText(this,"La contraseña debe tener míninmo 6 caracteres",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this,"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"Uno o más campos están vacíos",Toast.LENGTH_SHORT).show()
            }
        }
    }
    public override fun onStart() {//Para comprobar si el usuario ya está logueado
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {

    }


}
