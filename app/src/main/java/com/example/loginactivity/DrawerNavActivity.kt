package com.example.loginactivity

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu

class DrawerNavActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer_nav)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        val manager= supportFragmentManager
        val transaction = manager.beginTransaction()

        val miperfilFragment = MiperfilFragment()
        transaction.replace(R.id.contenedor,miperfilFragment).commit()
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.drawer_nav, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val manager= supportFragmentManager
        val transaction = manager.beginTransaction()

        when (item.itemId) {
            R.id.nav_Miperfil -> {
                // Handle the camera action
                val miperfilFragment = MiperfilFragment()
                transaction.replace(R.id.contenedor,miperfilFragment).commit()
            }
            R.id.nav_Miservicio -> {
                val miservicioFragment = MiservicioFragment()
                transaction.replace(R.id.contenedor,miservicioFragment).commit()
            }
            R.id.nav_Iniciarsesion -> {
                val IniciarsesionFragment = IniciarsesionFragment()
                transaction.replace(R.id.contenedor,IniciarsesionFragment).commit()
                val intent = Intent(this,LogueoActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_Salir -> {
                val salirFragment = SalirFragment()
                transaction.replace(R.id.contenedor,salirFragment).commit()
            }
            R.id.nav_Cambiarclave -> {
                val cambiarclaveFragment = CambiarclaveFragment()
                transaction.replace(R.id.contenedor,cambiarclaveFragment).commit()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
