package com.team404.foodtrack.ui.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import com.team404.foodtrack.R
import com.team404.foodtrack.configuration.FoodTrackDB
import com.team404.foodtrack.data.Order
import com.team404.foodtrack.data.Product
import com.team404.foodtrack.databinding.FragmentOrderBinding
import com.team404.foodtrack.domain.repositories.*
import com.team404.foodtrack.utils.buildCouponValidForMessage
import org.koin.android.ext.android.inject


class OrderFragment : Fragment() {
    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private val marketRepository : MarketRepository by inject()
    private val couponRepository : CouponRepository by inject()
    private val paymentMethodRepository : PaymentMethodRepository by inject()
    private val productRepository : ProductRepository by inject()
    private val consumptionModeRepository : ConsumptionModeRepository by inject()
    private lateinit var selectedProductsMap: MutableMap<Long, Product>
    private lateinit var order: Order
    private lateinit var room: FoodTrackDB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        val root: View = binding.root

        order = GsonBuilder().create().fromJson(arguments?.getString("order"), Order::class.java)

        room = FoodTrackDB.getDatabase(requireContext())

        setOrderDataInScreen()

        return root
    }

    fun setOrderDataInScreen() {
                setMarketData()
                setConsumptionModeData()
                setPaymentMethodData()
                setPricesData()
    }

    fun setMarketData() {
        order.marketId?.let {
            val market = marketRepository.searchById(it)

            binding.marketName.text = market.name

            if (market.marketImg != null) {
                Picasso.get()
                    .load(market.marketImg)
                    .placeholder(R.drawable.ic_market)
                    .error(R.drawable.ic_no_image)
                    .into(binding.imgMarket)
            }
        }
    }

    fun setConsumptionModeData() {
        order.consumptionModeId?.let {
            val consumptionMode = consumptionModeRepository.searchById(it)

            binding.consumptionModeName.text = consumptionMode.name

            if (consumptionMode.consumptionModeImg != null) {
                Picasso.get()
                    .load(consumptionMode.consumptionModeImg)
                    .placeholder(R.drawable.ic_market)
                    .error(R.drawable.ic_no_image)
                    .into(binding.imgConsumptionMode)
            }
        }
    }

    fun setPaymentMethodData() {
        order.paymentMethodId?.let {
            val paymentMethod = paymentMethodRepository.searchById(it)

            binding.paymentMethodName.text = paymentMethod.name

            if (paymentMethod.paymentMethodImg != null) {
                Picasso.get()
                    .load(paymentMethod.paymentMethodImg)
                    .placeholder(R.drawable.ic_market)
                    .error(R.drawable.ic_no_image)
                    .into(binding.imgPaymentMethod)
            }
        }
    }

    fun setPricesData() {
        binding.totalValue.text = "$ ${order.totalPrice}"

        if (order.discountedPrice != null) {
            binding.totalValue.setBackgroundResource(R.drawable.line)

            binding.discountedTotalValue.visibility = View.VISIBLE
            binding.discountedTotalValue.text = "$${order.discountedPrice}"
        } else {
            binding.discountedTotalValue.visibility = View.GONE
        }
    }
}