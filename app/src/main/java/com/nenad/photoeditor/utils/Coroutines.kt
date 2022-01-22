package com.nenad.photoeditor.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Coroutines {

    fun inputOutput(work: suspend(() -> Unit)) =
        CoroutineScope(Dispatchers.IO).launch { work()

        }
}