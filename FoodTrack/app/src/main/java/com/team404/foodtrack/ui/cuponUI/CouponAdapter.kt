package com.team404.foodtrack.ui.cuponUI

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.helper.widget.Carousel
import androidx.recyclerview.widget.RecyclerView
import com.team404.foodtrack.R
import com.team404.foodtrack.data.Coupon
import com.team404.foodtrack.databinding.ItemCouponBinding

class CouponAdapter(private val coupons: List<Coupon>) :
    RecyclerView.Adapter<CouponAdapter.ViewHolderCoupon>() {

    private lateinit var binding: ItemCouponBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCoupon {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_coupon, parent, false)
        return ViewHolderCoupon(view)
    }

    override fun onBindViewHolder(holder: ViewHolderCoupon, position: Int) {
        holder.bind(coupons[position])
    }

    override fun getItemCount() = coupons.size

    class ViewHolderCoupon(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: Coupon) {
            val binding = ItemCouponBinding.bind(view)
            binding.tvTitle.text = item.name
        }


    }

}