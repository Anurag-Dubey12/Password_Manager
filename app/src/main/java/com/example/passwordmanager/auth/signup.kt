package com.example.passwordmanager.auth

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.passwordmanager.MainActivity
import com.example.passwordmanager.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

//Register
class signup : AppCompatActivity() {
    private lateinit var usernameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var signUpButton: MaterialButton
    private lateinit var googleButton: MaterialButton
    private lateinit var progress_indi: LinearProgressIndicator
    val auth=FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        usernameEditText = findViewById(R.id.signup_username)
        emailEditText = findViewById(R.id.signup_email)
        passwordEditText = findViewById(R.id.signup_password)
        signUpButton = findViewById(R.id.signup)
        progress_indi = findViewById(R.id.progress_indi)
        googleButton = findViewById(R.id.google)

        signUpButton.setOnClickListener {
            val username=usernameEditText.text.toString()
            val email=emailEditText.text.toString()
            val password=passwordEditText.text.toString()
            val validpassword = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$".toRegex()

            if(username.isEmpty()){
                usernameEditText.error="Enter Username first"
            }
            else if(email.isEmpty()){
                emailEditText.error="Enter Email first"
            }
            else if(password.isEmpty() && password.matches(validpassword)){
                passwordEditText.error="Enter Password"
            }
            else{
                createuser(username,email,password)
            }
        }

    }
    private fun createuser(username:String,email: String, password: String) {
        showProgressBar()
        auth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                val user=it.user
                user?.getIdToken(true)
                    ?.addOnCompleteListener {
                        hideProgressBar()
                        if (it.isSuccessful){
                            val token=it.result?.token
                            firestore.collection("User")
                                .document(FirebaseAuth.getInstance().uid.toString())
                                .set(UserModel(username,email,password))
                                .addOnCompleteListener {
                                    if(it.isSuccessful){
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
                                        Toast.makeText(this,"SignUp Successfully", Toast.LENGTH_SHORT).show()
                                    }
                                    else{
                                        Toast.makeText(this,"Something went wrong", Toast.LENGTH_SHORT).show()

                                    }
                                }
                        }
                    }
                    ?.addOnFailureListener {
                        hideProgressBar()
                        Toast.makeText(this,"Something went wrong", Toast.LENGTH_SHORT).show()

                    }
            }
    }
    private fun showProgressBar() {
        progress_indi.visibility = View.VISIBLE
        signUpButton.isEnabled = false
    }

    private fun hideProgressBar() {
        progress_indi.visibility = View.GONE
        signUpButton.isEnabled = true
    }

}