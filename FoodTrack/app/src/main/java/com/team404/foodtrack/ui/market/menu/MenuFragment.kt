package com.team404.foodtrack.ui.market.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.team404.foodtrack.data.Product
import com.team404.foodtrack.databinding.FragmentMenuBinding
import com.team404.foodtrack.domain.adapters.ExpandableMenuItemAdapter
import com.team404.foodtrack.R
import com.team404.foodtrack.domain.repositories.MenuRepository
import org.koin.android.ext.android.inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MenuFragment : Fragment() {

    private lateinit var listViewAdapter: ExpandableMenuItemAdapter
    private lateinit var menuItemGroup: List<String>
    private lateinit var productsByMenuItem: HashMap<String, List<Product>>
    private var _binding: FragmentMenuBinding? = null
    private val menuRepository : MenuRepository by inject()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val marketId = arguments?.getLong("marketId")
        val marketName = arguments?.getString("marketName")
        val marketImg = arguments?.getString("marketImg")

        if(marketId != null) {

            buildExpandableList(marketId)

            listViewAdapter = ExpandableMenuItemAdapter(requireContext(), menuItemGroup, productsByMenuItem)
            binding.expandableMenuItems.setAdapter(listViewAdapter)

            binding.marketName.text = marketName?: ""


            if (marketImg != null) {
                Picasso.get()
                    .load(marketImg)
                    .placeholder(R.drawable.ic_market)
                    .error(R.drawable.ic_no_image)
                    .into(binding.imgMarket)
            }
        }

        return root
    }

    private fun buildExpandableList(marketId: Long) {
        val menu = menuRepository.searchMenuByMarketId(marketId)

        menuItemGroup = ArrayList()
        productsByMenuItem = HashMap()

        if (menu.items != null && menu.items.size > 0) {
            menu.items.forEach { menuItem ->
                (menuItemGroup as ArrayList<String>).add(menuItem.category!!)
                productsByMenuItem[menuItem.category] = menuItem.products!!
            }
        }
    }
}