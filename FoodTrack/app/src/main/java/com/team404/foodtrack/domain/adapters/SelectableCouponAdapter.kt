package com.team404.foodtrack.domain.adapters

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import androidx.recyclerview.widget.RecyclerView
import com.team404.foodtrack.R
import com.team404.foodtrack.configuration.FoodTrackDB
import com.team404.foodtrack.data.Coupon
import com.team404.foodtrack.data.Market
import com.team404.foodtrack.databinding.GridLayoutMarketBinding
import com.team404.foodtrack.databinding.ItemSelectableCouponBinding
import com.team404.foodtrack.domain.holders.CouponViewHolder
import com.team404.foodtrack.domain.holders.MarketViewHolder
import com.team404.foodtrack.domain.holders.SelectableCouponViewHolder
import com.team404.foodtrack.domain.repositories.MarketFavoritesRepository
import com.team404.foodtrack.utils.SnackbarBuilder
import com.team404.foodtrack.utils.buildCouponValidForMessage
import kotlinx.coroutines.*

class SelectableCouponAdapter(private val viewClickListener: (Coupon) -> Unit) : RecyclerView.Adapter<SelectableCouponViewHolder>() {

    private lateinit var room: FoodTrackDB
    private val couponList = mutableListOf<Coupon>()
    private var selectedCouponId: Long? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectableCouponViewHolder {
        room = FoodTrackDB.getDatabase(parent.context)

        val itemSelectableCouponBinding = ItemSelectableCouponBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SelectableCouponViewHolder(itemSelectableCouponBinding)
    }

    override fun onBindViewHolder(holder: SelectableCouponViewHolder, position: Int) {
        val coupon = couponList[position]

        holder.binding.couponName.text = coupon.name
        holder.binding.discountValue.text = "${ coupon.discount?.times(100)?.toInt() }%"
        holder.binding.validFor.text = buildCouponValidForMessage(coupon)

        val cardColor = if (selectedCouponId != null && coupon.id == selectedCouponId) "#FFFFE9BB" else "#FFFFFFFF"
        val discountColor = if (selectedCouponId != null && coupon.id == selectedCouponId) "#FF1F970A" else "#FFED1C24"

        holder.binding.couponCard.setBackgroundColor(Color.parseColor(cardColor))
        holder.binding.discountSection.background.setColorFilter(Color.parseColor(discountColor), PorterDuff.Mode.SRC_ATOP)

        if (coupon.couponImg != null) {
            Picasso.get()
                .load(coupon.couponImg)
                .placeholder(R.drawable.ic_food)
                .error(R.drawable.ic_no_image)
                .into(holder.binding.couponImg)
        } else {
            holder.binding.couponImg.setImageResource(R.drawable.ic_food)
        }

        holder.binding.couponCardSection.setOnClickListener {
            viewClickListener(coupon)

            selectedCouponId = if (selectedCouponId != null && coupon.id == selectedCouponId) null else coupon.id!!
        }
    }

    override fun getItemCount(): Int {
        return couponList.size
    }

    fun updateCouponList(results: List<Coupon>?, appliedCouponId: Long?) {
        couponList.clear()

        if(results != null) {
            couponList.addAll(results)
            selectedCouponId = appliedCouponId
        }
    }
}