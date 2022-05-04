package com.team404.foodtrack.configuration

import com.team404.foodtrack.domain.repositories.CouponRepository
import com.team404.foodtrack.domain.repositories.MarketFavoritesRepository
import com.team404.foodtrack.domain.repositories.MarketRepository
import com.team404.foodtrack.mockServer.MockServer
import org.koin.dsl.module

val appModule = module {
    factory { MarketRepository(get()) }
    factory { CouponRepository(get()) }
}

val mockServerModule = module {
    single { MockServer() }
}