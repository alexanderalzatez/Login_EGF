package com.example.loginactivity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var correo=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var datosRecibidosLo = intent.extras
        correo = datosRecibidosLo!!.getString("correo").toString()
        tvResultadoMain.text = "Bienvenido! " + "\n" + correo + "\n"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.overflow_menu,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.mCerrarSesion -> {

                var intent2 = Intent(this, LogueoActivity::class.java)
                Toast.makeText(this,"Has cerrado sesión...",Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK, intent2)
                //startActivity(intent2)
                finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        var intent2 = Intent(this, LogueoActivity::class.java)
        Toast.makeText(this,"Has cerrado la aplicación...",Toast.LENGTH_SHORT).show()
        setResult(Activity.RESULT_CANCELED, intent2)
        finish()
    }
}
