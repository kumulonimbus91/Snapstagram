package com.nenad.photoeditor.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.nenad.photoeditor.data.ImgFilter
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.*
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.lang.Exception

class EditImgRepoImpl(private val context: Context):EditRepository {
    override suspend fun prepareImgPrev(imageUri: Uri): Bitmap? {
        getInputStream(imageUri)?.let { inputStream ->
            val oriBitMap = BitmapFactory.decodeStream(inputStream)
            val width = context.resources.displayMetrics.widthPixels
            val height = ((oriBitMap.height * width)/oriBitMap.width)

            return Bitmap.createScaledBitmap(oriBitMap, width, height, false)

        } ?: return null
    }

    override suspend fun getImgFilter(image: Bitmap): List<ImgFilter> {
        val gpuImage = GPUImage(context).apply {
            setImage(image)
        }
        val imageFilters: ArrayList<ImgFilter> = ArrayList()

        //region::FiltersReviews
        GPUImageFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImgFilter(
                    name = "Normal",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        // Retro
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.2f, 0.0f,
                0.1f, 0.1f, 1.0f, 0.0f,
                1.0f, 0.0f, 0.0f, 1.0f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImgFilter(
                    name = "Retro",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        // Just
        GPUImageColorMatrixFilter(
            0.9f,
            floatArrayOf(
                0.4f, 0.6f, 0.5f, 0.0f,
                0.0f, 0.4f, 1.0f, 0.0f,
                0.05f, 0.1f, 0.4f, 0.4f,
                1.0f, 1.0f, 1.0f, 1.0f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImgFilter(
                    name = "Just",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        // Hume
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.25f, 0.0f, 0.2f, 0.0f,
                0.0f, 1.0f, 0.2f, 0.0f,
                0.0f, 0.3f, 1.0f, 0.3f,
                0.0f, 0.0f, 0.0f, 1.0f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImgFilter(
                    name = "Hume",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        // Desert
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0.6f, 0.4f, 0.2f, 0.05f,
                0.0f, 0.8f, 0.3f, 0.05f,
                0.3f, 0.3f, 0.5f, 0.08f,
                0.0f, 0.0f, 0.0f, 1.0f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImgFilter(
                    name = "Desert",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        // Old Times
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.0f, 0.05f, 0.0f, 0.0f,
                -0.2f, 1.1f, -0.2f, 0.11f,
                0.2f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImgFilter(
                    name = "Old Times",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        // Limo
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.0f, 0.0f, 0.08f, 0.0f,
                0.4f, 1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.1f,
                0.0f, 0.0f, 0.0f, 1.0f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImgFilter(
                    name = "Limo",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        // Sepia
        GPUImageSepiaToneFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImgFilter(
                    name = "Sepia",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        // Solar
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.5f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImgFilter(
                    name = "Solar",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        // Wole
        GPUImageSaturationFilter(2.0f).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImgFilter(
                    name = "Wole",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        // Neutron
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0f, 1f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0.6f, 1f, 0f,
                0f, 0f, 0f, 1f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImgFilter(
                    name = "Neutron",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        // Bright
        GPUImageRGBFilter(1.1f, 1.3f, 1.6f).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImgFilter(
                    name = "Bright",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        // Milk
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 0.64f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImgFilter(
                    name = "Milk",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        // BW
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 1.0f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImgFilter(
                    name = "BW",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        // Clue
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImgFilter(
                    name = "Clue",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        // Muli
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.0f, 0.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImgFilter(
                    name = "Muli",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        // Aero
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0f, 0f, 1f, 0f,
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 0f, 1f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImgFilter(
                    name = "Aero",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        // Classic
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0.763f, 0.0f, 0.2062f, 0f,
                0.0f, 0.9416f, 0.0f, 0f,
                0.1623f, 0.2614f, 0.8052f, 0f,
                0f, 0f, 0f, 1f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImgFilter(
                    name = "Classic",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        // Atom
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0.5162f, 0.3799f, 0.3247f, 0f,
                0.039f, 1.0f, 0f, 0f,
                -0.4773f, 0.461f, 1.0f, 0f,
                0f, 0f, 0f, 1f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImgFilter(
                    name = "Atom",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        // Mars
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0.0f, 0.0f, 0.5183f, 0.3183f,
                0.0f, 0.5497f, 0.5416f, 0f,
                0.5237f, 0.5269f, 0.0f, 0f,
                0f, 0f, 0f, 1f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImgFilter(
                    name = "Mars",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        // Yeli
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.0f, -0.3831f, 0.3883f, 0.0f,
                0.0f, 1.0f, 0.2f, 0f,
                -0.1961f, 0.0f, 1.0f, 0f,
                0f, 0f, 0f, 1f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImgFilter(
                    name = "Yeli",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }


        //endregion

        return imageFilters
    }

    override suspend fun saveFilteredImage(filteredBitmap: Bitmap): Uri? {
        return try {
            val mediaStorageDirectory = File (
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "Saved Image"
                    )
            if (!mediaStorageDirectory.exists()){
                mediaStorageDirectory.mkdirs()
            }
            val fileName = "IMG_${System.currentTimeMillis()}.jpg"
            val file = File (mediaStorageDirectory, fileName)
            saveFile(file, filteredBitmap)
            FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        } catch (e: Exception) {
            null
        }
    }
    private fun saveFile(file: File, bitmap: Bitmap) {
        with(FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
            flush()
            close()

        }
    }




    private fun getInputStream(uri: Uri): InputStream? {
        return context.contentResolver.openInputStream(uri)




    }


}