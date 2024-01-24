package com.example.passwordmanager.fragment

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.passwordmanager.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Bank_Card : DialogFragment() {
    private  val CAMERA_PERMISSION = 7762
    private lateinit var nameValue: TextInputEditText
    private lateinit var cardNumberField: TextInputEditText
    private lateinit var cardTypeField: TextInputEditText
    private lateinit var cardHolderField: TextInputEditText
    private lateinit var cardValidField: TextInputEditText
    private lateinit var cardCvvField: TextInputEditText
    private lateinit var cardPinField: TextInputEditText
    private lateinit var cardScan: TextView
    private lateinit var cardIssuerField: TextInputEditText
    private lateinit var cardCustomerServiceField: TextInputEditText
    private lateinit var cardColorField: TextInputEditText
    private lateinit var cardCommentField: TextInputEditText
    private val TAG="Bank_Card"
    private lateinit var toolbar: MaterialToolbar
    fun display(fragmentManager: FragmentManager):Bank_Card?{
        val bank_card=Bank_Card()
        if(fragmentManager!=null){
            bank_card.show(fragmentManager,TAG)
        }
        return bank_card
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_bank__card, container, false)
        toolbar=view.findViewById(R.id.appBarLayout)
        nameValue = view.findViewById(R.id.Name_value)
        cardNumberField = view.findViewById(R.id.Card_Number_field)
        cardTypeField = view.findViewById(R.id.Card_Type_field)
        cardHolderField = view.findViewById(R.id.card_holder_feild)
        cardValidField = view.findViewById(R.id.Card_valid_field)
        cardCvvField = view.findViewById(R.id.Card_CVV_Field)
        cardPinField = view.findViewById(R.id.Card_Pin_Field)
        cardScan = view.findViewById(R.id.card_scan)
        cardIssuerField = view.findViewById(R.id.Card_Issuer_Field)
        cardCustomerServiceField =view. findViewById(R.id.Card_Customer_service_Field)
        cardColorField = view.findViewById(R.id.Card_Color_Field)
        cardCommentField = view.findViewById(R.id.Card_Comment_Field)
        return view
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL,com.example.passwordmanager.R.style.AppTheme_FullScreenDialog)
    }
    override fun onStart() {
        super.onStart()
        val dialog:Dialog?=dialog
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
        cardScan.setOnClickListener {
            if(ContextCompat.checkSelfPermission(requireContext(),
                    android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                GlobalScope.launch(Dispatchers.IO){
                    Card_Scanning().display(requireFragmentManager())
                }
            }else{
                requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION)
            }

        }
    }

}