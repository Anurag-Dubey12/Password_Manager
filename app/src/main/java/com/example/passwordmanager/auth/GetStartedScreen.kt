package com.example.passwordmanager.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.passwordmanager.R
import com.google.android.material.button.MaterialButton

class GetStartedScreen : AppCompatActivity() {
    private lateinit var get_started:MaterialButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started_screen)
        get_started=findViewById(R.id.get_started)
        get_started.setOnClickListener {
            Intent(this,Signin::class.java).also {
                startActivity(it)
            }
        }
    }
}