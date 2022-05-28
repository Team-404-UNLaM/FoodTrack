package com.team404.foodtrack.ui.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.team404.foodtrack.R
import com.team404.foodtrack.data.MarketData
import com.team404.foodtrack.databinding.FragmentCameraBinding
import com.team404.foodtrack.domain.factories.CameraViewModelFactory
import com.team404.foodtrack.domain.repositories.MarketRepository
import com.team404.foodtrack.utils.FlashImplementation
import com.team404.foodtrack.utils.VibratorImplementation
import org.koin.android.ext.android.inject
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraFragment : Fragment() {
    private  var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private var cameraProvider: ProcessCameraProvider? = null
    private var cameraSelector: CameraSelector? = null
    private var previewUseCase: Preview? = null
    private var qrAnalysisUseCase: ImageAnalysis? = null
    private lateinit var cameraExecutor: ExecutorService
    private var camera: Camera? = null
    private lateinit var viewModel : CameraViewModel
    private lateinit var factory: CameraViewModelFactory
    private val marketRepository : MarketRepository by inject()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                startCamera()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permissions not granted by the user.",
                    Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory = CameraViewModelFactory(marketRepository)
        viewModel = ViewModelProvider(this, factory).get(CameraViewModel::class.java)

        // Initialize our background executor
        cameraExecutor = Executors.newSingleThreadExecutor()

        binding.viewFinder.post {
            showCameraPreview()
            setUpObserver(binding.root)
        }
    }


    private fun showCameraPreview(){
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
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

        //Set camera provider
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()


            //Bind camera use cases
            bindCameraUseCases()

        }, ContextCompat.getMainExecutor(requireContext()))
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

        previewUseCase = Preview.Builder()
            .setTargetRotation(binding.viewFinder.display.rotation).build()

        try {
            camera = cameraProvider!!.bindToLifecycle(
                viewLifecycleOwner,
                cameraSelector!!,
                previewUseCase
            ).also{ camera ->
                FlashImplementation.flashListener(camera, binding.flashOnOffButton)
            }
            previewUseCase?.setSurfaceProvider(binding.viewFinder.surfaceProvider)
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
            .setTargetRotation(binding.viewFinder.display.rotation)
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
                viewLifecycleOwner,
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
        VibratorImplementation.vibrate(requireContext())
        setUpMarketId(result)
        bindAnalyseUseCase()
    }

    private fun setUpMarketId(result: String) {
        try{
            val marketId = result.toLong()
            viewModel.getMarketData(binding.root,marketId)
        }catch(e:NumberFormatException){
            Snackbar.make(binding.root, "QR no asociado a Food Track", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setUpObserver(root: View) {
        viewModel.marketData.observe(viewLifecycleOwner,{marketData ->
            goToMarketMenu(root,marketData)
        })
    }

    private fun goToMarketMenu(root: View, marketData: MarketData) {
        val bundle = Bundle()
        bundle.putLong("marketId", marketData.market!!.id!!)
        bundle.putString("marketName", marketData.market.name)
        bundle.putString("marketImg", marketData.market.marketImg)
        Navigation.findNavController(root)
            .navigate(R.id.action_nav_qr_scanner_to_menuFragment, bundle)
    }

    override fun onResume() {
        super.onResume()
       requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }
    override fun onPause(){
        super.onPause()
        binding.flashOnOffButton.setImageResource(R.drawable.ic_flash_off)
    }
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraDebug"
    }
}