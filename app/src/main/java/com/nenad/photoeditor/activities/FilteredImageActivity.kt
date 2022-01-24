package com.nenad.photoeditor.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nenad.photoeditor.databinding.ActivityFilteredImageBinding

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

    }
}