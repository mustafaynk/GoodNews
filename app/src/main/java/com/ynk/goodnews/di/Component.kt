package com.ynk.goodnews.di

import com.ynk.goodnews.repositories.MainRepositoryImpl
import dagger.Module
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@EntryPoint
interface Component {
    fun inject(mainRepository: MainRepositoryImpl)
}