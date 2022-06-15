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
import com.team404.foodtrack.data.ConsumptionMode
import com.team404.foodtrack.data.Order
import com.team404.foodtrack.databinding.FragmentSelectConsumptionModeBinding
import com.team404.foodtrack.domain.repositories.ConsumptionModeRepository
import com.team404.foodtrack.domain.adapters.ConsumptionModeAdapter
import org.koin.android.ext.android.inject


class SelectConsumptionModeFragment : Fragment() {

    private lateinit var consumptionModeAdapter: ConsumptionModeAdapter
    private lateinit var room: FoodTrackDB
    private var _binding: FragmentSelectConsumptionModeBinding? = null
    private val binding get() = _binding!!
    private val consumptionModeRepository : ConsumptionModeRepository by inject()

    private lateinit var order: Order.Builder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectConsumptionModeBinding.inflate(inflater, container, false)
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

        val selectConsumptionModeClickListener = { consumptionMode: ConsumptionMode ->
            order.consumptionModeId(
                if (order.consumptionModeId == consumptionMode.id) 0L else consumptionMode.id!!
            )
            consumptionModeAdapter.notifyDataSetChanged()
        }

        consumptionModeAdapter = ConsumptionModeAdapter(selectConsumptionModeClickListener)
    }

    private fun setUpRecyclerView() {
        val consumptionModes = consumptionModeRepository.search()

        binding.rvConsumptionMode.also {
            it.layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
            it.setHasFixedSize(true)
            it.adapter = consumptionModeAdapter
        }

        consumptionModeAdapter.updateConsumptionModeList(consumptionModes, order.consumptionModeId)
        consumptionModeAdapter.notifyDataSetChanged()
    }

    private fun setUpListeners() {
        binding.btnGoToSelectProducts.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("order", GsonBuilder().create().toJson(order))
            Navigation.findNavController(it).navigate(R.id.action_selectConsumptionModeFragment_to_selectOrderProductsFragment, bundle)
        }
    }
}