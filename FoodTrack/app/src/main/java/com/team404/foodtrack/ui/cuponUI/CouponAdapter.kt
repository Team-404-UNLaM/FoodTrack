package com.team404.foodtrack.ui.cuponUI

import android.view.View
import androidx.constraintlayout.helper.widget.Carousel
import com.team404.foodtrack.data.Coupon
import com.team404.foodtrack.databinding.ItemCouponBinding

class CouponAdapter(private val coupons: List<Coupon>) : Carousel.Adapter {

    private lateinit var binding: ItemCouponBinding

    override fun count() = coupons.size

    override fun populate(view: View?, index: Int) {
        binding = ItemCouponBinding.bind(view!!)
        binding.tvTitle.text = coupons[index].name
    }

    override fun onNewItem(index: Int) {
    }


}