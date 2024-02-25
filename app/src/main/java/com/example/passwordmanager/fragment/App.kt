package com.example.passwordmanager.fragment

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.airbnb.lottie.LottieAnimationView
import com.example.passwordmanager.Data_Class.AppData
import com.example.passwordmanager.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class App : DialogFragment() {
    private lateinit var namevalue: TextInputEditText
    private lateinit var pass_para_layout: CardView
    private lateinit var loginvalue: TextInputEditText
    private lateinit var passwordvalue: TextInputEditText
    private lateinit var generatePassword: TextView
    private lateinit var lottieAnimationView: LottieAnimationView
    private lateinit var commentEditText: TextInputEditText
    private lateinit var passwordStrength: TextView
    private  lateinit var passwordField: TextView
    private lateinit var passwordParameter: TextView
    private lateinit var passwordLength: TextView
    private  lateinit var passwordLengthSlider: Slider
    private lateinit var numericCheckbox: MaterialCheckBox
    private lateinit var capitalAlphaCheckbox: MaterialCheckBox
    private lateinit var symbolCheckbox: MaterialCheckBox
    private lateinit var newPasswordImage: ImageView
    private lateinit var usePasswordButton: MaterialButton
    var includelowerCase=true
    var includeupperCase=false
    var includenumber=false
    var includesymbol=false
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
        loginvalue = view.findViewById(R.id.login_value)
        namevalue = view.findViewById(R.id.Name_value)
        passwordvalue = view.findViewById(R.id.Password_value)
        generatePassword = view.findViewById(R.id.generate_password)
        commentEditText = view.findViewById(R.id.Comment_value)
        lottieAnimationView = view.findViewById(R.id.lottieAnimationView)
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
        passwordvalue.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                generatePassword.visibility=View.VISIBLE
            }
            else{
                generatePassword.visibility=View.GONE
            }
        }
        generatePassword.setOnClickListener{
            showPasswordLayout()
        }
        toolbar.setOnMenuItemClickListener(){menuitem->
            when(menuitem.itemId){
//                R.id.action_add->{
//                    addAccountAddingLayout()
//
//                }
                R.id.action_save->{
                    val name=namevalue.text.toString()
                    val login=loginvalue.text.toString()
                    val password=passwordvalue.text.toString()
                    val comment=commentEditText.text.toString()
                    SaveData(name,login,password,comment)
                    dismiss()
                }
            }
            true
        }
    }
    private fun showPasswordLayout(){
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.generate_password, null)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.setTitle("Password Generator")
        bottomSheetDialog.show()
        var passwordlength=16
        passwordParameter=view.findViewById(R.id.password_parameter)
        pass_para_layout=view.findViewById(R.id.Password_parameter_layout)
        passwordStrength = view.findViewById(R.id.password_strength)
        passwordField = view.findViewById(R.id.password_field)
        passwordParameter = view.findViewById(R.id.password_parameter)
        passwordLength = view.findViewById(R.id.password_length)
        passwordLengthSlider = view.findViewById(R.id.password_length_slider)
        numericCheckbox = view.findViewById(R.id.numeric)
        capitalAlphaCheckbox = view.findViewById(R.id.CapitalAlpha)
        symbolCheckbox = view.findViewById(R.id.symbol)
        newPasswordImage = view.findViewById(R.id.new_password)
        usePasswordButton = view.findViewById(R.id.use_password)
        val ispass_visi=pass_para_layout.visibility==View.VISIBLE
        passwordParameter.setOnClickListener { pass_para_layout.visibility=if(ispass_visi) View.GONE else View.VISIBLE }
        passwordLengthSlider.addOnChangeListener { slider, value, fromUser ->
            passwordLength.text=value.toString()
            passwordlength= value.toInt()
            val password=GeneratePassword(passwordlength,includelowerCase,includeupperCase,includenumber,includesymbol)
            passwordField.text = password

            val password_strength=when(passwordlength){
                in 4..8->"Weak"
                in 9..12->"Medium"
                in 13..16->"Strong"
                else->"Very Strong"
            }
            passwordStrength.text=password_strength
        }
        numericCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            includenumber = isChecked
            val password=GeneratePassword(passwordlength,includelowerCase,includeupperCase,includenumber,includesymbol)
            passwordField.text = password
        }
        capitalAlphaCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            includeupperCase = isChecked
            val password=GeneratePassword(passwordlength,includelowerCase,includeupperCase,includenumber,includesymbol)
            passwordField.text = password
        }
        symbolCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            includesymbol = isChecked
            val password=GeneratePassword(passwordlength,includelowerCase,includeupperCase,includenumber,includesymbol)
            passwordField.text = password
        }
        val password=GeneratePassword(passwordlength,includelowerCase,includeupperCase,includenumber,includesymbol)
        passwordField.text = password
        newPasswordImage.setOnClickListener {
            val new_pass=GeneratePassword(passwordlength,includelowerCase,includeupperCase,includenumber,includesymbol)
            passwordField.text = new_pass
        }
        usePasswordButton.setOnClickListener {
            val value = passwordField.text.toString()
            val editableValue = Editable.Factory.getInstance().newEditable(value)
            passwordvalue.text = editableValue
            bottomSheetDialog.dismiss()
        }
    }
    private fun GeneratePassword(length:Int,includeLowerCase:Boolean,includeUpperCase:Boolean,
                                 includeNumber:Boolean,includeSymbol:Boolean):SpannableString {
        val lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz"
        val upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val numbers = "0123456789"
        val specialSymbols = "!@#$%^&*()_+-=[]{}|;:,.<>?/~"

        var charPool=""
        if (includeLowerCase) charPool += lowerCaseLetters
        if (includeUpperCase) charPool += upperCaseLetters
        if (includeNumber) charPool += numbers
        if (includeSymbol) charPool += specialSymbols
        if (charPool.isEmpty()) {
            throw IllegalArgumentException("At least one option must be selected")
        }

        val password=(1..length)
            .map { charPool.random() }
            .joinToString ("")
        val spannableString=SpannableString(password)
        for (i in password.indices){
            val char=password[i]
            val color=when{
                char in lowerCaseLetters || char in upperCaseLetters-> Color.WHITE
                char in specialSymbols->Color.GREEN
                else->Color.YELLOW
            }
            spannableString.setSpan(ForegroundColorSpan(color), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return spannableString
    }
    private fun SaveData(name:String,login:String,password:String,comment:String){
        val firestore=FirebaseFirestore.getInstance()
        val uid=FirebaseAuth.getInstance().uid.toString()

        val app_data=AppData(
            null,
            name,
            login,
            password,
            comment
        )
        firestore.collection(uid)
            .document("User_Data")
            .collection("App Data")
            .add(app_data)
            .addOnSuccessListener {documentRef->
                val newDocId = documentRef.id
                app_data.uid = newDocId
                documentRef.update("uid", newDocId)

            }
            .addOnFailureListener { e ->
//                Toast.makeText(requireContext(),"Something went wrong ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}