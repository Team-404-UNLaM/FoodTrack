package com.team404.foodtrack.ui.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.team404.foodtrack.R
import com.team404.foodtrack.domain.repositories.MarketRepository
import com.team404.foodtrack.mockServer.MockServer

class MapsFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->

        val markets = MarketRepository(MockServer()).search()

        markets.forEach { market ->
            val marketLocation = LatLng(market.address!!.latitude, market.address.longitude)
            googleMap.addMarker(MarkerOptions()
                .position(marketLocation)
                .title(market.name)
                .snippet("${market.address.street} ${market.address.number}")
            )
        }

        val currentLocation = LatLng(-34.670722304934316, -58.5628481153441)
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
        googleMap.addMarker(MarkerOptions()
            .position(currentLocation)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        )
        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(currentLocation, 18f),
            3000,
            null
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

}