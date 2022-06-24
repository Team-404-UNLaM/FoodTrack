package com.team404.foodtrack.ui.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.team404.foodtrack.R
import com.team404.foodtrack.data.Market
import com.team404.foodtrack.databinding.ItemMarketMapBinding
import com.team404.foodtrack.domain.repositories.MarketRepository
import org.koin.android.ext.android.inject


@SuppressLint("MissingPermission")
class MapsFragment : Fragment() {

    private val marketRepository : MarketRepository by inject()
    private var _marketItemBinding: ItemMarketMapBinding? = null
    private var markerTags : MutableMap<Marker?, Market> = mutableMapOf()
    private lateinit var map: GoogleMap
    private val marketItemBinding get() = _marketItemBinding!!

    companion object {
        const val REQUEST_CODE_LOCATION = 2008
    }

    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))

        val markets = marketRepository.search()

        markets.forEach { market ->
            val marketLocation = LatLng(market.address!!.latitude, market.address.longitude)

            val marker = googleMap.addMarker(MarkerOptions()
                .position(marketLocation)
                .title(market.name)
                .snippet("${market.address.street} ${market.address.number}")
            )

            markerTags[marker] = market
        }

        map = googleMap
        enableLocation()

        if (map.isMyLocationEnabled) {
            googleMap.isMyLocationEnabled = true
        } else {
            val currentLocation = LatLng(-34.670722304934316, -58.5628481153441)
            googleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(currentLocation, 18f),
                3000,
                null
            )
        }

        googleMap.setOnInfoWindowClickListener { marker ->
            val dialog = context?.let { it1 -> BottomSheetDialog(it1) }
            val market = markerTags[marker]

            dialog?.let {
                //TODO Completar en base a la visual
                marketItemBinding.txtTitle.text = market?.name
                marketItemBinding.txtTag1.text = "Restaurant"
                marketItemBinding.txtTag2.text = "Bar"

                dialog.setCancelable(true)
                dialog.setContentView(marketItemBinding.root)
                dialog.show()
            }
            Toast.makeText(requireContext(), "Clicked location is ${marker.id}", Toast.LENGTH_SHORT)
                .show()


        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _marketItemBinding = ItemMarketMapBinding.inflate(layoutInflater)
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun isLocationPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun enableLocation() {
        if(!::map.isInitialized) return
        if(isLocationPermissionGranted()) {
            map.isMyLocationEnabled = true
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(requireContext(), "Ve a ajustes y acepta los permisos", Toast.LENGTH_LONG).show()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            REQUEST_CODE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    map.isMyLocationEnabled = true
                } else {
                    Toast.makeText(requireContext(), "Para activar la localizacion ve a ajustes y acepta los permisos", Toast.LENGTH_LONG).show()
                }
            }
            else -> {}
        }
    }

    override fun onResume() {
        super.onResume()
        if(!::map.isInitialized) return
        if(!isLocationPermissionGranted()) {
            map.isMyLocationEnabled = false
            Toast.makeText(requireContext(), "Para activar la localizacion ve a ajustes y acepta los permisos", Toast.LENGTH_LONG).show()
        }
    }
}