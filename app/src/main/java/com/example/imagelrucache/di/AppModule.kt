package com.example.imagelrucache.di

import android.content.Context
import com.example.imagelrucache.cache.DogImageCache
import com.example.imagelrucache.cache.DogImageCacheImpl
import com.example.imagelrucache.cache.lrudisk.LruDiskCache
import com.example.imagelrucache.remote.DogApi
import com.example.imagelrucache.remote.repository.DogRepository
import com.example.imagelrucache.repository.DogRepositoryImpl
import com.example.imagelrucache.utils.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideDogApi() =
        Retrofit.Builder()
            .baseUrl(Constant.DOG_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DogApi::class.java)

    @Provides
    @Singleton
    fun provideDogRepository(dogApi: DogApi): DogRepository = DogRepositoryImpl(dogApi)

    @Provides
    @Singleton
    fun provideLRUCache(@ApplicationContext app: Context): LruDiskCache {
        return LruDiskCache(app)
    }

    @Provides
    @Singleton
    fun provideDogLruCache(imageLruCache:LruDiskCache):DogImageCache = DogImageCacheImpl(imageLruCache)


}