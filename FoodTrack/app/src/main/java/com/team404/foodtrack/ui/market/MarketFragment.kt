package com.team404.foodtrack.ui.market

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
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

class MarketFragment : Fragment() {

    private var _binding: FragmentMarketBinding? = null
    private val binding get() = _binding!!
    private lateinit var factory: MarketViewModelFactory
    private lateinit var viewModel: MarketViewModel
    private val marketRepository : MarketRepository by inject()
    private lateinit var room: FoodTrackDB

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        _binding = FragmentMarketBinding.inflate(inflater, container, false)
        room = FoodTrackDB.getDatabase(requireContext())
        val root: View = binding.root
        val marketFavoritesRepository = MarketFavoritesRepository(room.marketFavoritesDao())
        val marketId = arguments?.getLong("marketId")

        factory = MarketViewModelFactory(marketRepository, marketFavoritesRepository)
        viewModel = ViewModelProvider(this, factory).get(MarketViewModel::class.java)

        if (marketId != null) {
            setUpListeners(root, marketId)
            setUpViewData(root, marketId)
            setUpObserver(root)
        }

        return root
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
                .navigate(R.id.action_marketFragment_to_selectOrderProductsFragment, bundle)
        }
    }

    private fun isFavoriteListener(marketId: Long) {
        binding.isFavorite.setOnClickListener {
            viewModel.changeMarketFavorite()
        }

    }
}
