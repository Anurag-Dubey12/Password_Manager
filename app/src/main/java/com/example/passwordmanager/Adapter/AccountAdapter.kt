package com.example.passwordmanager.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.passwordmanager.Data_Class.Website
import com.example.passwordmanager.R
import com.google.android.material.textfield.TextInputEditText

class AccountAdapter(private val context: Context) : RecyclerView.Adapter<AccountAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var loginValue: TextInputEditText
        private lateinit var passwordValue: TextInputEditText
        private lateinit var commentValue: TextInputEditText
        private lateinit var menuOption: ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.account_adding, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }


}
