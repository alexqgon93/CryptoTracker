package com.cryptotracker.data.di

import com.cryptotracker.data.repository.CoinRepositoryImp
import com.cryptotracker.domain.repository.CoinRepository
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
