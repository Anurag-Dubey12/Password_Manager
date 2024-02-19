package com.example.passwordmanager.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.passwordmanager.Adapter.AccountAdapter
import com.example.passwordmanager.Data_Class.WebsiteData
import com.example.passwordmanager.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class Website : DialogFragment(){
    val TAG="WebSite_Dialog"
    private lateinit var toolbar:MaterialToolbar
    private lateinit var parentLinearLayout: LinearLayout
    private lateinit var webAddressvalue: TextInputEditText
    private lateinit var namevalue: TextInputEditText
    private lateinit var loginvalue: TextInputEditText
    private lateinit var passwordvalue: TextInputEditText
    private lateinit var generatePassword: TextView
    private lateinit var lottieAnimationView: LottieAnimationView
    private lateinit var commentEditText: TextInputEditText
    fun display(fragmentManager: FragmentManager):Website?{
        val web=Website()
        if(fragmentManager!=null){
            web.show(fragmentManager,TAG)
        }
        return web
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL,com.example.passwordmanager.R.style.AppTheme_FullScreenDialog)
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View=inflater.inflate(R.layout.fragment_website, container, false)
        toolbar=view.findViewById(R.id.appBarLayout)
        parentLinearLayout = view.findViewById(R.id.linearLayout2)
        loginvalue = view.findViewById(R.id.login_value)
        webAddressvalue = view.findViewById(R.id.Web_address_value)
        namevalue = view.findViewById(R.id.Name_value)
        passwordvalue = view.findViewById(R.id.Password_value)
        generatePassword = view.findViewById(R.id.generate_password)
        commentEditText = view.findViewById(R.id.Comment_value)
        lottieAnimationView = view.findViewById(R.id.lottieAnimationView)
        return view
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog?=dialog
        dialog?.let {
            val width=ViewGroup.LayoutParams.FILL_PARENT
            val height=ViewGroup.LayoutParams.FILL_PARENT
            it.window?.setLayout(width,height)
            it.window?.attributes?.windowAnimations= com.example.passwordmanager.R.style.AppTheme_Slide
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.title = "Website"
        toolbar.setOnMenuItemClickListener(){menuitem->
            when(menuitem.itemId){
                R.id.action_add->{
                    addAccountAddingLayout()

                }
                R.id.action_save->{
                    val web=webAddressvalue.text.toString()
                    val name=namevalue.text.toString()
                    val login=loginvalue.text.toString()
                    val password=passwordvalue.text.toString()
                    val comment=commentEditText.text.toString()
                    SaveData(web,name,login,password,comment)
                    dismiss()
                }
            }
            true
        }
    }
    private fun addAccountAddingLayout() {
        val inflater = LayoutInflater.from(requireContext())
        val accountAddingLayout =
            inflater.inflate(R.layout.account_adding, parentLinearLayout, false) as ConstraintLayout
        parentLinearLayout.addView(accountAddingLayout)
    }
    private fun SaveData(web_address:String,name:String,login:String,Password:String,comment:String){
        val firestore=FirebaseFirestore.getInstance()
        if(web_address.isEmpty()&&name.isEmpty()&&login.isEmpty()&&Password.isEmpty()){
            Toast.makeText(requireContext(),"Enter all required details",Toast.LENGTH_SHORT).show()
        }
        else{
            val uid=FirebaseAuth.getInstance().uid.toString()
            Log.d("Uid","The current user uid is:$uid")
            val web= WebsiteData(
                null,
                web_address,
                name,
                login,
                Password,
                comment,
            )
            firestore.collection(uid)
                .document("Data")
                .collection("Website")
                .add(web)
                .addOnSuccessListener {documentref->
                    val newdocid=documentref.id
                    web.id=newdocid
                    Toast.makeText(requireContext(),"Details added successfully",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(),"Something went wrong ${e.message}",Toast.LENGTH_SHORT).show()
                }
        }
    }
}