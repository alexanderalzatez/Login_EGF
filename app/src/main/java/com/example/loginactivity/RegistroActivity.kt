package com.example.loginactivity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_logueo.*
import kotlinx.android.synthetic.main.activity_registro.*

class RegistroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

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

                        var intent = Intent(this,LogueoActivity::class.java)
                        intent.putExtra("correo",correo)
                        intent.putExtra("contrasenia",contrasenia)
                        setResult(Activity.RESULT_OK,intent)
                        Toast.makeText(this,"Te has registado con éxito!",Toast.LENGTH_SHORT).show()
                        finish()
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

}
