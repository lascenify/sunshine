package com.example.android.sunshine.framework.work

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.android.sunshine.framework.sync.SunshineSyncTask

class SunshineSyncWorker(val context: Context, workerParams: WorkerParameters) : Worker(context,
    workerParams) {
    companion object {
        const val KEY_WEATHER_DATA = "KEY_WEATHER_DATA"
    }
    override fun doWork(): Result {
        return try {
            val result =
                SunshineSyncTask.syncWeather(
                    context = context
                )
            //makeStatusNotification("Updating weather data", context)
            val outputData = workDataOf(KEY_WEATHER_DATA to result)
            Result.success(outputData)
        } catch (throwable: Throwable) {
            Log.e("ERROR_SYNC", "Error executing syncing", throwable)
            Result.failure()
        }
    }

}