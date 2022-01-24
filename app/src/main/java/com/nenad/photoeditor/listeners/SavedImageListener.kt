package com.nenad.photoeditor.listeners

import java.io.File

interface SavedImageListener {

    fun onImageClicked(file: File)
}