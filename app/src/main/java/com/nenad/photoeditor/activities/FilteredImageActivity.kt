package com.nenad.photoeditor.activities

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.view.drawToBitmap
import com.nenad.photoeditor.databinding.ActivityFilteredImageBinding
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class FilteredImageActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityFilteredImageBinding
    private lateinit var fileUri:Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityFilteredImageBinding.inflate(layoutInflater)
        val view = mBinding.root

        displayImgs()
        listeners()


        setContentView(view)
    }
    private fun displayImgs() {
        intent.getParcelableExtra<Uri>(EditActivity.KEY_FILTERED_IMAGE_URI)?.let { imageUri ->
            fileUri = imageUri
            mBinding.imageFilteredImg.setImageURI(imageUri)

        }
    }

    private fun listeners() {
        mBinding.fabShare.setOnClickListener {
            with(Intent(Intent.ACTION_SEND)) {
                putExtra(Intent.EXTRA_STREAM, fileUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                type = "image/*"
                startActivity(this)
            }
        }
        mBinding.imageBack.setOnClickListener {
            onBackPressed()
        }

        mBinding.fabSave.setOnClickListener {


            val bitmap = mBinding.imageFilteredImg.drawToBitmap()



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) saveImageInQ(bitmap)
            else saveTheImageLegacyStyle(bitmap)

            Toast.makeText(this, "Image saved in gallery", Toast.LENGTH_SHORT).show()


















        }

    }
    fun saveImageInQ(bitmap: Bitmap):Uri {
        val filename = "IMG_${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        var imageUri: Uri? = null
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.Video.Media.IS_PENDING, 1)
        }

        //use application context to get contentResolver
        val contentResolver = application.contentResolver

        contentResolver.also { resolver ->
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }
        }

        fos?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 70, it) }

        contentValues.clear()
        contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
        contentResolver.update(imageUri!!, contentValues, null, null)

        return imageUri!!
    }


    //Make sure to call this function on a worker thread, else it will block main thread
    fun saveTheImageLegacyStyle(bitmap: Bitmap){
        val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File(imagesDir, System.currentTimeMillis().toString())
        val fos = FileOutputStream(image)
        fos?.use {bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)}
    }
}