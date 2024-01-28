package com.example.passwordmanager.auth

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.passwordmanager.MainActivity
import com.example.passwordmanager.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class Signin : AppCompatActivity() {
    private lateinit var log_in: MaterialButton
    private lateinit var Sign_up: MaterialButton
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var googleButton: MaterialButton
    private lateinit var reset_password: TextView
    private lateinit var progress_indi: LinearProgressIndicator
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    val auth=FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val usersReference = database.reference.child("users")
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        log_in=findViewById(R.id.Login_button)
        Sign_up=findViewById(R.id.signup_button)
        reset_password=findViewById(R.id.reset_password)
        progress_indi = findViewById(R.id.progress_indi)
        emailEditText = findViewById(R.id.signup_email)
        passwordEditText = findViewById(R.id.signup_password)
        googleButton = findViewById(R.id.google)
        log_in.setOnClickListener {
            val email=emailEditText.text.toString()
            val pass=passwordEditText.text.toString()
            if(email.isEmpty()){
                emailEditText.error="Email cannot be empty!"
            }
            else if(pass.isEmpty()){
                passwordEditText.error = "Password cannot be empty!"
            }
            else{
            logIn(email,pass)

            }
        }
        Sign_up.setOnClickListener {
            Intent(this,signup::class.java).also {
                startActivity(it)
            }
        }
        reset_password.setOnClickListener {
            resetPassword()
        }
//        AuthenticateBiometric()

    }

    fun logIn(email:String,password:String){
        showProgressBar()
        auth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                hideProgressBar()
                val userDetails=this.getSharedPreferences("User_Details",
                    MODE_PRIVATE)
                val editor=userDetails.edit()
                editor.putBoolean("isFirstTime",true)
                editor.putString("email",email)
                editor.apply()
                Intent(this, MainActivity::class.java).also {
                    startActivity(it)
                }
                    finish()
                displayMessage("Signing Successfully")
            }
            .addOnFailureListener { e->
                displayMessage("Email or Password is Invalid\n"+e.message)
               hideProgressBar()
            }
    }
    private fun resetPassword(){
        val email=emailEditText.text.toString()
        if(email.isEmpty()){
            emailEditText.error="Email cannot be empty!"
        }
        else{
            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    displayMessage("Check Email box!!")
                    hideProgressBar()
                }
                .addOnFailureListener {e->
                    displayMessage("Invalid email"+e.message)
                    hideProgressBar()
                }
        }
    }
    private fun AuthenticateBiometric() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                displayMessage("Biometric authentication is available")
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                displayMessage("This device doesn't support biometric authentication")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                displayMessage("Biometric authentication is currently unavailable")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                displayMessage("No biometric credentials are enrolled")
        }
        val executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                displayMessage("Authentication error: $errString")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                displayMessage("Authentication succeeded!")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                displayMessage("Authentication failed")
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()
        biometricPrompt.authenticate(promptInfo)
    }

    private fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    private fun showProgressBar() {
        progress_indi.visibility = View.VISIBLE
        log_in.isEnabled = false
    }

    private fun hideProgressBar() {
        progress_indi.visibility = View.GONE
        log_in.isEnabled = true
    }
}