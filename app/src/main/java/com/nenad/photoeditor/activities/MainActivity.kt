package com.nenad.photoeditor.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.nenad.photoeditor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding

    companion object {
        private const val REQ_CODE_IMAGE_PICKER = 1
        const val KEY_IMAGE = "imageUri"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)

        val view = mBinding.root
        listeners()




        setContentView(view)
    }


    private fun listeners() {
        mBinding.btnEdit.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
        }
        mBinding.btnSave.setOnClickListener {
           Intent(applicationContext, SavedImgActivity::class.java).also {
               startActivity(it)
           }
        }
    }


}