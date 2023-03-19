package com.example.imagelrucache.cache

import android.graphics.Bitmap

interface DogImageCache {

    fun putImageToCache(url:String,image:Bitmap)
    suspend fun getAllDogImage():List<Bitmap>
    fun clearDogImageCache()

}