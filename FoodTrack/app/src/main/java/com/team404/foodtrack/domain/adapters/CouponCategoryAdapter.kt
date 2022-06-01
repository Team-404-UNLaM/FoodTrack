package com.team404.foodtrack.domain.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team404.foodtrack.data.MarketTypes
import com.team404.foodtrack.databinding.ItemMarketCategoryBinding
import com.team404.foodtrack.domain.holders.CouponCategoryViewHolder

class CouponCategoryAdapter () : RecyclerView.Adapter<CouponCategoryViewHolder>() {

    private var categoriesList = mutableListOf<MarketTypes>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponCategoryViewHolder {
        val couponCategoryBinding = ItemMarketCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CouponCategoryViewHolder(couponCategoryBinding)
    }

    override fun onBindViewHolder(holder: CouponCategoryViewHolder, position: Int) {

        val category = categoriesList[position]

        holder.binding.categoryName.text = category.name
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    fun updateCouponCategories(results: List<MarketTypes>?) {
        categoriesList.clear()

        if(results != null) {
            categoriesList.addAll(results)
        }
    }

}