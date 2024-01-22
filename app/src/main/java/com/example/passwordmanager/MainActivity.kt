package com.example.passwordmanager

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.passwordmanager.Screen.AddPassword
import com.example.passwordmanager.fragment.AddSection
import com.example.passwordmanager.fragment.Dashboard_fragment
import com.example.passwordmanager.fragment.SettingFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import me.ibrahimsn.lib.SmoothBottomBar

class MainActivity : AppCompatActivity() {
    private lateinit var fragmentContainer: FrameLayout
    private lateinit var addpassword: FloatingActionButton
    lateinit var  dashboard:Dashboard_fragment
    lateinit var  setting:SettingFragment
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var bottom:SmoothBottomBar=findViewById(R.id.bottomBar)
        fragmentContainer=findViewById(R.id.fragmentContainer)
        addpassword=findViewById(R.id.addpassword)
        dashboard = Dashboard_fragment()
        setting = SettingFragment()
        bottom.setOnItemSelectedListener {index->
            when(index){
                0-> changefragment(dashboard)
                1-> changefragment(setting)
            }
        }
        addpassword.setOnClickListener {
            Intent(this,AddPassword::class.java).also {
                startActivity(it)
            }
//            AddSection().display(supportFragmentManager)
        }
    }
    fun changefragment(fragname:Fragment){
        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer,fragname)
        fragmentTransaction.commit()
    }
}