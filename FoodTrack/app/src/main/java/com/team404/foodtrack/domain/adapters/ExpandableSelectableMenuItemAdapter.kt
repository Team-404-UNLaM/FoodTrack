package com.team404.foodtrack.domain.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.team404.foodtrack.R
import com.team404.foodtrack.data.Market
import com.team404.foodtrack.data.Product
import kotlin.collections.HashMap

class ExpandableSelectableMenuItemAdapter internal constructor(
    private val context: Context,
    private val menuItemGroup: List<String>,
    private val productsByMenuItem: HashMap<String, List<Product>>,
    private val changeProductQuantity: (Product, Int) -> Unit) : BaseExpandableListAdapter() {

    private val selectedProductsData = mutableMapOf<Long, List<Any>>()

    override fun getGroupCount(): Int {
        return menuItemGroup.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return productsByMenuItem[menuItemGroup[groupPosition]]!!.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return menuItemGroup[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return productsByMenuItem[menuItemGroup[groupPosition]]!![childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var convertViewWrapper = convertView
        val groupTitle = getGroup(groupPosition) as String

        if (convertViewWrapper == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertViewWrapper = inflater.inflate(R.layout.menu_item_group, null)
        }

        val groupTextView = convertViewWrapper!!.findViewById<TextView>(R.id.list_parent)
        groupTextView.text = groupTitle

        return convertViewWrapper
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        var convertViewWrapper = convertView
        val product = getChild(groupPosition, childPosition) as Product

        if (convertViewWrapper == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertViewWrapper = inflater.inflate(R.layout.selectable_product, null)
        }

        val productNameView = convertViewWrapper!!.findViewById<TextView>(R.id.product_name)
        productNameView.text = product.name

        val productPriceView = convertViewWrapper.findViewById<TextView>(R.id.product_price)
        productPriceView.text = "$${product.price.toString()}"

        val productDescriptionView = convertViewWrapper.findViewById<TextView>(R.id.product_description)

        if (product.description != null) {
            productDescriptionView.text = product.description
        } else {
            productDescriptionView.visibility = View.GONE
        }

        val productImageView = convertViewWrapper.findViewById<ImageView>(R.id.img_product)
        Picasso.get()
            .load(product.productImg)
            .placeholder(R.drawable.ic_food)
            .error(R.drawable.ic_no_image)
            .into(productImageView)

        val productQuantityView = convertViewWrapper.findViewById<TextView>(R.id.product_quantity)

        if (selectedProductsData[product.id] !== null) {
            productQuantityView.text = selectedProductsData[product.id]?.get(1).toString()
        } else {
            productQuantityView.text = "0"
        }

        var actualProductQuantity = productQuantityView.text.toString().toInt()

        val addProductUnit = convertViewWrapper.findViewById<ImageView>(R.id.add_unit)
        addProductUnit.setOnClickListener {
            val newProductQuantity = ++actualProductQuantity
            changeProductQuantity(product, newProductQuantity)
        }

        val removeProductQuantity = convertViewWrapper.findViewById<ImageView>(R.id.remove_unit)

        removeProductQuantity.setOnClickListener {
            if (actualProductQuantity > 0) {
                val newProductQuantity = --actualProductQuantity
                changeProductQuantity(product, newProductQuantity)
            }
        }

        removeProductQuantity.visibility = if(actualProductQuantity == 0) View.GONE else View.VISIBLE

        return convertViewWrapper
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }

    fun updateSelectedProducts(results: MutableMap<Long, List<Any>>) {
        selectedProductsData.clear()
        if(results != null) {
            selectedProductsData.putAll(results)
        }
    }
}