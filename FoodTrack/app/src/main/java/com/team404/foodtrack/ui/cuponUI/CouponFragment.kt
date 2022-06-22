package com.team404.foodtrack.ui.cuponUI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.team404.foodtrack.data.Coupon
import com.team404.foodtrack.databinding.FragmentCouponBinding
import com.team404.foodtrack.domain.adapters.CouponAdapter
import com.team404.foodtrack.domain.services.CouponService
import org.koin.android.ext.android.inject
import www.sanju.zoomrecyclerlayout.ZoomRecyclerLayout

class CouponFragment : Fragment() {

    private val couponService: CouponService by inject()
    private var _binding: FragmentCouponBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCouponBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setupCarousel(couponService.searchCoupons())

        return root
    }

    private fun setupCarousel(coupons: List<Coupon>) {
        val adapter = CouponAdapter(coupons)
        binding.rvCoupon.layoutManager = ZoomRecyclerLayout(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvCoupon)
        binding.rvCoupon.isNestedScrollingEnabled = false
        binding.rvCoupon.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}