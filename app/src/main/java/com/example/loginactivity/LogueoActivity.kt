package com.example.loginactivity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_logueo.*
import kotlinx.android.synthetic.main.activity_registro.*

class LogueoActivity : AppCompatActivity() {
    var correoRe=""
    var contraseniaRe=""
    var flag_registro=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logueo)

        bnRegistro.setOnClickListener {

            var intent = Intent(this,RegistroActivity::class.java)
            Toast.makeText(this,"Iniciando Registro...",Toast.LENGTH_SHORT).show()
            startActivityForResult(intent,1234)

        }

        bnIniciarSesion.setOnClickListener {
            if(flag_registro){
                if(correoRe==etCorreo.text.toString() && contraseniaRe==etContraseña.text.toString()) {
                    Toast.makeText(this,"Iniciando sesión...",Toast.LENGTH_SHORT).show()
                    var intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("correo", correoRe)
                    intent.putExtra("contrasenia", contraseniaRe)
                    //startActivity(intent)
                    startActivityForResult(intent, 5678)
                }else{
                    if(correoRe!=etCorreo.text.toString()){
                        Toast.makeText(this,"Correo incorrecto",Toast.LENGTH_SHORT).show()
                    }
                    if(contraseniaRe!=etCorreo.text.toString()){
                        Toast.makeText(this,"Contrasenia incorrecta",Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this,"Correo o Contraseña incorrecto",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1234){
            if(resultCode== Activity.RESULT_OK){
                correoRe = data!!.getStringExtra("correo")
                contraseniaRe = data!!.getStringExtra("contrasenia")
                //tvResultado.text = "Los datos recibidos del registro son"+"\n"+correoRe +"\n"+contraseniaRe
                flag_registro=true

            }
        }
        if(requestCode==5678){
            if(resultCode==Activity.RESULT_CANCELED){
                finish()
            }
        }


    }

}
