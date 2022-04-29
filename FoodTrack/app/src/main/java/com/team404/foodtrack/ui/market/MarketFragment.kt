package com.team404.foodtrack.ui.market

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.team404.foodtrack.R
import com.team404.foodtrack.configuration.FoodTrackDB
import com.team404.foodtrack.data.database.MarketFavorites
import com.team404.foodtrack.databinding.FragmentMarketBinding
import com.team404.foodtrack.domain.mappers.MarketFavoritesMapper
import com.team404.foodtrack.domain.repositories.MarketFavoritesRepository
import com.team404.foodtrack.domain.repositories.MarketRepository
import com.team404.foodtrack.utils.SnackbarBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class MarketFragment : Fragment() {

    private var _binding: FragmentMarketBinding? = null
    private val binding get() = _binding!!
    private val marketFavoritesMapper: MarketFavoritesMapper = MarketFavoritesMapper()
    private val marketRepository : MarketRepository by inject()
    private lateinit var room: FoodTrackDB

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        _binding = FragmentMarketBinding.inflate(inflater, container, false)
        room = FoodTrackDB.getDatabase(requireContext())
        val root: View = binding.root
        val marketId = arguments?.getLong("marketId")

        if (marketId != null) {
            setUpListeners(root, marketId)
            setMarketViewData(marketId)
        }

        return root
    }

    private fun setUpListeners(root: View, marketId: Long) {
        goToMakeOrderListener(root, marketId)
        isFavoriteListener(marketId)
    }

    fun setMarketViewData(marketId: Long) {
        val market = marketRepository.searchById(marketId)

        if (market != null) {
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

            fillFavoriteHeartIfApplies(marketId)
        } else {
            SnackbarBuilder.showErrorMessage(binding.root)
        }
    }

    private fun goToMakeOrderListener(root: View, marketId: Long) {
        // binding.btnGoToMakeOrder.setOnClickListener { marketId: Long ->
        // TODO: Uncomment and modify when Order flow exist
            //  *val bundle = Bundle()
            //  market.id?.let { bundle.putLong("marketId", it) }
            //  Navigation.findNavController(root)
        //  .navigate(R.id.action_navigation_marketFragment_to_x, bundle)*/
        // }
    }

    private fun isFavoriteListener(marketId: Long) {
        binding.isFavorite.setOnClickListener {
            val market = marketRepository.searchById(marketId)

            if (market != null) {
                CoroutineScope(Dispatchers.Default).launch {
                    val marketFavoritesRepository = MarketFavoritesRepository(room.marketFavoritesDao())
                    var marketFavorite: MarketFavorites? = marketFavoritesRepository.getByMarketId(marketId)

                    if (marketFavorite != null) {
                        marketFavoritesRepository.delete(marketFavorite)
                    } else {
                        marketFavorite = marketFavoritesMapper.map(market)
                        marketFavoritesRepository.insert(marketFavorite)
                    }

                    fillFavoriteHeartIfApplies(marketId)
                }
            }
        }

    }

    private fun fillFavoriteHeartIfApplies(marketId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            val marketFavoritesList = MarketFavoritesRepository(room.marketFavoritesDao()).search()

            if(marketFavoritesList != null) {
                withContext(Dispatchers.Main) {
                    val favoriteMarket = marketFavoritesList.find { favorite -> marketId == favorite.marketId}

                    if (favoriteMarket != null) {
                        binding.isFavorite.setImageResource(R.drawable.ic_filled_heart)
                    } else {
                        binding.isFavorite.setImageResource(R.drawable.ic_empty_heart)
                    }
                }
            } else {
                SnackbarBuilder.showErrorMessage(binding.root)
            }
        }
    }
}
