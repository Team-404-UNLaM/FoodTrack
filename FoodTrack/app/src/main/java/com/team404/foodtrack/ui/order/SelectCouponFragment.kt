package com.team404.foodtrack.ui.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.team404.foodtrack.R
import com.team404.foodtrack.configuration.FoodTrackDB
import com.team404.foodtrack.data.ConsumptionMode
import com.team404.foodtrack.data.Coupon
import com.team404.foodtrack.data.Order
import com.team404.foodtrack.databinding.FragmentSelectConsumptionModeBinding
import com.team404.foodtrack.databinding.FragmentSelectCouponBinding
import com.team404.foodtrack.domain.adapters.ConsumptionModeAdapter
import com.team404.foodtrack.domain.adapters.SelectableCouponAdapter
import com.team404.foodtrack.domain.repositories.ConsumptionModeRepository
import com.team404.foodtrack.domain.repositories.CouponRepository
import com.team404.foodtrack.domain.services.CouponService
import org.koin.android.ext.android.inject

class SelectCouponFragment : Fragment() {

    private lateinit var selectableCouponAdapter: SelectableCouponAdapter
    private lateinit var room: FoodTrackDB
    private var _binding: FragmentSelectCouponBinding? = null
    private val binding get() = _binding!!
    private val couponService: CouponService by inject()
    private lateinit var filteredCoupons: List<Coupon>

    private lateinit var order: Order.Builder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectCouponBinding.inflate(inflater, container, false)
        val root: View = binding.root

        order = GsonBuilder().create().fromJson(arguments?.getString("order"), Order.Builder::class.java)

        getApplicableCoupons()
        injectDependencies()
        setUpRecyclerView()
        setUpListeners()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getApplicableCoupons() {
        filteredCoupons = couponService.searchCouponsForOrder(order.marketId!!, order.totalPrice!!)
    }

    private fun injectDependencies() {
        room = FoodTrackDB.getDatabase(requireContext())

        val selectCouponClickListener = { coupon: Coupon ->
            order.appliedCouponId(
                if (order.appliedCouponId == coupon.id) 0L else coupon.id!!
            )
            selectableCouponAdapter.notifyDataSetChanged()
        }

        selectableCouponAdapter = SelectableCouponAdapter(selectCouponClickListener)
    }

    private fun setUpRecyclerView() {
        binding.rvCoupon.also {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            it.setHasFixedSize(true)
            it.adapter = selectableCouponAdapter
        }

        selectableCouponAdapter.updateCouponList(filteredCoupons, order.appliedCouponId)
        selectableCouponAdapter.notifyDataSetChanged()
    }

    private fun setUpListeners() {
        binding.btnGoToSelectProducts.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("order", GsonBuilder().create().toJson(order))
            Navigation.findNavController(it).navigate(R.id.action_selectCouponFragment_to_selectOrderProductsFragment, bundle)
        }
        binding.btnGoToSelectPaymentMethod.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("order", GsonBuilder().create().toJson(order))
            Navigation.findNavController(it).navigate(R.id.action_selectCouponFragment_to_selectPaymentMethodFragment, bundle)
        }
    }
}