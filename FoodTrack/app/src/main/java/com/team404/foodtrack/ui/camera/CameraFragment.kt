package com.team404.foodtrack.ui.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.team404.foodtrack.R
import com.team404.foodtrack.databinding.FragmentCameraBinding
import com.team404.foodtrack.utils.FlashImplementation
import com.team404.foodtrack.utils.VibratorImplementation
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraFragment : Fragment() {
    private  var _binding: FragmentCameraBinding? = null
    private var previewView: PreviewView? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var cameraSelector: CameraSelector? = null
    private var previewUseCase: Preview? = null
    private var qrAnalysisUseCase: ImageAnalysis? = null
    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private lateinit var cameraContext: Context

    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        cameraContext = context
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                startCamera()
            } else {
                Toast.makeText(
                    cameraContext,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_LONG).show()
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater,container,false)
        val root : View = binding.root
        getViews()
        showCameraPreview()
        return root
    }
    private fun getViews() {
        previewView = binding.viewFinder
    }


    private fun showCameraPreview(){
        if (ContextCompat.checkSelfPermission(cameraContext, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else{
            requestCameraPermission()
        }
    }

    private fun requestCameraPermission() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }


    private fun startCamera() {
        //Set camera selector
        cameraSelector = CameraSelector
            .Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        val cameraProviderFuture = ProcessCameraProvider.getInstance(cameraContext)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()


            //Bind camera use cases
            bindCameraUseCases()

        }, ContextCompat.getMainExecutor(cameraContext))
    }
    private fun bindCameraUseCases() {
        bindPreviewUseCase()
        bindAnalyseUseCase()
    }
    private fun bindPreviewUseCase() {
        if (cameraProvider == null) {
            return
        }
        if (cameraProvider != null) {
            cameraProvider!!.unbind(previewUseCase)
        }
        previewUseCase = Preview.Builder().setTargetRotation(previewView!!.display.rotation).build()
        previewUseCase!!.setSurfaceProvider(previewView!!.surfaceProvider)

        try {
            val camera = cameraProvider!!.bindToLifecycle(
                this,
                cameraSelector!!,
                previewUseCase
            )
            FlashImplementation.flashListener(camera, binding.flashOnOffButton)
        } catch (illegalStateException: IllegalStateException) {
            Log.e(TAG, illegalStateException.message ?: "IllegalStateException")
        } catch (illegalArgumentException: IllegalArgumentException) {
            Log.e(TAG, illegalArgumentException.message ?: "IllegalArgumentException")
        }
    }
    @SuppressLint("UnsafeOptInUsageError")
    private fun bindAnalyseUseCase() {
        if (cameraProvider == null) {
            return
        }
        if (cameraProvider != null) {
            cameraProvider!!.unbind(qrAnalysisUseCase)
        }
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE, Barcode.FORMAT_AZTEC)
            .build()
        val barcodeScanner : BarcodeScanner = BarcodeScanning.getClient(options)
        qrAnalysisUseCase = ImageAnalysis
            .Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setTargetRotation(previewView!!.display.rotation)
            .build()
            .also { qrAnalysisUseCase ->
                qrAnalysisUseCase.setAnalyzer(cameraExecutor,
                    ImageAnalysis.Analyzer{ imageProxy : ImageProxy  ->
                        processImageProxy(imageProxy,barcodeScanner)
                    }
                )
            }


        try {
            cameraProvider!!.bindToLifecycle(
                this,
                cameraSelector!!,
                qrAnalysisUseCase
            )
        } catch (illegalStateException: IllegalStateException) {
            Log.e(TAG, illegalStateException.message ?: "IllegalStateException")
        } catch (illegalArgumentException: IllegalArgumentException) {
            Log.e(TAG, illegalArgumentException.message ?: "IllegalArgumentException")
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun processImageProxy(imageProxy: ImageProxy, barcodeScanner: BarcodeScanner) {
        val image = imageProxy.image
        if(image != null){
            val imageInput = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)
            barcodeScanner.process(imageInput)
                .addOnSuccessListener { barcodes ->
                    if(barcodes.size > 0){
                        barcodes.firstOrNull().let { barcode ->
                            val barcodeValue = barcode?.displayValue
                            barcodeValue?.let { result ->
                                processResult(result)
                            }
                        }
                    }
                }
                .addOnFailureListener { exc ->
                    Log.e("EXCEPTION", "${exc.message}")
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }

    private fun processResult(result: String) {
        qrAnalysisUseCase?.clearAnalyzer()
        cameraProvider!!.unbind(qrAnalysisUseCase)
        VibratorImplementation.vibrate(cameraContext)
        binding.qrCodeContentTextView.visibility = View.VISIBLE
        binding.qrCodeContentTextView.text = result
        bindAnalyseUseCase()
    }

    override fun onResume() {
        super.onResume()
        binding.qrCodeContentTextView.visibility = View.GONE
    }
    override fun onPause(){
        super.onPause()
        binding.flashOnOffButton.setImageResource(R.drawable.ic_flash_off)
    }
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraDebug"
    }
}