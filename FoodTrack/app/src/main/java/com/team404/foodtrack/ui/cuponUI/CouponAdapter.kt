package com.team404.foodtrack.ui.cuponUI

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.helper.widget.Carousel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.team404.foodtrack.R
import com.team404.foodtrack.data.Coupon
import com.team404.foodtrack.databinding.ItemCouponBinding
import com.team404.foodtrack.domain.adapters.CouponCategoryAdapter
import com.team404.foodtrack.domain.holders.CouponViewHolder

class CouponAdapter(private val coupons: List<Coupon>) : RecyclerView.Adapter<CouponViewHolder>() {

    private lateinit var couponCategoryAdapter: CouponCategoryAdapter
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder {
        val view = ItemCouponBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return CouponViewHolder(view)
    }

    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
        val item = coupons[position]

        if (item.tags == null || (item.tags.size == 1 && item.tags[0].name == "ALL") ) {
            holder.binding.couponValidCategoriesSection.visibility = View.INVISIBLE
        } else {
            holder.binding.couponValidCategoriesSection.visibility = View.VISIBLE

            couponCategoryAdapter = CouponCategoryAdapter()
            holder.binding.couponValidCategoriesRecycler.layoutManager =  GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
            holder.binding.couponValidCategoriesRecycler.adapter = couponCategoryAdapter

            couponCategoryAdapter.updateCouponCategories(item.tags)
            couponCategoryAdapter.notifyDataSetChanged()
        }

        holder.binding.couponName.text = item.name?.uppercase()
        holder.binding.couponDescription.text = item.description
        holder.binding.couponDiscountPercent.text = "${ item.discount?.times(100)?.toInt() }%"
        holder.binding.couponValid.text = buildCouponValidForMessage(item)

        if (item.couponImg != null) {
            Picasso.get()
                .load(item.couponImg)
                .placeholder(R.drawable.ic_food)
                .error(R.drawable.ic_no_image)
                .into(holder.binding.couponImg)
        } else {
            holder.binding.couponImg.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int  = coupons.size

    fun buildCouponValidForMessage(coupon: Coupon): String {
        var message = "Cupon válido"

        if (coupon.minimumPrice != null) {
            message = message.plus(" para pedidos mayores a $${coupon.minimumPrice}")
        }

        if (coupon.maximumDiscount != null) {
            message = message.plus(" con un tope de reintegro de $${coupon.minimumPrice}")
        }

        if (coupon.minimumPrice == null && coupon.maximumDiscount == null) {
            message = message.plus(" para cualquier valor de pedido")
        }

        return message
    }

}