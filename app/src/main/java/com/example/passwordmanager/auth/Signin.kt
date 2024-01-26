package com.example.passwordmanager.auth

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.passwordmanager.MainActivity
import com.example.passwordmanager.R
import com.google.android.material.button.MaterialButton

class Signin : AppCompatActivity() {
    private lateinit var log_in: MaterialButton
    private lateinit var Sign_up: MaterialButton
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        log_in=findViewById(R.id.Login_button)
        Sign_up=findViewById(R.id.signup_button)

        log_in.setOnClickListener {
            Intent(this,MainActivity::class.java).also {
                startActivity(it)
            }
        }
        Sign_up.setOnClickListener {
            Intent(this,signup::class.java).also {
                startActivity(it)
            }
        }


    }
}