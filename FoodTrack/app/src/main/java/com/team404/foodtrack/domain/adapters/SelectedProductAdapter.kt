package com.team404.foodtrack.domain.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import androidx.recyclerview.widget.RecyclerView
import com.team404.foodtrack.R
import com.team404.foodtrack.data.Product
import com.team404.foodtrack.databinding.GridLayoutProductBinding
import com.team404.foodtrack.domain.holders.SelectedProductViewHolder

class SelectedProductAdapter () : RecyclerView.Adapter<SelectedProductViewHolder>() {

    private val selectedProductList = mutableMapOf<Long, List<Any>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedProductViewHolder {
        val productBinding = GridLayoutProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SelectedProductViewHolder(productBinding)
    }

    override fun onBindViewHolder(holder: SelectedProductViewHolder, position: Int) {
        val selectedProductKeys = selectedProductList.keys
        val selectedProductId = selectedProductKeys.elementAt(position)
        val selectedProductData = selectedProductList[selectedProductId] as List<Any>
        val product = selectedProductData[0] as Product
        val quantity = selectedProductData[1] as Int

        holder.binding.productName.text = product.name
        holder.binding.productQuantity.text = "$quantity X"
        holder.binding.productPrice.text = "$ ${product.price}"
        holder.binding.productTotalPrice.text = "$ ${product.price?.times(quantity)}"

        if (product.productImg != null) {
            Picasso.get()
                .load(product.productImg)
                .placeholder(R.drawable.ic_food)
                .error(R.drawable.ic_no_image)
                .into(holder.binding.productImg)
        }
    }

    override fun getItemCount(): Int {
        return selectedProductList.size
    }

    fun updateSelectedProductList(results: MutableMap<Long, List<Any>>?) {
        selectedProductList.clear()
        if(results != null) {
            selectedProductList.putAll(results)
        }
    }

}