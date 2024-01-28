package com.example.passwordmanager.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.passwordmanager.MainActivity
import com.example.passwordmanager.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class GetStartedScreen : AppCompatActivity() {
    val isFirstlaunch:Boolean=false
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
        checksignin()
    }
    private fun checksignin() {
        val loggoogleaccount = GoogleSignIn.getLastSignedInAccount(this)
        if (loggoogleaccount != null) { Intent(this, MainActivity::class.java).also { startActivity(it) } }
        val currentuser = FirebaseAuth.getInstance().currentUser
        if (currentuser != null) { Intent(this, MainActivity::class.java).also { startActivity(it)
            finish()
        } } }
}