package com.example.passwordmanager.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.passwordmanager.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Note : DialogFragment() {
    private val TAG="Note_Dialog"
    private lateinit var toolbar: MaterialToolbar
    private lateinit var Notes_Name: TextInputEditText
    private lateinit var Notes_text: TextInputEditText
    fun display(fragmentManager: FragmentManager):Note{
        val note=Note()
        if(fragmentManager!=null){
            note.show(fragmentManager,TAG)
        }
        return note
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_note, container, false)
        toolbar=view.findViewById(R.id.appBarLayout)
        Notes_Name = view.findViewById(R.id.Note_name_field)
        Notes_text = view.findViewById(R.id.Note_text_field)
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