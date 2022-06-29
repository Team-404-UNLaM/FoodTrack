package com.team404.foodtrack.ui.market

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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import com.team404.foodtrack.R
import com.team404.foodtrack.configuration.FoodTrackDB
import com.team404.foodtrack.data.MarketData
import com.team404.foodtrack.data.Order
import com.team404.foodtrack.databinding.FragmentMarketBinding
import com.team404.foodtrack.domain.factories.MarketViewModelFactory
import com.team404.foodtrack.domain.repositories.MarketFavoritesRepository
import com.team404.foodtrack.domain.repositories.MarketRepository
import org.koin.android.ext.android.inject

@SuppressLint("MissingPermission")
class MarketFragment : Fragment() {

    private var _binding: FragmentMarketBinding? = null
    private val binding get() = _binding!!
    private lateinit var factory: MarketViewModelFactory
    private lateinit var viewModel: MarketViewModel
    private val marketRepository : MarketRepository by inject()
    private var marketId: Long = 0L
    private lateinit var room: FoodTrackDB
    private lateinit var map: GoogleMap

    companion object {
        const val REQUEST_CODE_LOCATION = 2008
    }

    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))

        val market = marketRepository.searchById(marketId)
        val currentLocation = LatLng(market.address!!.latitude, market.address!!.longitude)

        googleMap.addMarker(MarkerOptions()
            .position(currentLocation)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        )

        map = googleMap
        enableLocation()

        if (map.isMyLocationEnabled) {
            googleMap.isMyLocationEnabled = true
        }

        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(currentLocation, 14f)
        )

    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        _binding = FragmentMarketBinding.inflate(inflater, container, false)
        room = FoodTrackDB.getDatabase(requireContext())
        val root: View = binding.root
        val marketFavoritesRepository = MarketFavoritesRepository(room.marketFavoritesDao())
        marketId = arguments?.getLong("marketId")!!

        factory = MarketViewModelFactory(marketRepository, marketFavoritesRepository)
        viewModel = ViewModelProvider(this, factory).get(MarketViewModel::class.java)

        if (marketId != null) {
            setUpListeners(root, marketId)
            setUpViewData(root, marketId)
            setUpObserver(root)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.market_map_location) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun setUpListeners(root: View, marketId: Long) {
        isFavoriteListener(marketId)
    }

    fun setUpViewData(view: View, marketId: Long) {
        viewModel.getMarketData(view, marketId)
    }

    private fun setUpObserver(root: View) {
        viewModel.marketData.observe(viewLifecycleOwner, { marketData ->
            setUpMarketView(marketData)
            goToMakeOrderListener(root, marketData)
            goToMenuListener(root, marketData)
        })
    }

    fun setUpMarketView(marketData: MarketData) {
        if (marketData.market != null) {
            val market = marketData.market
            binding.marketName.text = market.name?.uppercase() ?: "NO NAME"
            binding.textStars.text = market.stars.toString()
            binding.marketStreetLocationTxt.text = "${market.address?.street} ${market.address?.number}, ${market.address?.city}"

            if (market.marketImg != null) {
                Picasso.get()
                    .load(market.marketImg)
                    .placeholder(R.drawable.ic_market)
                    .error(R.drawable.ic_no_image)
                    .into(binding.imgMarket)
            }

            if (marketData.isFavorite) {
                binding.isFavorite.setImageResource(R.drawable.ic_filled_heart)
            } else {
                binding.isFavorite.setImageResource(R.drawable.ic_empty_heart)
            }
        }
    }

    private fun goToMenuListener(root: View, marketData: MarketData) {
        binding.btnGoToMenu.setOnClickListener {
            val bundle = Bundle()
            bundle.putLong("marketId", marketData.market!!.id!!)
            bundle.putString("marketName", marketData.market.name)
            bundle.putString("marketImg", marketData.market.marketImg)
            Navigation.findNavController(root)
                .navigate(R.id.action_marketFragment_to_menuFragment, bundle)
        }
    }

    private fun goToMakeOrderListener(root: View, marketData: MarketData) {
        binding.btnGoToMakeOrder.setOnClickListener {
            val order = Order.Builder().marketId(marketData.market?.id!!)
            val bundle = Bundle()
            bundle.putString("order", GsonBuilder().create().toJson(order))
            Navigation.findNavController(root)
                .navigate(R.id.action_marketFragment_to_selectConsumptionModeFragment, bundle)
        }
    }

    private fun isFavoriteListener(marketId: Long) {
        binding.isFavorite.setOnClickListener {
            viewModel.changeMarketFavorite()
        }

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
