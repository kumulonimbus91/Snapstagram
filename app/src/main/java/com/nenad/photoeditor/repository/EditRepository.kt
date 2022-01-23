package com.nenad.photoeditor.repository

import android.graphics.Bitmap
import android.net.Uri
import com.nenad.photoeditor.data.ImgFilter


interface EditRepository {
    suspend fun prepareImgPrev(imageUri: Uri): Bitmap?

    suspend fun getImgFilter(image: Bitmap): List<ImgFilter>

    suspend fun saveFilteredImage(filteredBitmap: Bitmap): Uri?
}