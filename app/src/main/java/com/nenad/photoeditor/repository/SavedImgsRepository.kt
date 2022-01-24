package com.nenad.photoeditor.repository

import android.graphics.Bitmap
import java.io.File

interface SavedImgsRepository {

    suspend fun loadSavedImages(): List<Pair<File, Bitmap>>?
}