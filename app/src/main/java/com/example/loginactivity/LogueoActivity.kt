package com.example.loginactivity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.android.synthetic.main.activity_logueo.*
import kotlinx.android.synthetic.main.activity_registro.*
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit


class LogueoActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient:GoogleSignInClient
    private lateinit var callbackManager:CallbackManager
    lateinit var callBackPhone:PhoneAuthProvider.OnVerificationStateChangedCallbacks
    val RC_SIGN_IN = 123
    var banderaGoogle = false
    var verificacionId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logueo)


        printKeyHash()

        auth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()

        login_button.setReadPermissions("email")
        login_button.setOnClickListener {
            signInFacebook()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


        sign_in_button.setOnClickListener{
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
            banderaGoogle = true
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
        bnVerificar.setOnClickListener {
            verificacion()
        }
        bnAutenticar.setOnClickListener {
            autenticacionPhone()
        }
    }

    private fun autenticacionPhone() {
        var verificarNumero = etVerificacion.text.toString()

        val credential = PhoneAuthProvider.getCredential(verificacionId,verificarNumero)
        SignInPhone(credential)
    }

    private fun verificacion() {

        verificationCallbacks()

        val phoneNumber = etPhoneNumbertxt.text.toString()

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            this,
            callBackPhone
        )
    }

    private fun verificationCallbacks(){
     callBackPhone = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
         override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            SignInPhone(credential)
         }

         override fun onVerificationFailed(p0: FirebaseException) {

         }

         override fun onCodeSent(verification: String, p1: PhoneAuthProvider.ForceResendingToken) {
             super.onCodeSent(verification, p1)

             verificacionId = verification
         }

     }
    }

    private fun SignInPhone(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user
                    updateUI(user)
                    // ...
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this,"Falló la utenticación",Toast.LENGTH_SHORT).show()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                }
            }
    }


    private fun signInFacebook() {
        login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                handleFacebookAccessToken(result!!.accessToken)
            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException?) {

            }

        })
    }

    private fun handleFacebookAccessToken(accessToken: AccessToken?) {

        val credential = FacebookAuthProvider.getCredential(accessToken!!.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithCredential:success")
                val user = auth.currentUser
                updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithCredential:failure", task.exception)
                Toast.makeText(baseContext, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
                updateUI(null)
            }

            // ...
        }
    }

    private fun printKeyHash() {
        try{
            val info = packageManager.getPackageInfo("com.example.loginactivity",PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KEYHASH", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        }catch (e:PackageManager.NameNotFoundException){

        }catch (e:NoSuchAlgorithmException){

        }
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode, resultCode, data)

        if(banderaGoogle==true){
            if (requestCode == RC_SIGN_IN) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account!!)
                } catch (e: ApiException) {
                    updateUI(null)

                }
            }
        }else{
            banderaGoogle = false
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

    companion object {
        private const val TAG = "FacebookLogin"
    }
}
