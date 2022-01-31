package com.nenad.photoeditor.activities

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.ExifInterface.*
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
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
import com.swein.easypermissionmanager.EasyPermissionManager
import jp.co.cyberagent.android.gpuimage.GPUImage
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.*
import java.util.*
import android.graphics.Bitmap as Bitmap1


class EditActivity : AppCompatActivity(), ImgFilterListener {


    companion object {
        const val KEY_FILTERED_IMAGE_URI = "filtered"
        private const val STORAGE_PERMISSION_CODE =
            1 // its about which permission value is, could be camera permission, location per.etc.
        private const val GALLERY = 2
        val IMAGE_CAPTURE_CODE = 654
    }




    private lateinit var mBinding: ActivityEditBinding

    private val MAX_HEIGHT = 1024
    private val MAX_WIDTH = 1024

    private val viewModel: EditImgViewModel by viewModel()

    private lateinit var gpuImg: GPUImage

    private var cameraUri: Uri? = null



    private val easyPermissionManager = EasyPermissionManager(this)



    private lateinit var originalBM: Bitmap1
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
                    FilteredImageActivity::class.java
                ).also { filteredImgIntent ->
                    filteredImgIntent.putExtra(KEY_FILTERED_IMAGE_URI, savedImageUri)
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
        mBinding.imgCam.setOnClickListener {
            openCamera()
        }





    }

    private fun openCamera() {


        easyPermissionManager.requestPermission(
            "permission",
            "permissions are necessary", "setting",
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "New Picture")
            values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
            cameraUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri)
            startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == Activity.RESULT_OK) {

            viewModel.prepareImagePreview(cameraUri!!)














        }
    }







}
