package com.example.imagelrucache.ui.genratedogimage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagelrucache.cache.DogImageCache
import com.example.imagelrucache.remote.repository.DogRepository
import com.example.imagelrucache.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class GenerateDogViewModel @Inject constructor(
    private val dogRepository: DogRepository,
    private val imageCache: DogImageCache
) :
    ViewModel() {

    private val _dogImage = MutableLiveData<Resource<Bitmap>>()
    val dogImage: LiveData<Resource<Bitmap>> = _dogImage

    fun generateDogImage() {
        viewModelScope.launch {
            _dogImage.postValue(Resource.Loading())
            when (val dogImageResponse = dogRepository.getDogImage()) {
                is Resource.Success -> {
                    dogImageResponse.data?.let {
                        val imageBitmap = async { loadImageBitmap(URL(it.message)) }.await()
                        _dogImage.postValue(Resource.Success(imageBitmap))
                        imageCache.putImageToCache(it.message,imageBitmap)
                    } ?: run {
                        _dogImage.postValue(Resource.Error(message = "No Image Found"))
                    }
                }
                is Resource.Error -> {
                    _dogImage.postValue(Resource.Error(dogImageResponse.message))
                }
                else -> Unit
            }
        }
    }

    private suspend fun loadImageBitmap(url: URL): Bitmap = withContext(Dispatchers.IO) {
        BitmapFactory.decodeStream(url.openConnection().getInputStream())
    }

}