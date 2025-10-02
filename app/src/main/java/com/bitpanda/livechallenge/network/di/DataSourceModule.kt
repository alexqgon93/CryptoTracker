package com.bitpanda.livechallenge.network.di

import com.bitpanda.livechallenge.network.source.CoinDataSource
import com.bitpanda.livechallenge.network.source.CoinDataSourceImp
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
