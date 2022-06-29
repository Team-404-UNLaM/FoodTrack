package com.team404.foodtrack.domain.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.team404.foodtrack.R
import com.team404.foodtrack.data.Product
import com.team404.foodtrack.databinding.ItemProductBinding

class ProductsAdapter() :
    RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {

    private var productsSelectedMap: Map<Long, Product>? = null
    private var quantitiesSelectedMap: Map<Long, Int>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val productBinding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ProductsViewHolder(productBinding)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val productId = productsSelectedMap?.keys!!.elementAt(position)
        val product = productsSelectedMap!![productId]
        val productQuantity = quantitiesSelectedMap?.get(productId)

        holder.binding.productName.text = product!!.name
        holder.binding.productQuantity.text = "${productQuantity} X"
        holder.binding.productPrice.text = "$${product.price.toString()}"
        holder.binding.productTotalPrice.text = "$${product.price!!.times(productQuantity!!)}"

        Picasso.get()
            .load(product?.productImg)
            .placeholder(R.drawable.ic_food)
            .error(R.drawable.ic_no_image)
            .into(holder.binding.productImg)

        holder.binding.productItem.setBackgroundColor(Color.parseColor("#FFFFFFFF"))
    }

    override fun getItemCount(): Int {
        return productsSelectedMap?.size ?: 0
    }

    fun updateProductsSelectedList(products: Map<Long, Product>?, quantities: Map<Long, Int>?) {
        if(products != null && quantities != null) {
            productsSelectedMap = products
            quantitiesSelectedMap = quantities
        }
    }

    class ProductsViewHolder (val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

}