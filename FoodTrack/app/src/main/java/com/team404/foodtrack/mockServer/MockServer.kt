package com.team404.foodtrack.mockServer

import com.team404.foodtrack.data.*
import com.team404.foodtrack.mockServer.mocks.*
import com.team404.foodtrack.utils.transformToLowercaseAndReplaceSpaceWithDash

class MockServer {

    fun searchMarkets() : List<Market> {
        return MarketsMocks.getMarketsMocks()
    }

    fun searchMarkets(name: String) : List<Market> {
        val marketList = MarketsMocks.getMarketsMocks()
        val marketListFiltered = mutableListOf<Market>()

        marketList.forEach { market ->
            val transformedMarketName = market.name?.let {
                transformToLowercaseAndReplaceSpaceWithDash(
                    it
                )
            }

            if (transformedMarketName != null) {
                if (transformedMarketName.contains(name)) {
                    marketListFiltered.add(market)
                }
            }

        }

        return marketListFiltered
    }

    fun searchMarketById(id: Long) : Market {
        val markets = MarketsMocks.getMarketsMocks()
        return markets.first { market -> market.id!! == id }
    }

    fun searchProductById(id: Long) : Product {
        val products = ProductsMocks.getProductsMocks()
        return products.first { product -> product.id!! == id }
    }

    fun searchProductsById(ids: List<Long>) : List<Product> {
        val products = ProductsMocks.getProductsMocks()
        return products.filter { product -> product.id in ids }
    }

    fun searchMenuItemsByMenuId(menuId: Long) : List<MenuItem> {
        val menuItems = MenuItemsMocks.getMenuItemsMocks()
        return menuItems.filter { menuItem -> menuItem.menuId == menuId }
    }

    fun searchCoupons() : List<Coupon> {
        return CouponsMocks.getCouponsMocks()
    }

    fun searchMenuByMarketId(marketId:Long) : Menu {
        val menus = MenusMocks.getMenusMocks()
        return menus.first { menu -> menu.marketId!! == marketId }
    }

    fun searchCouponById(id: Long) : Coupon {
        val coupons = CouponsMocks.getCouponsMocks()
        return coupons.first { market -> market.id!! == id }
    }
}