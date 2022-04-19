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
import com.team404.foodtrack.domain.repositories.MarketFavoritesRepository
import com.team404.foodtrack.domain.repositories.MarketRepository
import com.team404.foodtrack.mockServer.MockServer
import com.team404.foodtrack.utils.SnackbarBuilder
import com.team404.foodtrack.utils.transformToLowercaseAndReplaceSpaceWithDash
import com.team404.poketeam.domain.adapters.MarketAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MarketListFragment : Fragment() {

    private lateinit var marketAdapter: MarketAdapter
    private lateinit var room: FoodTrackDB
    private var mockServer: MockServer = MockServer()
    private var _binding: FragmentMarketListBinding? = null
    private var query: String = ""


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarketListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        injectDependencies(root)
        setUpRecyclerView()
        setListeners(root)
        setUpMarketAdapter(root)

        return root
    }

    private fun injectDependencies(view: View) {
        room = FoodTrackDB.getDatabase(requireContext())

        val viewClickListener = { market: Market ->
        // TODO: Uncomment and modify when Market view exist
        /*val bundle = Bundle()
            market.id?.let { bundle.putLong("marketId", it) }
            Navigation.findNavController(view)
                .navigate(R.id.action_navigation_marketListFragment_to_x, bundle)*/
        }

        val favoriteClickListener = { market: Market ->
            CoroutineScope(Dispatchers.Default).launch {
                var marketFavorite: MarketFavorites? = market.id?.let {
                    MarketFavoritesRepository(room.marketFavoritesDao()).getByMarketId(
                        it
                    )
                }

                if (marketFavorite != null) {
                    MarketFavoritesRepository(room.marketFavoritesDao()).delete(marketFavorite)
                } else {
                    marketFavorite = MarketFavorites(market.id!!, market.name!!, market.address!!.city, market.stars!!)
                    MarketFavoritesRepository(room.marketFavoritesDao()).insert(marketFavorite)
                }

                searchMarketBySearchboxValue(view, query)
            }
        }

        marketAdapter = MarketAdapter(viewClickListener, favoriteClickListener)
    }

    private fun setListeners(root: View) {
        setSearchMarketListener(root)
    }

    private fun setSearchMarketListener(root: View) {
        binding.txtSearchMarket.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                query = transformToLowercaseAndReplaceSpaceWithDash(binding.txtSearchMarket.text.toString())

                searchMarketBySearchboxValue(root, query)
            }

        })
    }

    private fun searchMarketBySearchboxValue(root: View, query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val marketFavoritesList = MarketFavoritesRepository(room.marketFavoritesDao()).search()

            if(marketFavoritesList != null) {
                withContext(Dispatchers.Main) {
                    var marketList: MutableList<Market>? = null

                    if (query.isNotEmpty()) {
                        marketList = MarketRepository(mockServer).searchByName(query) as MutableList<Market>
                    } else {
                        marketList = MarketRepository(mockServer).search() as MutableList<Market>
                    }

                    if(marketList != null) {
                        marketAdapter.updateMarketList(marketList, marketFavoritesList)
                        marketAdapter.notifyDataSetChanged()
                    }
                }
            } else {
                SnackbarBuilder.showErrorMessage(root)
            }
        }
    }

    private fun setUpMarketAdapter(root: View) {
        CoroutineScope(Dispatchers.IO).launch {
            val marketList = MarketRepository(mockServer).search()
            val marketFavoritesList = MarketFavoritesRepository(room.marketFavoritesDao()).search()

            if (marketList != null && marketFavoritesList != null) {
                withContext(Dispatchers.Main) {
                    marketAdapter.updateMarketList(marketList, marketFavoritesList)
                    marketAdapter.notifyDataSetChanged()
                }
            } else {
                SnackbarBuilder.showErrorMessage(root)
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.recyclerViewMarket.layoutManager =
            GridLayoutManager(context, 1, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewMarket.adapter = marketAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}