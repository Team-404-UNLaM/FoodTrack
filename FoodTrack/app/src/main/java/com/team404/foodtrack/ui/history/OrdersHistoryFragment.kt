package com.team404.foodtrack.ui.history

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.team404.foodtrack.R
import com.team404.foodtrack.configuration.FoodTrackDB
import com.team404.foodtrack.data.Order
import com.team404.foodtrack.data.OrderHistory
import com.team404.foodtrack.databinding.FragmentOrdersHistoryBinding
import com.team404.foodtrack.domain.adapters.OrderHistoryAdapter
import com.team404.foodtrack.domain.factories.OrdersHistoryViewModelFactory
import com.team404.foodtrack.domain.mappers.OrderHistoryMapper
import com.team404.foodtrack.domain.mappers.OrderMapper
import com.team404.foodtrack.domain.repositories.OrderHistoryRepository
import com.team404.foodtrack.domain.repositories.OrderRepository
import com.team404.foodtrack.domain.services.OrdersHistoryService
import com.team404.foodtrack.utils.transformToLowercaseAndReplaceSpaceWithDash
import org.koin.android.ext.android.inject

class OrdersHistoryFragment : Fragment() {

    private lateinit var factory: OrdersHistoryViewModelFactory
    private lateinit var viewModel: OrdersHistoryViewModel
    private lateinit var orderHistoryAdapter: OrderHistoryAdapter
    private var _binding: FragmentOrdersHistoryBinding? = null
    private var searchInputValue: String = ""
    private val binding get() = _binding!!
    private val orderHistoryRepository: OrderHistoryRepository by inject()
    private val orderMapper : OrderMapper by inject()
    private val orderHistoryMapper: OrderHistoryMapper by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrdersHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val room = FoodTrackDB.getDatabase(requireContext())
        val ordersHistoryService = OrdersHistoryService(orderHistoryRepository, OrderRepository(room.minifiedOrderDao()), orderMapper, orderHistoryMapper)
        factory = OrdersHistoryViewModelFactory(ordersHistoryService)
        viewModel = ViewModelProvider(this, factory).get(OrdersHistoryViewModel::class.java)

        injectDependencies(root)
        setUpRecyclerView(root)
        setUpListeners(root)
        setUpObserver()

        return root
    }

    private fun injectDependencies(view: View) {
        val viewClickListener = { orderHistory: OrderHistory ->
            Snackbar.make(view, "Orden: ${orderHistory.order.id}", Snackbar.LENGTH_SHORT).show()
        }

        val viewMarketClickListener = { orderHistory: OrderHistory ->
            val bundle = Bundle()
            orderHistory.market.id?.let { bundle.putLong("marketId", it) }
            Navigation.findNavController(view)
                .navigate(R.id.action_ordersHistoryFragment_to_marketFragment, bundle)
        }


        orderHistoryAdapter = OrderHistoryAdapter(viewClickListener, viewMarketClickListener)
    }

    private fun setUpRecyclerView(view: View) {
        viewModel.getMarketList(view)

        binding.recyclerViewOrdersHistory.also {
            it.layoutManager = GridLayoutManager(context, 1, LinearLayoutManager.VERTICAL, false)
            it.setHasFixedSize(true)
            it.adapter = orderHistoryAdapter
        }
    }

    private fun setUpListeners(root: View) {
        setSearchOrderListener(root)
    }

    private fun setUpObserver() {
        viewModel.ordersHistoryList.observe(viewLifecycleOwner) { orderHistoryList ->
            if (orderHistoryList != null) {
                orderHistoryAdapter.updateOrderHistoryList(orderHistoryList)
                orderHistoryAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setSearchOrderListener(root: View) {
        binding.txtSearchOrder.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                searchInputValue = transformToLowercaseAndReplaceSpaceWithDash(binding.txtSearchOrder.text.toString())

                searchOrderBySearchInputValue(root)
            }

        })
    }

    private fun searchOrderBySearchInputValue(view: View) {
        if (searchInputValue.isNotEmpty()) {
            viewModel.getFilteredMarketList(searchInputValue)
        } else {
            viewModel.getMarketList(view)
        }
    }
}