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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.passwordmanager.Data_Class.NoteData
import com.example.passwordmanager.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Note : DialogFragment() {
    private val TAG="Note_Dialog"
    private lateinit var toolbar: MaterialToolbar
    private lateinit var Notes_Name: TextInputEditText
    private lateinit var submitnote: MaterialButton
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
        submitnote = view.findViewById(R.id.submitnote)
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
        submitnote.setOnClickListener {
            val name=Notes_Name.text.toString()
            val comment=Notes_text.text.toString()
            savedata(name,comment)
            dismiss()
        }
//        toolbar.setOnMenuItemClickListener {
//            dismiss()
//            true
//        }
    }
    fun savedata(name:String,comment:String){
        val firestore=FirebaseFirestore.getInstance()
        val uid=FirebaseAuth.getInstance().uid.toString()
        if(name.isEmpty()){
            Notes_Name.error="Enter Name "
        }else{
            val note=NoteData(
                null,
                name,
                comment
            )
            firestore.collection(uid)
//                .document(FirebaseAuth.getInstance().uid.toString())
                .document("User_Data")
                .collection("Notes")
                .add(note)
                .addOnSuccessListener {docref->
                    val newdocid=docref.id
                    note.uid=newdocid
//                    Toast.makeText(requireContext(),"Notes Details added successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(),"Something went wrong ${e.message}",Toast.LENGTH_SHORT).show()
                }
        }
    }

}