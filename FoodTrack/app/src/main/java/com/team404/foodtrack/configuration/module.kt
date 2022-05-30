package com.team404.foodtrack.configuration

import com.team404.foodtrack.domain.repositories.*
import com.team404.foodtrack.mockServer.MockServer
import org.koin.dsl.module

val appModule = module {
    factory { MarketRepository(get()) }
    factory { CouponRepository(get()) }
    factory { MenuRepository(get()) }
    factory { ProductRepository(get()) }
}

val mockServerModule = module {
    single { MockServer() }
}