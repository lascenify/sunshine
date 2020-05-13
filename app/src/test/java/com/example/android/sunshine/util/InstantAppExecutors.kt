package com.example.android.sunshine.util

import com.example.android.sunshine.core.data.AppExecutors
import java.util.concurrent.Executor

class InstantAppExecutors : AppExecutors(instant, instant, instant){
    companion object{
        private val instant = Executor{it.run()}
    }
}