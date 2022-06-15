package com.team404.foodtrack.domain.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import androidx.recyclerview.widget.RecyclerView
import com.team404.foodtrack.R
import com.team404.foodtrack.configuration.FoodTrackDB
import com.team404.foodtrack.data.Market
import com.team404.foodtrack.databinding.GridLayoutMarketBinding
import com.team404.foodtrack.domain.repositories.MarketFavoritesRepository
import com.team404.foodtrack.utils.SnackbarBuilder
import com.team404.foodtrack.domain.holders.MarketViewHolder
import kotlinx.coroutines.*

class MarketAdapter (private val viewClickListener: (Market) -> Unit, private val favoriteClickListener: (Market) -> Unit) : RecyclerView.Adapter<MarketViewHolder>() {

    private lateinit var room: FoodTrackDB
    private val marketList = mutableListOf<Market>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        room = FoodTrackDB.getDatabase(parent.context)

        val marketBinding = GridLayoutMarketBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MarketViewHolder(marketBinding)
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        val market = marketList[position]

        holder.binding.marketName.text = market.name
        holder.binding.textStars.text = market.stars.toString()

        if (market.marketImg != null) {
            Picasso.get()
                .load(market.marketImg)
                .placeholder(R.drawable.ic_market)
                .error(R.drawable.ic_no_image)
                .into(holder.binding.marketImg)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val marketFavoritesList = MarketFavoritesRepository(room.marketFavoritesDao()).search()

            if(marketFavoritesList != null) {
                withContext(Dispatchers.Main) {
                    val favoriteMarket = marketFavoritesList.find { favorite -> market.id == favorite.marketId}

                    if (favoriteMarket != null) {
                        holder.binding.isFavorite.setImageResource(R.drawable.ic_filled_heart)
                    } else {
                        holder.binding.isFavorite.setImageResource(R.drawable.ic_empty_heart)
                    }
                }
            } else {
                SnackbarBuilder.showErrorMessage(holder.binding.root)
            }
        }

        holder.binding.marketCardSection.setOnClickListener { viewClickListener(market) }
        holder.binding.isFavorite.setOnClickListener { favoriteClickListener(market) }
    }

    override fun getItemCount(): Int {
        return marketList.size
    }

    fun updateMarketList(results: List<Market>?) {
        marketList.clear()
        if(results != null) {
            marketList.addAll(results)
        }
    }

}