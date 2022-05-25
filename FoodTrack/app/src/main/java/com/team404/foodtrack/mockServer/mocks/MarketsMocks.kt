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
            .marketImg("https://lh4.googleusercontent.com/-rkvi-cLfJVw/AAAAAAAAAAI/AAAAAAAAAAA/z5hxB2EZhxY/s44-p-k-no-ns-nd/photo.jpg")
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
            .marketImg("https://lh6.googleusercontent.com/-KAQN0rCtfHQ/AAAAAAAAAAI/AAAAAAAAAAA/uG_1oErDfsI/s44-p-k-no-ns-nd/photo.jpg")
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
            .marketImg("https://lh5.googleusercontent.com/-RodSLM67Y0U/AAAAAAAAAAI/AAAAAAAAAAA/22-YOVR6zKk/s44-p-k-no-ns-nd/photo.jpg")
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

        val hood = Market.Builder()
            .id(555555L)
            .name("Hood")
            .address(MarketAddress(
                "Florencio Varela",
                "1754",
                "San Justo",
                -34.67124749076973,
            -58.56305038635756
            ))
            .marketImg("https://lh4.googleusercontent.com/-rkvi-cLfJVw/AAAAAAAAAAI/AAAAAAAAAAA/z5hxB2EZhxY/s44-p-k-no-ns-nd/photo.jpg")
            .stars(4.8)
            .description("La mejor hamburguesa del oeste, ¿Te la vas a perder?")
            .type(listOf("Bar"))
            .build()

        val clubCervecero = Market.Builder()
            .id(666666L)
            .name("Club Cervecero UNLAM")
            .address(MarketAddress(
                "Pres. Juan Domingo Perón",
                "2602",
                "San Justo",
                -34.6713802064953,
                -58.563396991033954
            ))
            .marketImg("https://lh4.googleusercontent.com/-rkvi-cLfJVw/AAAAAAAAAAI/AAAAAAAAAAA/z5hxB2EZhxY/s44-p-k-no-ns-nd/photo.jpg")
            .stars(3.2)
            .description("Pizzas, hamburguesas y papitas frente a tu universidad favorita")
            .type(listOf("Bar"))
            .build()

        val barEnfrente = Market.Builder()
            .id(777777L)
            .name("Bar de enfrente")
            .address(MarketAddress(
                "Florencio Varela",
                "1940",
                "San Justo",
                -34.670456691404546,
                -58.56220037928297
            ))
            .marketImg("https://lh4.googleusercontent.com/-rkvi-cLfJVw/AAAAAAAAAAI/AAAAAAAAAAA/z5hxB2EZhxY/s44-p-k-no-ns-nd/photo.jpg")
            .stars(3.2)
            .description("La mejor comida, la mejor musica, el mejor plan")
            .type(listOf("Bar"))
            .build()

        return listOf(bigPons, miGusto, malcriadaCafe, lucille, hood, clubCervecero, barEnfrente)
    }
}