package com.nenad.photoeditor.dependencyinjection

import com.nenad.photoeditor.viewModels.EditImgViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel {EditImgViewModel(repository = get())}
}