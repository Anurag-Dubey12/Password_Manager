package com.example.passwordmanager.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.passwordmanager.Data_Class.AddressData
import com.example.passwordmanager.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Address : DialogFragment() {
    val TAG="Address_Dialog"
    private lateinit var toolbar: MaterialToolbar
    private lateinit var nameValue: TextInputEditText
    private lateinit var addressRegionField: TextInputEditText
    private lateinit var addressStateField: TextInputEditText
    private lateinit var addressCityField: TextInputEditText
    private lateinit var addressPostalField: TextInputEditText
    private lateinit var addressField: TextInputEditText
    private lateinit var addressFirstNameField: TextInputEditText
    private lateinit var addressMiddleNameField: TextInputEditText
    private lateinit var addressLastNameField: TextInputEditText
    private lateinit var addressNumberField: TextInputEditText
    private lateinit var addressEmailField: TextInputEditText
    fun display(fragmentManager: FragmentManager):Address?{
        val address=Address()
        if(fragmentManager!=null){
            address.show(fragmentManager,TAG)
        }
        return address
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL,com.example.passwordmanager.R.style.AppTheme_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_address, container, false)
        toolbar=view.findViewById(R.id.appBarLayout)
        nameValue = view.findViewById(R.id.Name_value)
        addressRegionField = view.findViewById(R.id.Address_Region_field)
        addressStateField = view.findViewById(R.id.Address_State_field)
        addressCityField = view.findViewById(R.id.Address_City_field)
        addressPostalField = view.findViewById(R.id.Address_Postal_field)
        addressField = view.findViewById(R.id.Address_field)
        addressFirstNameField = view.findViewById(R.id.Address_firstName_field)
        addressMiddleNameField = view.findViewById(R.id.Address_Middlename_field)
        addressLastNameField = view.findViewById(R.id.Address_LastName_field)
        addressNumberField = view.findViewById(R.id.Address_Number_field)
        addressEmailField = view.findViewById(R.id.Address_Email_field)
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
        toolbar.setOnMenuItemClickListener(){menuitem->
            when(menuitem.itemId){
                R.id.action_save->{
                    var name = nameValue.text?.toString()!!
                    var region = addressRegionField.text?.toString()!!
                    var state = addressStateField.text?.toString()!!
                    var city = addressCityField.text?.toString()!!
                    var postalCode = addressPostalField.text?.toString()!!
                    var address = addressField.text?.toString()!!
                    var firstName = addressFirstNameField.text?.toString()!!
                    var middleName = addressMiddleNameField.text?.toString()!!
                    var lastName = addressLastNameField.text?.toString()!!
                    var phoneNumber = addressNumberField.text?.toString()!!
                    var emailAddress = addressEmailField.text?.toString()!!
                    SaveData(name,region,state,city,postalCode,address,firstName,middleName,lastName,phoneNumber,emailAddress)
                    dismiss()
                }
            }
            true
        }
    }
    fun SaveData( name: String, region: String, state: String, city: String, postalCode: String, address: String,
                     firstName: String, middleName: String, lastName: String, phoneNumber: String, emailAddress: String){
        val firestore=FirebaseFirestore.getInstance()
        val uid=FirebaseAuth.getInstance().uid.toString()
        val address_data=AddressData(null,name,region,state,city,postalCode, address, firstName, middleName, lastName, phoneNumber,emailAddress)
        firestore.collection(uid)
            .document("User_Data")
            .collection("Address")
            .add(address_data)
            .addOnSuccessListener {documentref->
                val newdocid=documentref.id
                address_data.uid=newdocid
                Toast.makeText(requireContext(),"Address Details added successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(),"Something went wrong ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }
}