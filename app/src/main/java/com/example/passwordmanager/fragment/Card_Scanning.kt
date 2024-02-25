package com.example.passwordmanager.fragment

import ai.cardscan.insurance.CardScanActivity
import ai.cardscan.insurance.CardScanActivityResult
import ai.cardscan.insurance.CardScanActivityResultHandler
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.OptIn
import androidx.camera.core.CameraProvider
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.example.passwordmanager.Data_Class.CardDetails
import com.example.passwordmanager.ExtractDataUseCase
import com.example.passwordmanager.R
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import takePicture
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class Card_Scanning : DialogFragment(), CardScanActivityResultHandler {
    private val CAMERA_PERMISSION = 7762
    lateinit var cardScanResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var rootLayout: LinearLayout
    private lateinit var previewView: PreviewView
    private lateinit var scanButton: Button
    private lateinit var ownerTextView: TextView
    private lateinit var numberTextView: TextView
    private lateinit var dateTextView: TextView
    private val useCase = ExtractDataUseCase(TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS))

    val TAG = "Card_Scanning"

    fun display(fragmentManager: FragmentManager): Card_Scanning {
        val card_scanning = Card_Scanning()
        if (fragmentManager != null) {
            card_scanning.show(fragmentManager, TAG)
        }
        return card_scanning
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_card__scanning, container, false)
        rootLayout = view.findViewById(R.id.root)
        previewView = view.findViewById(R.id.previewView)
        scanButton = view.findViewById(R.id.button)
        ownerTextView = view.findViewById(R.id.owner)
        numberTextView = view.findViewById(R.id.number)
        dateTextView = view.findViewById(R.id.date)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        dialog?.let {
            val width = ViewGroup.LayoutParams.FILL_PARENT
            val height = ViewGroup.LayoutParams.FILL_PARENT
            it.window?.setLayout(width, height)
            it.window?.attributes?.windowAnimations = R.style.AppTheme_Slide
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scanButton.setOnClickListener {

        }
    }
//    private fun startCardScanActivity() {
//        val intent = Intent(requireContext(), CardScanActivity::class.java)
//        startActivityForResult(intent, REQUEST_CARD_SCAN)
//    }
//
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
//            val cardResult = data?.getSerializableExtra(CardScanActivity.CARD_RESULT_EXTRA) as? CardScanActivityResult
//            cardResult?.let { scanSuccess(it) }
        }
    }

    override fun scanSuccess(card: CardScanActivityResult) {

    }

}