package com.example.android.sunshine.core.data

import com.example.android.sunshine.core.data.Status.SUCCESS
import com.example.android.sunshine.core.data.Status.ERROR
import com.example.android.sunshine.core.data.Status.LOADING

/**
 * Generic class holding a value with its loading status
 */

data class Resource<out T>(val status:Status, val data: T?, val message: String?){
    companion object{
        fun <T> success (data: T?): Resource<T>{
            return Resource(SUCCESS, data, null)
        }

        fun <T> error (msg: String, data: T?): Resource<T>{
            return Resource(ERROR, data, msg)
        }

        fun <T> loading (data: T?): Resource<T>{
            return Resource(LOADING, data, null)
        }
    }
}