package com.nenad.photoeditor.viewModels

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nenad.photoeditor.data.ImgFilter
import com.nenad.photoeditor.repository.EditRepository
import com.nenad.photoeditor.utils.Coroutines

class EditImgViewModel(private val repository: EditRepository) : ViewModel() {

    //region::ImageReviews


    private val imagePreviewDataState = MutableLiveData<ImagePreviewDataState>()
    val imgPreviewUiState: LiveData<ImagePreviewDataState> get() = imagePreviewDataState


    fun prepareImagePreview(imageUri: Uri) {
        Coroutines.inputOutput {
            runCatching {
                emitImgPreviewState(isLoading = true)
                repository.prepareImgPrev(imageUri)
            }.onSuccess { bitmap ->
                if (bitmap != null) {
                    emitImgPreviewState(bitmap = bitmap)
                } else {
                    emitImgPreviewState(error = "Unable to perform operation")
                }

            }.onFailure {
                emitImgPreviewState(error = it.message.toString())
            }
        }

    }





    private fun emitImgPreviewState(
        isLoading: Boolean = false,
        bitmap: Bitmap? = null,
        error: String? = null
    ) {
        val dataState = ImagePreviewDataState(isLoading, bitmap, error)
        imagePreviewDataState.postValue(dataState)
    }


    data class ImagePreviewDataState(
        val isLoading: Boolean,
        val bitmap: Bitmap?,
        val error: String?

        )

    //endregion

    //region:: Load Filters
    private val imageFiltersDataState = MutableLiveData<ImageFiltersDataState>()
    val imageFiltersUIState: LiveData<ImageFiltersDataState> get() = imageFiltersDataState

    fun loadImgFilters(originalImage: Bitmap) {
        Coroutines.inputOutput {
            runCatching {
                emitImgFiltersUiState(true)
                repository.getImgFilter(getOriginalImage(originalImage))
            }.onSuccess { imageFilters ->
                emitImgFiltersUiState(imageFilters = imageFilters)

            }.onFailure {
                emitImgFiltersUiState(error = it.message.toString())
            }
        }
    }



    private fun getOriginalImage(originalImage: Bitmap) : Bitmap {
        return runCatching {
            val oriWidth = 150
            val oriHeight = originalImage.height * oriWidth / originalImage.height

            Bitmap.createScaledBitmap(originalImage,oriWidth,oriHeight, false)

        }.getOrDefault(originalImage)
    }


    private fun emitImgFiltersUiState(
        isLoading: Boolean = false,
        imageFilters: List<ImgFilter>? = null,
        error: String? = null
    ) {

        val dataState = imageFilters?.let { ImageFiltersDataState(isLoading, it, error) }
        imageFiltersDataState.postValue(dataState)

    }

    data class ImageFiltersDataState(
        val isLoading: Boolean,
        val filters: List<ImgFilter>?,
        val error: String?

    )

    //endregion

    //region Save Images




    private val savedFilterImageDataState = MutableLiveData<SavedFilterImageDataState>()

    val saveFilteredImageUiState: LiveData<SavedFilterImageDataState> get() = savedFilterImageDataState

    fun saveFilteredImg(filteredBitmap: Bitmap) {
        Coroutines.inputOutput {
            runCatching {
                emitSavedState(isLoading = true)
                repository.saveFilteredImage(filteredBitmap)
            }.onSuccess { savedImgUri ->
                emitSavedState(uri = savedImgUri)

            }.onFailure { error ->
               emitSavedState(error = error.message.toString())
            }
        }

    }

    private fun emitSavedState(isLoading: Boolean = false,
    uri: Uri? = null,
    error: String? = null) {
        val dataState = SavedFilterImageDataState(isLoading, uri, error)
        savedFilterImageDataState.postValue(dataState)

    }



    data class SavedFilterImageDataState(val isLoading: Boolean,
                                         val uri:Uri?,
                                         val error: String?)
    //endregion
}

/*
We use MutableLiveData when we want to make its value writable or can be change anytime.

We use LiveData when we just want to read and listen to any updates made by MutableLiveData.
 */