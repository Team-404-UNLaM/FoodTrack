package com.team404.foodtrack.ui.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import com.team404.foodtrack.R
import com.team404.foodtrack.data.Market
import com.team404.foodtrack.data.Order
import com.team404.foodtrack.data.Product
import com.team404.foodtrack.databinding.SelectProductsDrawerLayoutBinding
import com.team404.foodtrack.domain.adapters.ExpandableSelectableMenuItemAdapter
import com.team404.foodtrack.domain.factories.SelectedProductViewModelFactory
import com.team404.foodtrack.domain.repositories.MarketRepository
import com.team404.foodtrack.domain.repositories.MenuRepository
import com.team404.foodtrack.domain.repositories.ProductRepository
import com.team404.poketeam.domain.adapters.SelectedProductAdapter
import org.koin.android.ext.android.inject

class SelectOrderProductsFragment : Fragment() {

    private lateinit var listViewAdapter: ExpandableSelectableMenuItemAdapter
    private lateinit var menuItemGroup: List<String>
    private lateinit var productsByMenuItem: HashMap<String, List<Product>>
    private lateinit var factory: SelectedProductViewModelFactory
    private lateinit var viewModel: SelectedProductViewModel
    private lateinit var selectedProductAdapter: SelectedProductAdapter
    private var _binding: SelectProductsDrawerLayoutBinding? = null
    private val menuRepository : MenuRepository by inject()
    private val marketRepository: MarketRepository by inject()
    private val productRepository: ProductRepository by inject()

    private lateinit var order: Order.Builder

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SelectProductsDrawerLayoutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        factory = SelectedProductViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(SelectedProductViewModel::class.java)

        order = GsonBuilder().create().fromJson(arguments?.getString("order"), Order.Builder::class.java)

        if(order.marketId != null) {

            val market: Market = marketRepository.searchById(order.marketId!!)

            buildExpandableList(market.id!!)

            listViewAdapter = ExpandableSelectableMenuItemAdapter(requireContext(), menuItemGroup, productsByMenuItem, ::changeProductQuantity)
            binding.expandableMenuItems.setAdapter(listViewAdapter)

            binding.marketName.text = market.name?: ""

            if (market.marketImg != null) {
                Picasso.get()
                    .load(market.marketImg)
                    .placeholder(R.drawable.ic_market)
                    .error(R.drawable.ic_no_image)
                    .into(binding.imgMarket)
            }

            injectDependencies()
            setUpRecyclerView(root)
            setUpObserver()
            setUpListeners()
        }

        return root
    }

    private fun setUpListeners() {
        binding.btnGoToPay.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("order", GsonBuilder().create().toJson(order))
            Navigation.findNavController(it).navigate(R.id.action_selectOrderProductsFragment_to_selectOrderPaymentMethodFragment, bundle)
        }
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

    private fun injectDependencies() {
        var products: MutableMap<Long, List<Any>>? = null
        if(order.products != null && order.products?.size!! > 0) {
            products = getSelectedProductsForSelectProductsFragment()
        }
        viewModel.initializeSelectedProducts(products)
        selectedProductAdapter = SelectedProductAdapter()
    }

    private fun getSelectedProductsForSelectProductsFragment(): MutableMap<Long, List<Any>> {
        val products: MutableMap<Long, List<Any>> = mutableMapOf()
        order.products?.forEach { (key, value) ->
            val product = productRepository.searchById(key)
            products.put(key, listOf(product, value))
        }
        return products
    }

    private fun setUpRecyclerView(view: View) {
        binding.recyclerViewSelectedProducts.also {
            it.layoutManager = GridLayoutManager(context, 1, LinearLayoutManager.VERTICAL, false)
            it.setHasFixedSize(true)
            it.adapter = selectedProductAdapter
            it.addItemDecoration(
                DividerItemDecoration(
                    view.context,
                    LinearLayoutManager.VERTICAL
                )
            )
        }
    }

    private fun setUpObserver() {
        viewModel.selectedProducts.observe(viewLifecycleOwner, { selectedProducts ->
            if (selectedProducts != null) {
                selectedProductAdapter.updateSelectedProductList(selectedProducts)
                selectedProductAdapter.notifyDataSetChanged()
                listViewAdapter.updateSelectedProducts(selectedProducts)
                listViewAdapter.notifyDataSetChanged()
                if (selectedProducts.size > 0) {
                    binding.totalPriceLayout.visibility = View.VISIBLE
                    binding.recyclerViewSelectedProducts.visibility = View.VISIBLE
                    binding.noProductsSelected.visibility = View.GONE
                    calculateOrderTotalPrice(selectedProducts)
                } else {
                    binding.totalPriceLayout.visibility = View.GONE
                    binding.recyclerViewSelectedProducts.visibility = View.GONE
                    binding.noProductsSelected.visibility = View.VISIBLE
                }
            }
        })
    }

    fun changeProductQuantity(product: Product, quantity: Int) {
        viewModel.changeProductQuantity(product, quantity)
        updateSelectedProductsForOrder(product, quantity)
    }

    fun updateSelectedProductsForOrder(product: Product, quantity: Int) {
        if(order.products == null) order.products(mutableMapOf())

        if (order.products?.get(product.id) == null) {
            order.products?.put(product.id!!, quantity)
        } else {
            if(quantity == 0) {
                product.id?.let { order.products!!.remove(it) }
            } else {
                order.products!![product.id!!] = quantity
            }
        }
    }

    fun calculateOrderTotalPrice(selectedProducts: MutableMap<Long, List<Any>>) {
        var totalPrice = 0.0
        selectedProducts.values.forEach { selectedProduct ->
            val product = selectedProduct[0] as Product
            val quantity = selectedProduct[1] as Int
            totalPrice = totalPrice.plus(product.price?.times(quantity) ?: 0.0)
        }

        binding.orderTotalPrice.text = "$ $totalPrice"
    }
}