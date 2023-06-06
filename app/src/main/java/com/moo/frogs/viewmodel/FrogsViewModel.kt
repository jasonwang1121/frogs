package com.moo.frogs.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moo.frogs.model.Photo
import com.moo.frogs.model.RetrofitInstance
import com.moo.frogs.model.UnsplashService
import com.moo.frogs.BuildConfig
import kotlinx.coroutines.launch

class FrogsViewModel: ViewModel() {
    private val unsplashApi: UnsplashService = RetrofitInstance.instance.create(UnsplashService::class.java)

    val images = mutableStateOf<List<Photo>>(emptyList())
    val isLoading = mutableStateOf(false)
    val currentImage = mutableStateOf("")
    var num = 0
    var count = 0

    init {
        getImages()
    }

    private fun getImages() = viewModelScope.launch {
        try {
            isLoading.value = true
            val response = unsplashApi.getPhotos(
                query = "frogs",
                authHeader = "Client-ID ${BuildConfig.UNSPLASH_ACCESS_KEY}"
            )
            images.value = images.value.plus(response)
            println("Another one!")
            getNextImage()
        } catch (e: Exception) {
            e.localizedMessage
        } finally {
            isLoading.value = false
        }

    }

    fun getNextImage() {
        currentImage.value = images.value[num].urls.regular
        num++
        count++

        if (count == 25) {
            count = 0
            getImages()
        }
    }
}