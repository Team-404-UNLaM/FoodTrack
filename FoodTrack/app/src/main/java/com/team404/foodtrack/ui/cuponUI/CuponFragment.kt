package com.team404.foodtrack.ui.cuponUI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.team404.foodtrack.R
import com.team404.foodtrack.databinding.FragmentCuponBinding
import com.team404.foodtrack.databinding.FragmentGalleryBinding

class CuponFragment : Fragment() {

    private var _binding: FragmentCuponBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCuponBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}