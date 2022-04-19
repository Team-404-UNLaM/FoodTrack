package com.team404.poketeam.domain.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import androidx.recyclerview.widget.RecyclerView
import com.team404.foodtrack.R
import com.team404.foodtrack.data.Market
import com.team404.foodtrack.data.database.MarketFavorites
import com.team404.foodtrack.databinding.GridLayoutMarketBinding
import com.team404.poketeam.domain.holders.MarketViewHolder
import kotlinx.coroutines.*

class MarketAdapter (private val viewClickListener: (Market) -> Unit, private val favoriteClickListener: (Market) -> Job) : RecyclerView.Adapter<MarketViewHolder>() {

    private val marketList = mutableListOf<Market>()
    private val marketFavoritesList = mutableListOf<MarketFavorites>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val marketBinding = GridLayoutMarketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MarketViewHolder(marketBinding)
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        val market = marketList[position]

        holder.binding.marketName.text = market.name
        holder.binding.textStars.text = market.stars.toString()


            val favoriteMarket = marketFavoritesList.find { favorite -> market.id == favorite.marketId}

        if (favoriteMarket != null) {
            holder.binding.isFavorite.setImageResource(R.drawable.ic_filled_heart)
        } else {
            holder.binding.isFavorite.setImageResource(R.drawable.ic_empty_heart)
        }

        if (market.marketImg != null) {
            Picasso.get()
                .load(market.marketImg)
                .placeholder(R.drawable.ic_market)
                .error(R.drawable.ic_market)
                .into(holder.binding.marketImg)
        }

        holder.binding.btnViewMarket.setOnClickListener { viewClickListener(market) }
        holder.binding.isFavorite.setOnClickListener { favoriteClickListener(market) }
    }

    override fun getItemCount(): Int {
        return marketList.size
    }

    fun updateMarketList(results: List<Market>?, favorites: List<MarketFavorites>) {
        marketList.clear()
        marketFavoritesList.clear()
        if(results != null) {
            marketList.addAll(results)
            marketFavoritesList.addAll(favorites)
        }
    }

}