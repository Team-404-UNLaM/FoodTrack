package com.team404.foodtrack.domain.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.toColor
import com.squareup.picasso.Picasso
import androidx.recyclerview.widget.RecyclerView
import com.team404.foodtrack.R
import com.team404.foodtrack.data.ConsumptionMode
import com.team404.foodtrack.data.Market
import com.team404.foodtrack.databinding.ItemConsumptionModeBinding
import org.koin.core.component.getScopeId
import org.koin.core.component.getScopeName
import kotlin.math.absoluteValue

class ConsumptionModeAdapter (private val selectConsumptionModeClickListener: (ConsumptionMode) -> Unit) :
    RecyclerView.Adapter<ConsumptionModeAdapter.ConsumptionModeViewHolder>() {

    private val consumptionModeList = mutableListOf<ConsumptionMode>()
    private var selectedConsumptionModeId: Long? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsumptionModeViewHolder {
        val consumptionModeBinding = ItemConsumptionModeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ConsumptionModeViewHolder(consumptionModeBinding)
    }

    override fun onBindViewHolder(holder: ConsumptionModeViewHolder, position: Int) {
        val consumptionMode = consumptionModeList[position]

        holder.binding.consumptionModeName.text = consumptionMode.name

        val cardColor = if (selectedConsumptionModeId != null && consumptionMode.id == selectedConsumptionModeId) "#FFFFE9BB" else "#FFFFFFFF"

        holder.binding.consumptionModeArea.setBackgroundColor(Color.parseColor(cardColor))

        if (consumptionMode.consumptionModeImg != null) {
            Picasso.get()
                .load(consumptionMode.consumptionModeImg)
                .placeholder(R.drawable.ic_market)
                .error(R.drawable.ic_no_image)
                .into(holder.binding.consumptionModeImg)
        } else {
            holder.binding.consumptionModeImg.setImageResource(R.drawable.ic_market)
        }

        holder.binding.consumptionModeCard.setOnClickListener {
            selectConsumptionModeClickListener(consumptionMode)

            selectedConsumptionModeId = if (selectedConsumptionModeId != null && consumptionMode.id == selectedConsumptionModeId) 0 else consumptionMode.id!!
        }
    }

    override fun getItemCount(): Int {
        return consumptionModeList.size
    }

    fun updateConsumptionModeList(results: List<ConsumptionMode>?, consumptionModeId: Long?) {
        consumptionModeList.clear()
        if(results != null) {
            consumptionModeList.addAll(results)
            selectedConsumptionModeId = consumptionModeId
        }
    }

    class ConsumptionModeViewHolder (val binding: ItemConsumptionModeBinding) : RecyclerView.ViewHolder(binding.root)

}