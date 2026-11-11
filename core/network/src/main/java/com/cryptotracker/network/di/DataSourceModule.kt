package com.cryptotracker.network.di

import com.cryptotracker.network.source.CoinDataSource
import com.cryptotracker.network.source.CoinDataSourceImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/** Exposes [CoinDataSourceImp] to consumers through the [CoinDataSource] abstraction. */
@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {
    @Binds
    fun bindsDataSource(implementation: CoinDataSourceImp): CoinDataSource
}
