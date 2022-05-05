package com.team404.foodtrack.mockServer.mocks

import com.team404.foodtrack.data.Menu
import com.team404.foodtrack.domain.repositories.MenuItemRepository
import com.team404.foodtrack.mockServer.MockServer

object MenusMocks {

    fun getMenusMocks() : List<Menu> {

        val mockServer = MockServer()
        val menuItemRepository = MenuItemRepository(mockServer)

        // List of menu items by menu
        val menuItems1 = menuItemRepository.searchByMenuId(111111L)
        val menuItems2 = menuItemRepository.searchByMenuId(222222L)
        val menuItems3 = menuItemRepository.searchByMenuId(333333L)
        val menuItems4 = menuItemRepository.searchByMenuId(444444L)

        val menu1 = Menu.Builder()
            .id(111111L)
            .marketId(111111L)
            .items(menuItems1)
            .build()

        val menu2 = Menu.Builder()
            .id(222222L)
            .marketId(222222L)
            .items(menuItems2)
            .build()

        val menu3 = Menu.Builder()
            .id(333333L)
            .marketId(333333L)
            .items(menuItems3)
            .build()

        val menu4 = Menu.Builder()
            .id(444444L)
            .marketId(444444L)
            .items(menuItems4)
            .build()

        return listOf(menu1, menu2, menu3, menu4)
    }
}