package com.team404.foodtrack.ui.order

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.team404.foodtrack.configuration.FoodTrackDB
import com.team404.foodtrack.data.Market
import com.team404.foodtrack.data.database.MarketFavorites
import com.team404.foodtrack.databinding.FragmentMarketListBinding
import com.team404.foodtrack.domain.factories.MarketListViewModelFactory
import com.team404.foodtrack.domain.mappers.MarketFavoritesMapper
import com.team404.foodtrack.domain.repositories.MarketFavoritesRepository
import com.team404.foodtrack.domain.repositories.MarketRepository
import com.team404.foodtrack.R
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.gson.GsonBuilder
import com.team404.foodtrack.data.ConsumptionMode
import com.team404.foodtrack.data.Order
import com.team404.foodtrack.databinding.FragmentSelectConsumptionModeBinding
import com.team404.foodtrack.domain.repositories.ConsumptionModeRepository
import com.team404.foodtrack.ui.market.MarketListViewModel
import com.team404.foodtrack.utils.transformToLowercaseAndReplaceSpaceWithDash
import com.team404.poketeam.domain.adapters.ConsumptionModeAdapter
import com.team404.poketeam.domain.adapters.MarketAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject


class SelectConsumptionModeFragment : Fragment() {

    private lateinit var consumptionModeAdapter: ConsumptionModeAdapter
    private lateinit var room: FoodTrackDB
    private var _binding: FragmentSelectConsumptionModeBinding? = null
    private var selectedConsumptionMode: Long = 0L
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