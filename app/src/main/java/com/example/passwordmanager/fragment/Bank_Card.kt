package com.example.passwordmanager.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.passwordmanager.Data_Class.BankCardData
import com.example.passwordmanager.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    private lateinit var cardTypeIcon: ImageView
    private lateinit var cardScan: TextView
    private lateinit var cardIssuerField: TextInputEditText
    private lateinit var cardCustomerServiceField: TextInputEditText
    private lateinit var cardColorField: TextInputEditText
    private lateinit var cardCommentField: TextInputEditText
    private lateinit var  cardTypeLayout : TextInputLayout

    private val TAG="Bank_Card"
    private lateinit var toolbar: MaterialToolbar
    fun display(fragmentManager: FragmentManager):Bank_Card?{
        val bank_card=Bank_Card()
        if(fragmentManager!=null){
            bank_card.show(fragmentManager,TAG)
        }
        return bank_card
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_bank__card, container, false)
        toolbar=view.findViewById(R.id.appBarLayout)
        nameValue = view.findViewById(R.id.Name_value)
        cardNumberField = view.findViewById(R.id.Card_Number_field)
        cardTypeField = view.findViewById(R.id.Card_Type_field)
        cardTypeLayout = view.findViewById(R.id.Card_Type_layout)
//        cardTypeIcon = view.findViewById(R.id.cardTypeIcon)
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
        cardNumberField?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val cardNumber = s.toString()
                val detectedCardType = detectCardType(cardNumber)
                cardTypeField.setText(detectedCardType)
                val drawableId = getDrawableCardType(detectedCardType)
                val StartIconDrawable = drawableId?.let { ContextCompat.getDrawable(requireContext(), it) }
                if (StartIconDrawable != null) {
//                    cardTypeIcon.setImageDrawable(StartIconDrawable)
//                    cardTypeLayout.startIconDrawable=StartIconDrawable
                }
            }
        })

        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.setOnMenuItemClickListener {menuitem->
            when(menuitem.itemId){
                R.id.action_save->{
//                    SaveData()
                }
            }
            dismiss()
            true
        }
//        cardTypeField.setOnClickListener {
//            CardTypeShow()
//        }
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
    private fun getDrawableCardType(cardType: String): Int? {
        return when(cardType){
            "Visa"->R.drawable.visa
            "MasterCard"->R.drawable.mastercard
            "American Express"->R.drawable.american_express
            "Carte Bancaire"->R.drawable.carte_bleue
            "Carte Blanche"->R.drawable.carte_bleue
            "Carte Bleue"->R.drawable.carte_bleue
            "China UnionPay"->R.drawable.unionpay
            "Dankort"->R.drawable.dankort
            "Delta"->R.drawable.delta
            "Diners Club"->R.drawable.diners_club
            "Discover"->R.drawable.discover
//            "Inter Payment"->R.drawable.app
            "JCB"->R.drawable.jcb
            "MIR"->R.drawable.mir
            "Maestro"->R.drawable.maestro
            "Solo"->R.drawable.solo
            "Switch"->R.drawable.switch_card
            "UATP"->R.drawable.uatp
            "Visa Electron"->R.drawable.visa_elect
            else-> null
        }
    }
    private fun detectCardType(cardNumber: String): String {
        val cardtype= mapOf(
            "Visa" to listOf("4"),
            "MasterCard" to listOf("51","52","53","54","55"),
            "American Express" to listOf("34", "37"),
            "Carte Bancaire" to listOf("30", "31", "32", "33", "34", "35", "36", "37"),
            "Carte Blanche" to listOf("300", "301", "302", "303", "304", "305"),
            "Carte Bleue" to listOf("30", "31", "32", "33", "34", "35", "36", "37"),
            "China UnionPay" to listOf("62"),
            "Dankort" to listOf("5019"),
            "Delta" to listOf("45", "46", "47"),
            "Diners Club" to listOf("300", "301", "302", "303", "304", "305", "36"),
            "Discover" to listOf("6011", "622", "644", "645", "646", "647", "648", "649", "65"),
            "Inter Payment" to listOf("636"),
            "JCB" to listOf("35"),
            "MIR" to listOf("2200", "2201", "2202", "2203", "2204"),
            "Maestro" to listOf("50", "56", "57", "58", "6"),
            "Solo" to listOf("6334", "6767"),
            "Switch" to listOf("4903", "4905", "4911", "4936", "564182", "633110", "6333", "6759"),
            "UATP" to listOf("1"),
            "Visa Electron" to listOf("4026", "417500", "4508", "4844", "4913", "4917")
        )
        for ((cardType,prefix) in cardtype ){
            for(prefixs in prefix){
                if(cardNumber.startsWith(prefixs)){
                    return cardType
                }
            }
        }
        return "Unknown"
    }

    private fun SaveData(name:String,cardNumber:String,cardType:String,cardholder:String,
                         validThrough:String,cvc:String,pin:String,cardIssuer:String,
                         CustomerPhone:String,cardColor:String,comment:String){
        val firestore= FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid
        if(cardNumber.isEmpty()){
            cardNumberField.error="Enter A Longer Card Number"
        }
        else{
            val card=BankCardData(
                null,
                name, cardNumber, cardType,
                cardholder,validThrough,cvc,pin,
                cardIssuer,CustomerPhone,cardColor,comment
            )
            firestore.collection("Bank_Card")
                .document(FirebaseAuth.getInstance().uid.toString())
                .collection("BankCardList")
                .add(card)
                .addOnSuccessListener {documentref->
                    val newdocid=documentref.id
                    card.id=newdocid
                    Toast.makeText(requireContext(),"Bank Card Details added successfully",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(),"Something went wrong ${e.message}",Toast.LENGTH_SHORT).show()
                }
        }
    }

}