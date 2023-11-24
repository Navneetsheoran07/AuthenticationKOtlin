package com.sheoran.authenticationkotlin

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
lateinit var userNameEdt: TextInputEditText



    lateinit var passwordEdt:TextInputEditText
    lateinit var loginBtn: Button
    lateinit var newUserTV: TextView
    lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        userNameEdt = findViewById(R.id.idEdtUserName)
        passwordEdt = findViewById(R.id.idEdtPassword)
        loginBtn = findViewById(R.id.idBtnLogin)
        newUserTV = findViewById(R.id.idTVNewUser)
        mAuth = FirebaseAuth.getInstance()

        newUserTV.setOnClickListener {
            val i = Intent(this@HomeActivity, EmailAndPassword::class.java)
            startActivity(i)
        }
        loginBtn.setOnClickListener(View.OnClickListener {
            val email = userNameEdt.text.toString()
            val password = passwordEdt.text.toString()
            if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                Toast.makeText(
                    this@HomeActivity,
                    "Please enter your Credentials",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@HomeActivity, "Loin Successful", Toast.LENGTH_SHORT).show()
                    val i = Intent(this@HomeActivity, EmailAndPassword::class.java)
                    startActivity(i)
                    finish()
                } else {
                    Toast.makeText(
                        this@HomeActivity,
                        "Please enter valid user Credentials..",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}