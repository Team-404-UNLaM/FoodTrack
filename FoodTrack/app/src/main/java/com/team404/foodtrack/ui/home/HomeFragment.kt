package com.team404.foodtrack.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.team404.foodtrack.R
import com.team404.foodtrack.databinding.FragmentHomeBinding
import com.team404.foodtrack.domain.repositories.MarketRepository
import com.team404.foodtrack.utils.transformToLowercaseAndReplaceSpaceWithDash

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        addGoToMarketListListener(root)

        return root
    }

    private fun addGoToMarketListListener(view: View) {
        binding.btnGoToMarketList.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_nav_home_to_marketListFragment, null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}