package com.example.imagelrucache.ui.recentdogimage

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagelrucache.cache.DogImageCache
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentDogViewModel @Inject constructor(private val imageCache: DogImageCache) :ViewModel() {

    private val _allDogs = MutableLiveData<List<Bitmap>>()
    val allDogs:LiveData<List<Bitmap>> = _allDogs

    init {
        getAllDogs()
    }

    private fun getAllDogs() = viewModelScope.launch {
        _allDogs.postValue(imageCache.getAllDogImage())
    }

    fun clearDogs(){
        _allDogs.postValue(emptyList())
        imageCache.clearDogImageCache()
    }

}