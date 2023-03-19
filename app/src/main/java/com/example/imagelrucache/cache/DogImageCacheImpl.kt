package com.example.imagelrucache.cache

import android.graphics.Bitmap
import com.example.imagelrucache.cache.lrudisk.LruDiskCache
import javax.inject.Inject

class DogImageCacheImpl @Inject constructor(private val lruDiskCache: LruDiskCache):DogImageCache {

    override fun putImageToCache(url: String, image: Bitmap) {
        lruDiskCache.putImage(url,image)
    }

    override suspend fun getAllDogImage(): List<Bitmap> {
        return lruDiskCache.getAllImages()
    }

    override fun clearDogImageCache() {
        return lruDiskCache.clearCache()
    }
}