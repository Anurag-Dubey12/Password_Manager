package com.example.passwordmanager.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.passwordmanager.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import java.io.IOException
import java.io.InputStream
import java.util.Date

class Document : DialogFragment() {
    private lateinit var toolbar:MaterialToolbar
    private lateinit var selected_img:ImageView
    private lateinit var docname:TextInputEditText
    private lateinit var submitdoc:MaterialButton
    private lateinit var ImageFileName:TextView
    private lateinit var doc_comment:TextInputEditText
    private lateinit var byteArrayvalue: ByteArray
    private val uid=FirebaseAuth.getInstance().currentUser?.uid
    val TAG="Document_Dialog"
    fun display(fragmentManager: FragmentManager):Document{
        val doc=Document()
        if(fragmentManager!=null){
            doc.show(fragmentManager,TAG)
        }
        return doc
    }
    private val storage = FirebaseStorage.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_document, container, false)
        toolbar=view.findViewById(R.id.appBarLayout)
        docname=view.findViewById(R.id.Doc_file_Name)
        submitdoc=view.findViewById(R.id.submitdoc)
        ImageFileName=view.findViewById(R.id.filename)
        doc_comment=view.findViewById(R.id.doc_comment)
        selected_img=view.findViewById(R.id.selected_image)
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
            it.window?.attributes?.windowAnimations= com.example.passwordmanager.R.style.AppTheme_Slide
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checksdk(requireContext()) {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            fileAccess.launch(intent)
        }
        submitdoc.setOnClickListener {
            val name=docname.text.toString()
            val comment=doc_comment.text.toString()
            uploadFile(byteArrayvalue, name, comment, uid.toString())
            dismiss()
        }
        selected_img.setOnClickListener {
            checksdk(requireContext()) {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "*/*"
                fileAccess.launch(intent)
            }
        }
        toolbar.setNavigationOnClickListener { dismiss() }
//        toolbar.setOnMenuItemClickListener {
//            dismiss()
//            true
//        }
    }
    fun checksdk(context: Context,call:() -> Unit){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if(ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)
                ==PackageManager.PERMISSION_GRANTED){
                call.invoke()
            }else{
                requestPermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                dismiss()
            }
        }
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            it?.let {
                if (it) {
                    Toast.makeText(requireContext(),"permission granted",Toast.LENGTH_SHORT).show()
                }
            }
        }
    private val fileAccess =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result->
            if(result.resultCode==Activity.RESULT_OK){
                result?.data?.data?.let {uri->
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    inputStream?.readBytes()?.let { byteArray ->
                        selected_img.setImageURI(uri)
                        byteArrayvalue=byteArray
                        val fileName = getFileName(uri)
                        ImageFileName.setText("FileName: $fileName")
                        Log.d("byte","The byte array value is $byteArrayvalue")
                    }
                }

            }else{
                dismiss()
            }
        }

    fun uploadFile(byteArray: ByteArray, fileName: String, comment: String,uid:String) {
        val storageRef = storage.reference
        val storageRef2 = storageRef.child("files/$fileName")

        val metadata = StorageMetadata.Builder()
            .setCustomMetadata("comment", comment)
            .setCustomMetadata("name", fileName)
            .setCustomMetadata("uid",uid)
            .build()
        storageRef2.putBytes(byteArray, metadata)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "File uploaded successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to upload file", Toast.LENGTH_SHORT).show()
            }
            .addOnCompleteListener {
            }
    }
    @SuppressLint("Range")
    private fun getFileName(uri: Uri): String? {
        var filename: String? = null
        if (uri.scheme == "content") {
            val cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    filename = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        return filename
    }

}
