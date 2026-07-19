package com.cryptotracker.network.di

import com.cryptotracker.network.source.CoinDataSource
import com.cryptotracker.network.source.CoinDataSourceImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {
    @Binds
    fun bindsDataSource(dataSource: CoinDataSourceImp): CoinDataSource
}
