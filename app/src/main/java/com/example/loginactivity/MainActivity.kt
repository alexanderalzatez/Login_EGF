package com.example.loginactivity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.tasks.Task
import androidx.annotation.NonNull
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {

    var correo=""
    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val email = user.email
            val name = user.displayName
            val emailVerify = user.isEmailVerified
            val uid = user.uid
            tvResultadoMain.text = "Bienvenido! "+name+"\n email:"+email+"\n Estado de verificación email: "+emailVerify+"\n uid: "+uid


            val ref = FirebaseDatabase.getInstance().getReference("userEGFP")
            val userEGFP_id = ref.push().key!!
            val usuarioEGFP = UserRealTimeEGFP(userEGFP_id,name,email,emailVerify,uid)


            ref.child(userEGFP_id).setValue(usuarioEGFP).addOnCompleteListener {
                Toast.makeText(applicationContext,"Guardado en la base de datos",Toast.LENGTH_SHORT).show()
            }

        }
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            //tvResultadoMain.text = acct.email
        }
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso)
        auth = FirebaseAuth.getInstance()

        /*var datosRecibidosLo = intent.extras
        correo = datosRecibidosLo!!.getString("correo").toString()
        tvResultadoMain.text = "Bienvenido! " + "\n" + correo + "\n"*/
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.overflow_menu,menu)
        return true
    }

    override fun onStart(){
        super.onStart()
        if(auth.currentUser == null){
            startActivity(Intent(this,LogueoActivity::class.java))
        }else{
            Toast.makeText(this,"Already signed in",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.mCerrarSesion -> {

                LoginManager.getInstance().logOut()//Desconectar de Facebook
                mGoogleSignInClient.signOut() //Desconectar de Google
                auth.signOut() //Desconectar de Firebase
                startActivity(Intent(this,LogueoActivity::class.java))
                Toast.makeText(this,"Has cerrado sesión...",Toast.LENGTH_SHORT).show()
                finish()

                /*var intent2 = Intent(this, LogueoActivity::class.java)
                Toast.makeText(this,"Has cerrado sesión...",Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK, intent2)
                //startActivity(intent2)
                finish()*/
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        finish()
        /*var intent2 = Intent(this, LogueoActivity::class.java)
        Toast.makeText(this,"Has cerrado la aplicación...",Toast.LENGTH_SHORT).show()
        setResult(Activity.RESULT_CANCELED, intent2)
        finish()*/
    }
}
