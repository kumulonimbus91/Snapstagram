package com.nenad.photoeditor.listeners

import com.nenad.photoeditor.data.ImgFilter

interface ImgFilterListener {

    fun onFilterClicked(imgFilter: ImgFilter)
}