package com.team404.foodtrack.mockServer.mocks

import com.team404.foodtrack.data.MenuItem
import com.team404.foodtrack.domain.repositories.ProductRepository
import com.team404.foodtrack.mockServer.MockServer

object MenuItemsMocks {

    fun getMenuItemsMocks() : List<MenuItem> {
        val mockServer = MockServer()
        val productRepository = ProductRepository(mockServer)

        // List of products by category
        val products1 = productRepository.searchByIds(listOf(666666L, 777777L))
        val products2 = productRepository.searchByIds(listOf(111111L, 222222L, 333333L))
        val products3 = productRepository.searchByIds(listOf(444444L, 555555L))
        val products4 = productRepository.searchByIds(listOf(888888L, 999999L))
        val products5 = productRepository.searchByIds(listOf(101010101010L, 111111111111L))
        val products6 = productRepository.searchByIds(listOf(121212121212L, 252525252525L))
        val products7 = productRepository.searchByIds(listOf(131313131313L, 242424242424L))
        val products8 = productRepository.searchByIds(listOf(141414141414L, 262626262626L))
        val products9 = productRepository.searchByIds(listOf(151515151515L, 161616161616L))
        val product10 = productRepository.searchByIds(listOf(171717171717L, 272727272727L))
        val product11 = productRepository.searchByIds(listOf(181818181818L, 191919191919L))
        val product12 = productRepository.searchByIds(listOf(202020202020L, 343434343434L))
        val product13 = productRepository.searchByIds(listOf(212121212121L, 353535353535L))
        val product14 = productRepository.searchByIds(listOf(222222222222L, 232323232323L))
        val product15 = productRepository.searchByIds(listOf(282828282828L, 292929292929L))
        val product16 = productRepository.searchByIds(listOf(303030303030L, 313131313131L))
        val product17 = productRepository.searchByIds(listOf(323232323232L, 333333333333L))
        val product18 = productRepository.searchByIds(listOf(363636363636L, 373737373737L))

        val menuItem1 = MenuItem.Builder()
            .id(111111L)
            .menuId(111111L)
            .category("Starters")
            .products(products1)
            .build()

        val menuItem2 = MenuItem.Builder()
            .id(222222L)
            .menuId(111111L)
            .category("Nuestras hamburguesas")
            .products(products2)
            .build()

        val menuItem3 = MenuItem.Builder()
            .id(333333L)
            .menuId(111111L)
            .category("Bebidas")
            .products(products3)
            .build()

        val menuItem4 = MenuItem.Builder()
            .id(444444L)
            .menuId(222222L)
            .category("Empanadas de verdad")
            .products(products4)
            .build()

        val menuItem5 = MenuItem.Builder()
            .id(555555L)
            .menuId(222222L)
            .category("Pizzas")
            .products(products5)
            .build()

        val menuItem6 = MenuItem.Builder()
            .id(666666L)
            .menuId(333333L)
            .category("Cafés")
            .products(products6)
            .build()

        val menuItem7 = MenuItem.Builder()
            .id(777777L)
            .menuId(333333L)
            .category("Porciones")
            .products(products7)
            .build()

        val menuItem8 = MenuItem.Builder()
            .id(888888L)
            .menuId(333333L)
            .category("Salados")
            .products(products8)
            .build()

        val menuItem9 = MenuItem.Builder()
            .id(999999L)
            .menuId(444444L)
            .category("Pizzas")
            .products(products9)
            .build()

        val menuItem10 = MenuItem.Builder()
            .id(101010101010L)
            .menuId(444444L)
            .category("Nuestras hamburguesas")
            .products(product10)
            .build()

        val menuItem11 = MenuItem.Builder()
            .id(111111111111L)
            .menuId(555555L)
            .category("Nuestras hamburguesas")
            .products(product11)
            .build()

        val menuItem12 = MenuItem.Builder()
            .id(121212121212L)
            .menuId(666666L)
            .category("Starters")
            .products(product12)
            .build()

        val menuItem13 = MenuItem.Builder()
            .id(131313131313L)
            .menuId(666666L)
            .category("Pizzas")
            .products(product13)
            .build()

        val menuItem14 = MenuItem.Builder()
            .id(141414141414L)
            .menuId(777777L)
            .category("Sándwiches")
            .products(product14)
            .build()

        val menuItem15 = MenuItem.Builder()
            .id(151515151515L)
            .menuId(444444L)
            .category("Bebidas")
            .products(product15)
            .build()

        val menuItem16 = MenuItem.Builder()
            .id(161616161616L)
            .menuId(555555L)
            .category("Starters")
            .products(product16)
            .build()

        val menuItem17 = MenuItem.Builder()
            .id(171717171717L)
            .menuId(555555L)
            .category("Bebidas")
            .products(product17)
            .build()

        val menuItem18 = MenuItem.Builder()
            .id(181818181818L)
            .menuId(777777L)
            .category("Tartas")
            .products(product18)
            .build()

        return listOf(menuItem1, menuItem2, menuItem3, menuItem4, menuItem5, menuItem6, menuItem7, menuItem8,
            menuItem9, menuItem10, menuItem11, menuItem12, menuItem13, menuItem14, menuItem15, menuItem16,
            menuItem17, menuItem18)
    }
}