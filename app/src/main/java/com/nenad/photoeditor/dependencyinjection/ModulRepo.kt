package com.nenad.photoeditor.dependencyinjection

import android.content.pm.ModuleInfo
import com.nenad.photoeditor.repository.EditImgRepoImpl
import com.nenad.photoeditor.repository.EditRepository
import com.nenad.photoeditor.repository.SavedImgsRepository
import com.nenad.photoeditor.repository.SavedImgsRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    factory<EditRepository> {
        EditImgRepoImpl(androidContext()) }

    factory<SavedImgsRepository> {SavedImgsRepositoryImpl(androidContext())}
}