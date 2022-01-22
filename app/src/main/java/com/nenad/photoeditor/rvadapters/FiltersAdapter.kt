package com.nenad.photoeditor.rvadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.nenad.photoeditor.data.ImgFilter
import com.nenad.photoeditor.databinding.ContainerFilterBinding
import com.nenad.photoeditor.listeners.ImgFilterListener

class FiltersAdapter (private val imgFilters: List<ImgFilter>,
private val imgFilterListener: ImgFilterListener): RecyclerView.Adapter<FiltersAdapter.ViewHolder> () {


    inner class ViewHolder(val mBinding: ContainerFilterBinding) : RecyclerView.ViewHolder(mBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val mBinding = ContainerFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(mBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(imgFilters[position]) {
                mBinding.filter.setImageBitmap(filterPreview)
                mBinding.tvFilterName.text = name
                mBinding.root.setOnClickListener {
                   imgFilterListener.onFilterClicked(this)
                }
            }
        }

    }

    override fun getItemCount(): Int {
      return imgFilters.size
    }
}