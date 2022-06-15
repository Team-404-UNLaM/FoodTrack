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
        val menuItems5 = menuItemRepository.searchByMenuId(555555L)
        val menuItems6 = menuItemRepository.searchByMenuId(666666L)
        val menuItems7 = menuItemRepository.searchByMenuId(777777L)


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

        val menu5 = Menu.Builder()
            .id(555555L)
            .marketId(555555L)
            .items(menuItems5)
            .build()

        val menu6 = Menu.Builder()
            .id(666666L)
            .marketId(666666L)
            .items(menuItems6)
            .build()

        val menu7 = Menu.Builder()
            .id(777777L)
            .marketId(777777L)
            .items(menuItems7)
            .build()

        return listOf(menu1, menu2, menu3, menu4, menu5, menu6, menu7)
    }
}