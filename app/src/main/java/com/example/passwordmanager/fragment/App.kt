package com.example.passwordmanager.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.passwordmanager.R
import com.google.android.material.appbar.MaterialToolbar

class App : DialogFragment() {
    private val TAG="App_Dialog"
    private lateinit var toolbar:MaterialToolbar
    fun display(fragmentManager: FragmentManager):App{
        val app=App()
        if(fragmentManager!=null){
            app.show(fragmentManager,TAG)
        }
        return app
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View=inflater.inflate(R.layout.fragment_app, container, false)
        toolbar=view.findViewById(R.id.appBarLayout)
        return view
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL,com.example.passwordmanager.R.style.AppTheme_FullScreenDialog)
    }
    override fun onStart() {
        super.onStart()
        val dialog: Dialog?=dialog
        dialog?.let {
            val width=ViewGroup.LayoutParams.FILL_PARENT
            val height=ViewGroup.LayoutParams.FILL_PARENT
            it.window?.setLayout(width,height)
            it.window?.attributes?.windowAnimations=R.style.AppTheme_Slide
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.setOnMenuItemClickListener {
            dismiss()
            true
        }
    }


}