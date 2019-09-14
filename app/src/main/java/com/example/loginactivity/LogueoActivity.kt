package com.example.loginactivity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_logueo.*
import kotlinx.android.synthetic.main.activity_registro.*
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider


class LogueoActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient:GoogleSignInClient
    val RC_SIGN_IN = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logueo)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


        sign_in_button.setOnClickListener{
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        bnRegistro.setOnClickListener {

            startActivity(Intent(this,RegistroActivity::class.java))
            finish()

        }

        bnIniciarSesion.setOnClickListener {

            if(etCorreo.text.toString()!="" && etContraseña.text.toString()!="") {
                auth.signInWithEmailAndPassword(etCorreo.text.toString(), etContraseña.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            updateUI(user)
                        } else {
                            updateUI(null)
                        }
                    }
            }else{
                Toast.makeText(baseContext, "Falló la autenticación.",Toast.LENGTH_SHORT).show()
            }

        }
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                updateUI(null)

            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    updateUI(null)
                }
            }
    }


    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser!=null){
            startActivity(Intent(this,MainActivity::class.java))
            Toast.makeText(baseContext, "Iniciando sesión...",Toast.LENGTH_SHORT).show()
            finish()
        }else{
            Toast.makeText(baseContext, "Falló la autenticación.",Toast.LENGTH_SHORT).show()
        }
    }

}
