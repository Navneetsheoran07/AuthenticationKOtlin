package com.sheoran.authenticationkotlin

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class EmailAndPassword : AppCompatActivity() {
    lateinit var userNameEdt: TextInputEditText

    lateinit var passwordEdt:TextInputEditText
    lateinit var confirmPwdEdt:TextInputEditText

    lateinit var loginTv: TextView
    lateinit var registerBtn: Button
    lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_and_password)
        userNameEdt = findViewById(R.id.idEdtUserName)
        passwordEdt = findViewById(R.id.idEdtPassword)

        confirmPwdEdt = findViewById(R.id.idEdtConfirmPassword)
        loginTv = findViewById(R.id.idTVLoginUser)
        registerBtn = findViewById(R.id.idBtnRegister)
        mAuth = FirebaseAuth.getInstance()
        loginTv.setOnClickListener {
            val i = Intent(this@EmailAndPassword, HomeActivity::class.java)
            startActivity(i)
        }
        registerBtn.setOnClickListener {
            val userNAme = userNameEdt.text.toString()
            val pwd = passwordEdt.text.toString()
            val cnfpwd = confirmPwdEdt.text.toString()
            if (pwd != cnfpwd) {
                Toast.makeText(this@EmailAndPassword, "Not mAtch", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(userNAme) && TextUtils.isEmpty(
                    pwd
                ) && TextUtils.isEmpty(cnfpwd)
            ) {
                Toast.makeText(this@EmailAndPassword, "Please Enter your Data", Toast.LENGTH_SHORT)
                    .show()
            } else {
                mAuth.createUserWithEmailAndPassword(userNAme, pwd).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@EmailAndPassword,
                            "User Registered..",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        val i = Intent(this@EmailAndPassword, HomeActivity::class.java)
                        startActivity(i)
                        finish()
                    } else {
                        Toast.makeText(
                            this@EmailAndPassword,
                            "Fail to Register user..",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}