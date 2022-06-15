package com.team404.foodtrack.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.team404.foodtrack.configuration.FoodTrackDB
import com.team404.foodtrack.R
import androidx.navigation.Navigation
import com.google.gson.GsonBuilder
import com.team404.foodtrack.data.Order
import com.team404.foodtrack.data.PaymentMethod
import com.team404.foodtrack.databinding.FragmentSelectPaymentMethodBinding
import com.team404.foodtrack.domain.repositories.PaymentMethodRepository
import com.team404.poketeam.domain.adapters.PaymentMethodAdapter
import org.koin.android.ext.android.inject


class SelectPaymentMethodFragment : Fragment() {

    private lateinit var paymentMethodAdapter: PaymentMethodAdapter
    private lateinit var room: FoodTrackDB
    private var _binding: FragmentSelectPaymentMethodBinding? = null
    private val binding get() = _binding!!
    private val paymentMethodRepository : PaymentMethodRepository by inject()

    private lateinit var order: Order.Builder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectPaymentMethodBinding.inflate(inflater, container, false)
        val root: View = binding.root

        order = GsonBuilder().create().fromJson(arguments?.getString("order"), Order.Builder::class.java)

        injectDependencies()
        setUpRecyclerView()
        setUpListeners()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun injectDependencies() {
        room = FoodTrackDB.getDatabase(requireContext())

        val selectPatmentMethodClickListener = { paymentMethod: PaymentMethod ->
            order.paymentMethodId(
                if (order.paymentMethodId == paymentMethod.id) 0L else paymentMethod.id!!
            )
            paymentMethodAdapter.notifyDataSetChanged()
        }

        paymentMethodAdapter = PaymentMethodAdapter(selectPatmentMethodClickListener)
    }

    private fun setUpRecyclerView() {
        val paymentMethods = paymentMethodRepository.search()

        binding.rvPaymentMethod.also {
            it.layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
            it.setHasFixedSize(true)
            it.adapter = paymentMethodAdapter
        }

        paymentMethodAdapter.updatePaymentMethodList(paymentMethods, order.paymentMethodId)
        paymentMethodAdapter.notifyDataSetChanged()
    }

    private fun setUpListeners() {
        binding.btnGoToSelectProductsOrSelectCoupon.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("order", GsonBuilder().create().toJson(order))
            Navigation.findNavController(it).navigate(R.id.action_selectPaymentMethodFragment_to_selectOrderProductsFragment, bundle)
        }
    }
}