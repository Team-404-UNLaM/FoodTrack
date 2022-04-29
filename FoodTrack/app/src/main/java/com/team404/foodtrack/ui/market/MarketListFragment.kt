package com.team404.foodtrack.ui.market

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.team404.foodtrack.utils.transformToLowercaseAndReplaceSpaceWithDash
import com.team404.poketeam.domain.adapters.MarketAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject


class MarketListFragment : Fragment() {

    private lateinit var factory: MarketListViewModelFactory
    private lateinit var viewModel: MarketListViewModel
    private lateinit var marketAdapter: MarketAdapter
    private lateinit var room: FoodTrackDB
    private var _binding: FragmentMarketListBinding? = null
    private var searchInputValue: String = ""
    private val binding get() = _binding!!
    private val marketFavoritesMapper: MarketFavoritesMapper = MarketFavoritesMapper()
    private val marketRepository : MarketRepository by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarketListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        factory = MarketListViewModelFactory(marketRepository)
        viewModel = ViewModelProvider(this, factory).get(MarketListViewModel::class.java)

        injectDependencies(root)
        setUpRecyclerView(root)
        setUpListeners(root)
        setUpObserver()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun injectDependencies(view: View) {
        room = FoodTrackDB.getDatabase(requireContext())

        val viewClickListener = { market: Market ->
        val bundle = Bundle()
            market.id?.let { bundle.putLong("marketId", it) }
            Navigation.findNavController(view)
                .navigate(R.id.action_marketListFragment_to_marketFragment, bundle)
        }

        val isFavoriteClickListener = { market: Market -> changeFavoriteMarket(market) }

        marketAdapter = MarketAdapter(viewClickListener, isFavoriteClickListener)
    }

    private fun setUpRecyclerView(view: View) {
        viewModel.getMarketList(view)

        binding.recyclerViewMarket.also {
            it.layoutManager = GridLayoutManager(context, 1, LinearLayoutManager.VERTICAL, false)
            it.setHasFixedSize(true)
            it.adapter = marketAdapter
        }
    }

    private fun setUpListeners(root: View) {
        setSearchMarketListener(root)
    }

    private fun setUpObserver() {
        viewModel.marketList.observe(viewLifecycleOwner, { marketList ->
            if (marketList != null) {
                marketAdapter.updateMarketList(marketList)
                marketAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun changeFavoriteMarket(market: Market) {
        market.id?.let {
            CoroutineScope(Dispatchers.Default).launch {
                val marketFavoritesRepository = MarketFavoritesRepository(room.marketFavoritesDao())
                var marketFavorite: MarketFavorites? = marketFavoritesRepository.getByMarketId(it)

                if (marketFavorite != null) {
                    marketFavoritesRepository.delete(marketFavorite)
                } else {
                    marketFavorite = marketFavoritesMapper.map(market)
                    marketFavoritesRepository.insert(marketFavorite)
                }

                withContext(Dispatchers.Main) {
                    marketAdapter.notifyDataSetChanged()
                }
            }
        }

    }

    private fun setSearchMarketListener(root: View) {
        binding.txtSearchMarket.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                searchInputValue = transformToLowercaseAndReplaceSpaceWithDash(binding.txtSearchMarket.text.toString())

                searchMarketBySearchInputValue()
            }

        })
    }

    private fun searchMarketBySearchInputValue() {
        if (searchInputValue.isNotEmpty()) {
            viewModel.marketList.value = marketRepository.searchByName(searchInputValue) as MutableList<Market>
        } else {
            viewModel.marketList.value = marketRepository.search() as MutableList<Market>
        }
    }
}