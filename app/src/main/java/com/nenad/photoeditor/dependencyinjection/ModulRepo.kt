package com.nenad.photoeditor.dependencyinjection

import android.content.pm.ModuleInfo
import com.nenad.photoeditor.repository.EditImgRepoImpl
import com.nenad.photoeditor.repository.EditRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    factory<EditRepository> {
        EditImgRepoImpl(androidContext()) }
}