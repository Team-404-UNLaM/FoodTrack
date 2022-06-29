package com.team404.foodtrack.domain.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import androidx.recyclerview.widget.RecyclerView
import com.team404.foodtrack.R
import com.team404.foodtrack.data.OrderHistory
import com.team404.foodtrack.databinding.GridLayoutOrderHistoryBinding
import com.team404.foodtrack.domain.holders.OrderHistoryViewHolder

class OrderHistoryAdapter (private val viewClickListener: (OrderHistory) -> Unit, private val viewMarketClickListener: (OrderHistory) -> Unit) : RecyclerView.Adapter<OrderHistoryViewHolder>() {

    private val TAKE_AWAY = "Take away"
    private val LOCAL_CONSUMPTION = "Consumicion en el local"

    private val orderHistoryList = mutableListOf<OrderHistory>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val orderHistoryBinding = GridLayoutOrderHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return OrderHistoryViewHolder(orderHistoryBinding)
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val orderHistory = orderHistoryList[position]

        holder.binding.marketName.text = orderHistory.market.name
        holder.binding.orderDate.text = orderHistory.order.date
        holder.binding.txtConsumptionMode.text = resolveConsumptionMode(orderHistory)


        if (orderHistory.market.marketImg != null) {
            Picasso.get()
                .load(orderHistory.market.marketImg)
                .placeholder(R.drawable.ic_market)
                .error(R.drawable.ic_no_image)
                .into(holder.binding.marketImg)
        } else {
            Picasso.get()
                .load(R.drawable.ic_market)
                .placeholder(R.drawable.ic_market)
                .noFade()
                .into(holder.binding.marketImg)
        }

        holder.binding.orderCard.setOnClickListener { viewClickListener(orderHistory) }
        holder.binding.txtViewMarket.setOnClickListener { viewMarketClickListener(orderHistory) }
    }

    private fun resolveConsumptionMode(orderHistory: OrderHistory): String {
        if(orderHistory.order.consumptionModeId!! == 111111L)
            return LOCAL_CONSUMPTION
        return TAKE_AWAY
    }

    override fun getItemCount(): Int {
        return orderHistoryList.size
    }

    fun updateOrderHistoryList(results: List<OrderHistory>?) {
        orderHistoryList.clear()
        if(results != null) {
            orderHistoryList.addAll(results)
        }
    }

}