package com.sheoran.authenticationkotlin

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    lateinit var  mAuth :FirebaseAuth
 var  currentUser:FirebaseUser?= null
    lateinit var edtphone: EditText
    lateinit var edtotp:EditText

    lateinit var verifyOtpbtn: Button
    lateinit var grenateOtpBtn:android.widget.Button

    lateinit var verificationid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtotp = findViewById(R.id.idEdtOtp)
        edtphone = findViewById(R.id.idEdtPhoneNumber)
        verifyOtpbtn = findViewById(R.id.idBtnVerify)
        grenateOtpBtn = findViewById(R.id.idBtnGetOtp)

        mAuth = FirebaseAuth.getInstance()
        currentUser =mAuth.currentUser


        verifyOtpbtn.setOnClickListener {
            if (TextUtils.isEmpty(edtphone.text.toString())) {
                Toast.makeText(
                    this@MainActivity,
                    "Please enter a valid Otp number",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                verifyCode(edtotp.text.toString())
            }
        }
        grenateOtpBtn.setOnClickListener {
            if (TextUtils.isEmpty(edtphone.text.toString())) {
                Toast.makeText(
                    this@MainActivity,
                    "Please enter a valid phone number",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val phone = "+91" + edtphone.text.toString()
                sendVerificationCode(phone)
            }
        }
    }

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCallback)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    var mCallback: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                verificationid = s
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode
                if (code != null) {
                    edtotp.setText(code)
                    verifyCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationid, code)
        signINWithCredential(credential)
    }

    private fun signINWithCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@MainActivity, task.exception!!.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }
}