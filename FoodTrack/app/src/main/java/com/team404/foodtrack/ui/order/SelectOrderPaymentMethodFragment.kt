package com.team404.foodtrack.ui.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import com.team404.foodtrack.R
import com.team404.foodtrack.data.Market
import com.team404.foodtrack.data.Order
import com.team404.foodtrack.data.Product
import com.team404.foodtrack.databinding.FragmentSelectOrderPaymentMethodBinding
import com.team404.foodtrack.databinding.SelectProductsDrawerLayoutBinding
import com.team404.foodtrack.domain.adapters.ExpandableSelectableMenuItemAdapter
import com.team404.foodtrack.domain.factories.SelectedProductViewModelFactory
import com.team404.foodtrack.domain.repositories.MarketRepository
import com.team404.foodtrack.domain.repositories.MenuRepository
import com.team404.foodtrack.domain.repositories.ProductRepository
import com.team404.poketeam.domain.adapters.SelectedProductAdapter
import org.koin.android.ext.android.inject

class SelectOrderPaymentMethodFragment : Fragment() {

    private var _binding: FragmentSelectOrderPaymentMethodBinding? = null

    private lateinit var order: Order.Builder

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectOrderPaymentMethodBinding.inflate(inflater, container, false)
        val root: View = binding.root

        order = GsonBuilder().create().fromJson(arguments?.getString("order"), Order.Builder::class.java)

        setUpListeners()

        return root
    }

    private fun setUpListeners() {
        binding.btnGoToProductSelect.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("order", GsonBuilder().create().toJson(order))
            Navigation.findNavController(it).navigate(R.id.action_selectOrderPaymentMethodFragment_to_selectOrderProductsFragment, bundle)
        }
    }
}