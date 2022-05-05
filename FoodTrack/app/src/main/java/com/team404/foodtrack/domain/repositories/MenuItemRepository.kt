package com.team404.foodtrack.domain.repositories

import com.team404.foodtrack.data.MenuItem
import com.team404.foodtrack.mockServer.MockServer

class MenuItemRepository(private val mockServer: MockServer) {

    fun searchByMenuId(menuId: Long) : List<MenuItem> {
        return mockServer.searchMenuItemsByMenuId(menuId)
    }
}