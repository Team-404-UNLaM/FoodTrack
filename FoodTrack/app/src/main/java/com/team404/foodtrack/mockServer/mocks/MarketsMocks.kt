package com.team404.foodtrack.mockServer.mocks

import com.team404.foodtrack.data.Market
import com.team404.foodtrack.data.MarketAddress

object MarketsMocks {

    fun getMarketsMocks() : List<Market> {
        val bigPons = Market.Builder()
            .id(111111L)
            .name("Big Pons")
            .address(MarketAddress(
                "Fitz Roy",
                "1731",
                "Palermo",
                -34.58378,
                -58.43586
            ))
            .stars(4.5)
            .description("Restaurant tranquilo, especializado en hamburguesas y acompañamientos caseros")
            .type(listOf("Bar"))
            .build()

        val miGusto = Market.Builder()
            .id(222222L)
            .name("Mi Gusto")
            .address(MarketAddress(
                "Av. Cnel. Niceto Vega",
                "5795",
                "Palermo",
                -34.58511,
                -58.44113
            ))
            .stars(4.9)
            .description("Tus empanadas favoritas en un solo lugar")
            .type(listOf("Restaurant"))
            .build()

        val malcriadaCafe = Market.Builder()
            .id(333333L)
            .name("Malcriada Cafe")
            .address(MarketAddress(
                "Bonpland",
                "1367",
                "Palermo",
                -34.58634,
                -58.44069
            ))
            .stars(4.0)
            .description("Lugar ideal para tus desayunos o meriendas, conversar tranquilo y disfrutar de un buen cafe")
            .type(listOf("Cafeteria", "Restaurant"))
            .build()

        val lucille = Market.Builder()
            .id(444444L)
            .name("Lucille")
            .address(MarketAddress(
                "Gorriti",
                "5520",
                "Palermo",
                -34.58555,
                -58.43659
            ))
            .stars(4.8)
            .description("Resto-bar para disfrutar de buena comida, con buena musica y buen acompañamiento")
            .type(listOf("Bar", "Restaurant"))
            .build()

        return listOf(bigPons, miGusto, malcriadaCafe, lucille)
    }
}