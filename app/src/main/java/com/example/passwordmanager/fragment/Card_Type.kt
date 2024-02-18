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
class Card_Type : DialogFragment() {
    private val TAG="CARD_TYPE"
    fun display(fragmentManager: FragmentManager):Card_Type{
        val cardtype=Card_Type()
        if(fragmentManager!=null){
            cardtype.show(fragmentManager,TAG)
        }
        return cardtype
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_card__type, container, false)
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

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        toolbar.setNavigationOnClickListener { dismiss() }
//        toolbar.setOnMenuItemClickListener {
//            dismiss()
//            true
//        }
//    }
}