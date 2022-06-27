package com.team404.foodtrack.ui.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import com.team404.foodtrack.R
import com.team404.foodtrack.configuration.FoodTrackDB
import com.team404.foodtrack.data.Market
import com.team404.foodtrack.data.database.MarketFavorites
import com.team404.foodtrack.databinding.FragmentMapsBinding
import com.team404.foodtrack.databinding.FragmentMarketListBinding
import com.team404.foodtrack.databinding.ItemMarketMapBinding
import com.team404.foodtrack.domain.adapters.CouponCategoryAdapter
import com.team404.foodtrack.domain.mappers.MarketFavoritesMapper
import com.team404.foodtrack.domain.repositories.MarketFavoritesRepository
import com.team404.foodtrack.domain.repositories.MarketRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject


@SuppressLint("MissingPermission")
class MapsFragment : Fragment() {

    private val marketRepository : MarketRepository by inject()
    private var _marketItemBinding: ItemMarketMapBinding? = null
    private var markerTags : MutableMap<Marker?, Market> = mutableMapOf()
    private lateinit var room: FoodTrackDB
    private val marketFavoritesMapper: MarketFavoritesMapper = MarketFavoritesMapper()
    private lateinit var categoryAdapter: CouponCategoryAdapter
    private lateinit var map: GoogleMap
    private val marketItemBinding get() = _marketItemBinding!!
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

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
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
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

        googleMap.setOnMarkerClickListener { marker ->
            _marketItemBinding = ItemMarketMapBinding.inflate(layoutInflater)

            val dialog = context?.let { it1 -> BottomSheetDialog(it1) }
            val market: Market = markerTags[marker]!!

            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

            dialog?.let {
                marketItemBinding.marketName.text = market?.name
                marketItemBinding.marketAddress.text = "${market.address?.street} ${market.address?.number}, ${market.address?.city}"

                if (market?.marketImg != null) {
                    Picasso.get()
                        .load(market.marketImg)
                        .placeholder(R.drawable.ic_market)
                        .error(R.drawable.ic_no_image)
                        .into(marketItemBinding.imgMarket)
                }

                initializeFavoriteMarket(market, marketItemBinding.isFavorite)

                marketItemBinding.isFavorite.setOnClickListener{
                    changeFavoriteMarket(market, marketItemBinding.isFavorite)
                }

                marketItemBinding.viewLocalDetailBtn.setOnClickListener {
                    val bundle = Bundle()
                    market.id?.let { bundle.putLong("marketId", market.id) }
                    dialog.dismiss()
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_mapsFragment_to_marketFragment, bundle)
                }

                categoryAdapter = CouponCategoryAdapter()
                marketItemBinding.rvMarketCategories.layoutManager =  GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
                marketItemBinding.rvMarketCategories.adapter = categoryAdapter

                categoryAdapter.updateCouponCategories(market?.type)
                categoryAdapter.notifyDataSetChanged()

                dialog.setCancelable(true)
                dialog.setContentView(marketItemBinding.root)

                dialog.setOnCancelListener {
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                }
                dialog.show()
            }

            true
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        room = FoodTrackDB.getDatabase(requireContext())

        val root: View = binding.root

        return root
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

    private fun initializeFavoriteMarket(market: Market, imgView: ImageView) {
        var isFavorite = false

        market.id?.let {
            CoroutineScope(Dispatchers.Default).launch {
                val marketFavoritesRepository = MarketFavoritesRepository(room.marketFavoritesDao())
                var marketFavorite: MarketFavorites? = marketFavoritesRepository.searchByMarketId(it)

                withContext(Dispatchers.Main) {
                    imgView.setImageResource(
                        if (marketFavorite != null) R.drawable.ic_filled_heart else R.drawable.ic_empty_heart
                    )
                }
            }
        }
    }

    private fun changeFavoriteMarket(market: Market, imgView: ImageView) {
        var isFavorite = false

        market.id?.let {
            CoroutineScope(Dispatchers.Default).launch {
                val marketFavoritesRepository = MarketFavoritesRepository(room.marketFavoritesDao())
                var marketFavorite: MarketFavorites? = marketFavoritesRepository.searchByMarketId(it)

                if (marketFavorite != null) {
                    marketFavoritesRepository.delete(marketFavorite)
                } else {
                    marketFavorite = marketFavoritesMapper.map(market)
                    marketFavoritesRepository.insert(marketFavorite)
                    isFavorite = true
                }

                withContext(Dispatchers.Main) {
                    imgView.setImageResource(
                        if (isFavorite == true) R.drawable.ic_filled_heart else R.drawable.ic_empty_heart
                    )
                }
            }
        }
    }
}