package com.team404.foodtrack.data

class MenuItem(
    val id: Long?,
    val category: String?,
    val products: List<Product>?,
    val menuId: Long?) {

    data class Builder(
        var id: Long? = null,
        var category: String? = null,
        var products: List<Product>? = null,
        var menuId: Long? = null,) {

        fun id(id: Long) = apply { this.id = id }
        fun category(category: String) = apply { this.category = category }
        fun products(products: List<Product>) = apply { this.products = products }
        fun menuId(menuId: Long) = apply { this.menuId = menuId }
        fun build() = MenuItem(id, category, products, menuId)
    }
}