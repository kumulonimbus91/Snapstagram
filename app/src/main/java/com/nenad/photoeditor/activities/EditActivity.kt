package com.nenad.photoeditor.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.nenad.photoeditor.data.ImgFilter
import com.nenad.photoeditor.databinding.ActivityEditBinding
import com.nenad.photoeditor.listeners.ImgFilterListener
import com.nenad.photoeditor.rvadapters.FiltersAdapter
import com.nenad.photoeditor.utils.displayToast
import com.nenad.photoeditor.utils.show
import com.nenad.photoeditor.viewModels.EditImgViewModel
import jp.co.cyberagent.android.gpuimage.GPUImage
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.graphics.Bitmap as Bitmap1


class EditActivity : AppCompatActivity(), ImgFilterListener {


    companion object {
        const val KEY_FILTERED_IMAGE_URI = "filtered"
    }




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
        viewModel.saveFilteredImageUiState.observe(this, {
            val savedFilterImageDataState = it ?: return@observe
            if (savedFilterImageDataState.isLoading) {
                mBinding.imgDone.visibility = View.GONE
                mBinding.savingImgProgress.visibility = View.VISIBLE
            } else {
                mBinding.imgDone.visibility = View.VISIBLE
                mBinding.savingImgProgress.visibility = View.GONE
            }
            savedFilterImageDataState.uri?.let { savedImageUri ->
                Intent(
                    applicationContext,
                    FilteredImageActivity::class.java).also { filteredImgIntent ->
                    filteredImgIntent.putExtra(KEY_FILTERED_IMAGE_URI,savedImageUri)
                    startActivity(filteredImgIntent)

                }
            } ?: kotlin.run {
                savedFilterImageDataState.error?.let { error ->
                    displayToast(error)

                }
            }
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
        /*
        The following code will display original image on long press so we can see the difference between original and edited img
         */
        mBinding.imgPreview.setOnLongClickListener {
            mBinding.imgPreview.setImageBitmap(originalBM)
            return@setOnLongClickListener false
        }

        mBinding.imgPreview.setOnClickListener {
            mBinding.imgPreview.setImageBitmap(filteredBM.value)
        }
        mBinding.imgDone.setOnClickListener {
            filteredBM.value?.let {
                viewModel.saveFilteredImg(it)
            }
        }

    }



}
