package com.nenad.photoeditor.rvadapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nenad.photoeditor.databinding.ItemContainerSavedImgBinding
import com.nenad.photoeditor.listeners.SavedImageListener
import java.io.File

class SavedImagesAdapter (private val savedImages: List<Pair <File, Bitmap>>, private val savedImageListener: SavedImageListener) :
    RecyclerView.Adapter<SavedImagesAdapter.ViewHolder>() {




    inner class ViewHolder(val binding: ItemContainerSavedImgBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         val binding = ItemContainerSavedImgBinding.inflate(
             LayoutInflater.from(parent.context), parent, false
         )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      with(holder) {
          with(savedImages[position]) {
              binding.imageSaved.setImageBitmap(second) // second in Pair queque(bitmap)
              binding.imageSaved.setOnClickListener {
                  savedImageListener.onImageClicked(first)
              }
          }
      }
    }

    override fun getItemCount() = savedImages.size
}