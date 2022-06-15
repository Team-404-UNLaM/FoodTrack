package com.team404.foodtrack.configuration

import com.team404.foodtrack.domain.repositories.*
import com.team404.foodtrack.domain.services.CouponService
import com.team404.foodtrack.mockServer.MockServer
import org.koin.dsl.module

val appModule = module {
    single { MarketRepository(get()) }
    single { CouponRepository(get()) }
    single { MenuRepository(get()) }
    single { ProductRepository(get()) }
    single { ConsumptionModeRepository(get()) }
    factory { CouponService(get(), get()) }
}

val mockServerModule = module {
    single { MockServer() }
}