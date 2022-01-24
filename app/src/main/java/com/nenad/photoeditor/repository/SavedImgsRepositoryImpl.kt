package com.nenad.photoeditor.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import java.io.File

class SavedImgsRepositoryImpl(private val context: Context) : SavedImgsRepository {
    override suspend fun loadSavedImages(): List<Pair<File, Bitmap>>? {

        val savedImages = ArrayList<Pair<File, Bitmap>>()
        val dir = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "Saved Image"
        )
        dir.listFiles()?.let { data ->
            data.forEach { file->
                savedImages.add(Pair(file, getPreviewBitmap(file)))
            }
            return savedImages
        }?: return null

    }

  private fun getPreviewBitmap(file: File): Bitmap {
      val originalBitmap = BitmapFactory.decodeFile(file.absolutePath)
      val width = 150
      val height = ((originalBitmap.height * width) / originalBitmap.width)

      return Bitmap.createScaledBitmap(originalBitmap, width, height, false)


  }
}