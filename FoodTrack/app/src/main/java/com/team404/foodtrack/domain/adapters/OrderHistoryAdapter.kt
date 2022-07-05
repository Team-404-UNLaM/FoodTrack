package com.team404.foodtrack.domain.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import androidx.recyclerview.widget.RecyclerView
import com.team404.foodtrack.R
import com.team404.foodtrack.data.OrderHistory
import com.team404.foodtrack.databinding.GridLayoutOrderHistoryBinding
import com.team404.foodtrack.domain.holders.OrderHistoryViewHolder
import com.team404.foodtrack.utils.DateAndTime
import java.time.Month

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
        holder.binding.orderDate.text = convertOrderDate(orderHistory.order.date!!)
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

    private fun convertOrderDate(date: String): String {
        val dateBuilder = StringBuilder()
        val dateSplitted = date.split('/')

        dateBuilder.append(dateSplitted[0])
        dateBuilder.append(" de ")
        dateBuilder.append(getMonth(dateSplitted[1]))

        if (dateSplitted.size == 3) {
            if (dateSplitted[2] != DateAndTime.getCurrentYear()) {
                dateBuilder.append(" del ")
                dateBuilder.append(dateSplitted[2])
            }

        }

        return  dateBuilder.toString()
    }

    private fun getMonth(month: String): String {
        return when(month) {
            "01" -> "enero"
            "02" -> "febrero"
            "03" -> "marzo"
            "04" -> "abril"
            "05" -> "mayo"
            "06" -> "junio"
            "07" -> "julio"
            "08" -> "agosto"
            "09" -> "septiembre"
            "10" -> "octubre"
            "11" -> "noviembre"
            "12" -> "diciembre"
            else -> "algun mes"
        }
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