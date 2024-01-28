package com.example.passwordmanager.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
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

class Card_Scanning : DialogFragment() {
    private val CAMERA_PERMISSION = 7762
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
    val executor: Executor
        get() = ContextCompat.getMainExecutor(requireContext())
    @OptIn(ExperimentalGetImage::class)
    private fun bindUseCases(cameraProvider: ProcessCameraProvider) {
        val preview = buildPreview()
        val takePicture = buildTakePicture()
        val cameraSelector = buildCameraSelector()

        cameraProvider.bindToLifecycle(this, cameraSelector, preview, takePicture)

        scanButton.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val imageProxy = takePicture.takePicture(executor)
                val cardDetails = useCase(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)
                bindCardDetails(cardDetails)
            }
        }

    }

    private fun buildPreview(): Preview = Preview.Builder()
        .build()
        .apply {
            setSurfaceProvider(previewView.surfaceProvider)
        }

    private fun buildCameraSelector(): CameraSelector = CameraSelector.Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
        .build()

    private fun buildTakePicture(): ImageCapture = ImageCapture.Builder()
        .setTargetRotation(previewView.display.rotation)
        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
        .build()

    @SuppressLint("SetTextI18n")
    private fun bindCardDetails(card: CardDetails) {
        ownerTextView.text = card.owner
        numberTextView.text = card.number
        dateTextView.text = "${card.expirationMonth}/${card.expirationYear}"
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val cameraProvider = getCameraProvider()
                    bindUseCases(cameraProvider!!)
                } catch (e: Exception) {
                    // Handle exceptions
                    e.printStackTrace()
                }
            }
        }
    }

    private suspend fun getCameraProvider(): ProcessCameraProvider =
        suspendCancellableCoroutine { continuation ->
            ProcessCameraProvider.getInstance(requireContext()).apply {
                addListener(Runnable {
                    continuation.resume(get())
                }, ContextCompat.getMainExecutor(requireContext()))
            }
        }

}