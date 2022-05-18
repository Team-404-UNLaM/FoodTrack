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
import com.team404.foodtrack.data.Product
import kotlin.collections.HashMap

class ExpandableMenuItemAdapter internal constructor(
    private val context: Context,
    private val menuItemGroup: List<String>,
    private val productsByMenuItem: HashMap<String, List<Product>>) : BaseExpandableListAdapter() {

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
            convertViewWrapper = inflater.inflate(R.layout.product, null)
        }

        val productNameView = convertViewWrapper!!.findViewById<TextView>(R.id.product_name)
        productNameView.text = product.name

        val productPriceView = convertViewWrapper.findViewById<TextView>(R.id.product_price)
        productPriceView.text = "$${product.price.toString()}"

        val productDescriptionView = convertViewWrapper.findViewById<TextView>(R.id.product_description)
        productDescriptionView.text = product.description

        val productImageView = convertViewWrapper.findViewById<ImageView>(R.id.img_product)
        Picasso.get()
            .load(product.productImg)
            .placeholder(R.drawable.ic_food)
            .error(R.drawable.ic_no_image)
            .into(productImageView)

        return convertViewWrapper
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }
}