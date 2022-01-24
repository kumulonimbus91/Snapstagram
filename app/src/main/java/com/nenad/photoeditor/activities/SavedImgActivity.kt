package com.nenad.photoeditor.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.FileProvider
import com.nenad.photoeditor.databinding.ActivitySavedImgBinding
import com.nenad.photoeditor.listeners.SavedImageListener
import com.nenad.photoeditor.rvadapters.SavedImagesAdapter
import com.nenad.photoeditor.utils.displayToast
import com.nenad.photoeditor.viewModels.SavedImagesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.lang.Exception


class SavedImgActivity : AppCompatActivity(), SavedImageListener {

    private lateinit var mBinding: ActivitySavedImgBinding
    private val viewModel: SavedImagesViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySavedImgBinding.inflate(layoutInflater)

        val view = mBinding.root

        observers()
        listeners()
        viewModel.loadSavedImgs()





        setContentView(view)
    }

    private fun observers() {
        viewModel.savedImagesUiState.observe(this, {
            val dataState = it ?: return@observe
            mBinding.savedImgsprogress.visibility =
                if (dataState.isLoading) View.VISIBLE else View.GONE

            dataState.savedImages?.let { savedImages ->
               SavedImagesAdapter(savedImages, this).also { adapter ->
                   with(mBinding.savedRv) {
                       this.adapter = adapter
                       visibility = View.VISIBLE
                   }

               }

            } ?: run {
                dataState.error?.let { error ->
                    displayToast(error)

                }
            }
        })
    }
    private fun listeners() {
        mBinding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onImageClicked(file: File) {
        val fileUri = FileProvider.getUriForFile(applicationContext, "${packageName}.provider",
        file)
        Intent(applicationContext, FilteredImageActivity::class.java).also {
            filteredImageIntent ->
            filteredImageIntent.putExtra(EditActivity.KEY_FILTERED_IMAGE_URI, fileUri)
            startActivity(filteredImageIntent)
        }
    }
}