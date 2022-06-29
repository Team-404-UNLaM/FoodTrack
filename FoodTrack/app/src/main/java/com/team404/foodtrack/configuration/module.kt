package com.team404.foodtrack.configuration

import com.team404.foodtrack.domain.mappers.MinifiedOrderMapper
import com.team404.foodtrack.domain.mappers.OrderHistoryMapper
import com.team404.foodtrack.domain.mappers.OrderMapper
import com.team404.foodtrack.domain.repositories.*
import com.team404.foodtrack.domain.services.CouponService
import com.team404.foodtrack.mockServer.MockServer
import org.koin.dsl.module

val appModule = module {
    // Mappers
    single { OrderHistoryMapper(get()) }
    single { MinifiedOrderMapper()}
    single { OrderMapper()}

    // Repositories
    single { MarketRepository(get()) }
    single { CouponRepository(get()) }
    single { MenuRepository(get()) }
    single { ProductRepository(get()) }
    single { OrderHistoryRepository(get()) }
    single { ConsumptionModeRepository(get()) }
    single { PaymentMethodRepository(get()) }

    // Services
    factory { CouponService(get(), get()) }
}

val mockServerModule = module {
    single { MockServer() }
}