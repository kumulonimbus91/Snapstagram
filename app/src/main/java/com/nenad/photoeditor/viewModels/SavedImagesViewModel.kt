package com.nenad.photoeditor.viewModels

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nenad.photoeditor.repository.SavedImgsRepository
import com.nenad.photoeditor.utils.Coroutines
import java.io.File

class SavedImagesViewModel(private val savedImagesRepository: SavedImgsRepository) : ViewModel() {


    private val savedImagesDataState = MutableLiveData<SavedImgsDataState>()
    val savedImagesUiState: LiveData<SavedImgsDataState> get() = savedImagesDataState

    fun loadSavedImgs() {
        Coroutines.inputOutput {
            runCatching {
                emitSavedImgsUiState(isLoading = true)
                savedImagesRepository.loadSavedImages()
            }.onSuccess { savedImages ->
                if (savedImages.isNullOrEmpty()) {
                    emitSavedImgsUiState(error = "No image found")
                } else {
                    emitSavedImgsUiState(savedImages = savedImages)

                }

            }.onFailure {
                emitSavedImgsUiState(error = it.message.toString())
            }
        }
    }

    private fun emitSavedImgsUiState (
        isLoading: Boolean = false,
        savedImages: List<Pair<File, Bitmap>>? = null,
        error: String? = null
    ) {
        val dataState = SavedImgsDataState(isLoading, savedImages, error)
        savedImagesDataState.postValue(dataState)
    }


    data class SavedImgsDataState (
        val isLoading: Boolean,
        val savedImages: List<Pair<File, Bitmap>>?,
        val error: String?
            )

}