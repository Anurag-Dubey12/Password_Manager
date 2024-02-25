package com.example.passwordmanager.fragment
import android.annotation.SuppressLint
import android.app.Activity
import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.passwordmanager.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
class PhotoCapture: DialogFragment() {
    private lateinit var toolbar: MaterialToolbar
    private lateinit var selected_img: ImageView
    private lateinit var docname: TextInputEditText
    private lateinit var submitdoc: MaterialButton
    private lateinit var photoUri: Uri
    private lateinit var doc_comment: TextInputEditText
    private lateinit var byteArrayvalue: ByteArray
    private val uid= FirebaseAuth.getInstance().currentUser?.uid
    val TAG="Take_A_photo"
    fun display(fragmentManager: FragmentManager):PhotoCapture{
        val doc=PhotoCapture()
        if(fragmentManager!=null){
            doc.show(fragmentManager,TAG)
        }
        return doc
    }
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {isGranted ->
            if (isGranted) {
                openCamera()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
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
            val width= ViewGroup.LayoutParams.FILL_PARENT
            val height= ViewGroup.LayoutParams.FILL_PARENT
            it.window?.setLayout(width,height)
            it.window?.attributes?.windowAnimations= com.example.passwordmanager.R.style.AppTheme_Slide
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissionAndOpenCamera()
        submitdoc.setOnClickListener {
            val name = docname.text.toString()
            val comment = doc_comment.text.toString()
            uploadFile(byteArrayvalue, name, comment, uid.toString())
            dismiss()
        }
        selected_img.setOnClickListener {
            // Capture image
            checkPermissionAndOpenCamera()
        }
        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.title = "Photo"
    }

    private fun checkPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request CAMERA permission
            requestPermission.launch(Manifest.permission.CAMERA)
        } else {
            openCamera()
        }
    }

    private fun openCamera() {
        val photoFile = createImageFile()
        photoFile?.let {
            val photoURI = Uri.fromFile(it)
            photoUri = photoURI
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } ?: Toast.makeText(requireContext(), "Failed to create photo file", Toast.LENGTH_SHORT).show()
    }

    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg ,.png, .jpeg",
            storageDir
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            photoUri?.let { uri ->
                byteArrayvalue = readBytesFromUri(uri)!!
                byteArrayvalue?.let { bytes ->
                    selected_img.setImageURI(uri)
                }
            }
        } else {
            Toast.makeText(requireContext(), "Failed to capture photo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun readBytesFromUri(uri: Uri): ByteArray? {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        return inputStream?.readBytes()
    }

    fun uploadFile(byteArray: ByteArray, fileName: String, comment: String, uid: String) {
        val storageRef = storage.reference
        val storageRef2 = storageRef.child("files/$fileName")

        val metadata = StorageMetadata.Builder()
            .setCustomMetadata("comment", comment)
            .setCustomMetadata("name", fileName)
            .setCustomMetadata("uid", uid)
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
}