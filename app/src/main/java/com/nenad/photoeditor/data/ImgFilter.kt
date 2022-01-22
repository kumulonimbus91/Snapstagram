package com.nenad.photoeditor.data

import android.graphics.Bitmap
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter

data class ImgFilter (
    val name: String,
    val filter: GPUImageFilter,
    val filterPreview: Bitmap
        )