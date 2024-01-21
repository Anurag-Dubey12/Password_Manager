package com.example.passwordmanager.Screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.passwordmanager.Adapter.WebsiteAdapter
import com.example.passwordmanager.Data_Class.Website
import com.example.passwordmanager.R

class AddPassword : AppCompatActivity() {
    private lateinit var websiteAdapter: WebsiteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_password)
    }
}