package com.example.passwordmanager.Screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.passwordmanager.R
import com.example.passwordmanager.fragment.Document
import com.example.passwordmanager.fragment.Note
import com.google.android.material.appbar.MaterialToolbar

class AddPassword : AppCompatActivity() {
    private lateinit var websitelayout:LinearLayout
    private lateinit var toolbar: MaterialToolbar
    private lateinit var accountLayout: LinearLayout
    private lateinit var accountOptionsLayout: LinearLayout
    private lateinit var appLayout: LinearLayout
    private lateinit var othersLayout: LinearLayout
    private lateinit var bankCardLayout: LinearLayout
    private lateinit var takeAPhotoLayout: LinearLayout
    private lateinit var documentLayout: LinearLayout
    private lateinit var addressLayout: LinearLayout
    private lateinit var noteLayout: LinearLayout
    private lateinit var folderLayout: LinearLayout
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_password)
        toolbar = findViewById(R.id.appBarLayout)
        accountLayout = findViewById(R.id.Account)
        accountOptionsLayout = findViewById(R.id.Accountoptions)
        appLayout = findViewById(R.id.app)
        othersLayout = findViewById(R.id.others)
        bankCardLayout = findViewById(R.id.bank_card)
        takeAPhotoLayout = findViewById(R.id.take_a_photo)
        documentLayout = findViewById(R.id.document)
        addressLayout = findViewById(R.id.address)
        noteLayout = findViewById(R.id.note)
        folderLayout = findViewById(R.id.folder)
        websitelayout=findViewById(R.id.website)
        websitelayout.setOnClickListener {
            com.example.passwordmanager.fragment.Website().display(supportFragmentManager)
        }
        appLayout.setOnClickListener {
            com.example.passwordmanager.fragment.App().display(supportFragmentManager)
        }
        othersLayout.setOnClickListener {
            com.example.passwordmanager.fragment.Others().display(supportFragmentManager)
        }
        bankCardLayout.setOnClickListener {
            com.example.passwordmanager.fragment.Bank_Card().display(supportFragmentManager)
        }
        addressLayout.setOnClickListener {
            com.example.passwordmanager.fragment.Address().display(supportFragmentManager)
        }
        othersLayout.setOnClickListener {
            com.example.passwordmanager.fragment.Others().display(supportFragmentManager)
        }
        documentLayout.setOnClickListener {
            Document().display(supportFragmentManager)
        }
        accountLayout.setOnClickListener {
            if(accountOptionsLayout.visibility == View.GONE) {
                accountOptionsLayout.alpha = 0f
                accountOptionsLayout.translationY = -accountOptionsLayout.height.toFloat()
                accountOptionsLayout.visibility = View.VISIBLE
                accountOptionsLayout.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(500)
                    .setListener(null)
            } else {
                accountOptionsLayout.animate()
                    .alpha(0f)
                    .translationY(-accountOptionsLayout.height.toFloat())
                    .setDuration(500)
                    .withEndAction {
                        accountOptionsLayout.visibility = View.GONE
                    }
            }
        }
        noteLayout.setOnClickListener {
            Note().display(supportFragmentManager)
        }
    }
}