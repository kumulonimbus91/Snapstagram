package com.nenad.photoeditor.activities

import android.Manifest
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider.getUriForFile
import androidx.lifecycle.MutableLiveData
import com.nenad.photoeditor.data.ImgFilter
import com.nenad.photoeditor.databinding.ActivityEditBinding
import com.nenad.photoeditor.listeners.ImgFilterListener
import com.nenad.photoeditor.rvadapters.FiltersAdapter
import com.nenad.photoeditor.utils.Coroutines
import com.nenad.photoeditor.utils.displayToast
import com.nenad.photoeditor.utils.show
import com.nenad.photoeditor.viewModels.EditImgViewModel
import com.swein.easypermissionmanager.EasyPermissionManager
import jp.co.cyberagent.android.gpuimage.GPUImage
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.io.File
import android.graphics.Bitmap as Bitmap1


class EditActivity : AppCompatActivity(), ImgFilterListener {
    private lateinit var mBinding: ActivityEditBinding

    private val viewModel: EditImgViewModel by viewModel()

    private lateinit var gpuImg: GPUImage

    private lateinit  var originalBM: Bitmap1
    private var filteredBM: MutableLiveData<Bitmap1> = MutableLiveData<Bitmap1>()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityEditBinding.inflate(layoutInflater)
        val view = mBinding.root



        listeners()
        prepareImgPreview()
        observers()








        setContentView(view)
    }


    fun observers() {
        viewModel.imgPreviewUiState.observe(this, {
            val dataState = it ?: return@observe
            mBinding.progressPreview.visibility =
                if (dataState.isLoading) View.VISIBLE else View.GONE

            dataState.bitmap?.let { bitmap ->

                //for the first time
                originalBM = bitmap
                filteredBM.value = bitmap

                with(originalBM) {
                    gpuImg.setImage(this)
                    mBinding.imgPreview.show()
                    viewModel.loadImgFilters(this)
                }


            } ?: kotlin.run {
                dataState.error?.let { error ->
                    displayToast(error)

                }
            }

        })
        viewModel.imageFiltersUIState.observe(this, {
            val imgFiltersDataState = it ?: return@observe
            mBinding.editProgress.visibility =
                if (imgFiltersDataState.isLoading) View.VISIBLE else View.GONE

            imgFiltersDataState.filters?.let {
                FiltersAdapter(it, this).also {
                    mBinding.rvFilters.adapter = it
                }
            } ?: kotlin.run {
                imgFiltersDataState.error.let { error ->
                    displayToast(error)
                }

            }

        })
        filteredBM.observe(this, {
            mBinding.imgPreview.setImageBitmap(it)
        })
    }

    private fun prepareImgPreview() {
        gpuImg = GPUImage(applicationContext)

        val loadImg =
            registerForActivityResult(ActivityResultContracts.GetContent(), ActivityResultCallback {
                viewModel.prepareImagePreview(it)

            })
        mBinding.imgGallery.setOnClickListener {
            loadImg.launch("image/*")
        }




    }

    override fun onFilterClicked(imgFilter: ImgFilter) {
        with(imgFilter) {
            with(gpuImg) {
                setFilter(filter)
                filteredBM.value = bitmapWithFilterApplied
            }
        }

    }

    private fun listeners() {
        mBinding.backButton.setOnClickListener {
            onBackPressed()
        }
    }



}
