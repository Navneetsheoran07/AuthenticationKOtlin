package com.sheoran.authenticationkotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class GoogleAuth : AppCompatActivity() {

    lateinit var btSignIN: SignInButton
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_auth)
        btSignIN = findViewById(R.id.bt_sign_in)
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("357338695778-ej3o81j92756l74v93s4r288bti8et7j.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this@GoogleAuth, googleSignInOptions)
        btSignIN.setOnClickListener {
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, 100)
        }
        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            startActivity(
                Intent(
                    this@GoogleAuth,
                    HomeActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            val signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (signInAccountTask.isSuccessful) {
                val s = "Google Sign inSuccessful"
                Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
                try {
                    val googleSignInAccount = signInAccountTask.getResult(
                        ApiException::class.java
                    )
                    if (googleSignInAccount != null) {
                        val authCredential = GoogleAuthProvider.getCredential(
                            googleSignInAccount
                                .idToken, null
                        )
                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(
                            this
                        ) { task ->
                            if (task.isComplete) {
                                startActivity(
                                    Intent(this@GoogleAuth, HomeActivity::class.java)
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                )
                            } else {
                                Toast.makeText(
                                    this@GoogleAuth,
                                    "Authentication Failed: " + task.exception,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } catch (e: ApiException) {
                    throw RuntimeException(e)
                }
            }
        }
    }
}