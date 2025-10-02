package com.bitpanda.livechallenge.data.di

import com.bitpanda.livechallenge.data.repository.CoinRepositoryImp
import com.bitpanda.livechallenge.domain.repository.CoinRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindCoinRepository(coinRepositoryImp: CoinRepositoryImp): CoinRepository
}
