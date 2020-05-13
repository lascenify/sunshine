package com.example.android.sunshine.framework.sync

import android.content.Context
import android.content.Intent
import androidx.work.*
import com.example.android.sunshine.framework.Constants
import com.example.android.sunshine.framework.provider.WeatherContract
import com.example.android.sunshine.framework.work.SunshineSyncWorker
import java.util.concurrent.TimeUnit

object SunshineSyncUtils {
    const val TAG_OUTPUT = "OUTPUT"
    /** Sunshine will update each this amount of hours */
    const val SYNC_INTERVAL_HOURS = 4L
    /** Interval of syncing in seconds */
    val SYNC_INTERVAL_SECONDS = TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS).toInt()
    /** Flextime */
    val SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3


    private var sInitialized = false

    /**
     * Method used to schedule the sync task using a work manager
     */
    private fun schedulePeriodicWeatherSyncTask(context: Context){
        val workManager = WorkManager.getInstance(context)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequest = PeriodicWorkRequest.Builder(SunshineSyncWorker::class.java, SYNC_INTERVAL_HOURS, TimeUnit.HOURS)
            .addTag(TAG_OUTPUT)
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniquePeriodicWork(Constants.SUNSHINE_SYNC_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, workRequest)
        sInitialized = true
    }

    /**
     * Checks if there is a immediate sync required or schedules in a normal way.
     * It is called when the ForecastListFragment is created
     * An immediate sync is required where no data is returned from the content resolver
     */
    @Synchronized
    fun initialize(context: Context){
        if (sInitialized)
            return
        schedulePeriodicWeatherSyncTask(context)
        val thread = Thread(){
            Runnable {
                val forecastQueryUri = WeatherContract.WeatherEntry.CONTENT_URI
                val projectionColumns = arrayOf(WeatherContract.WeatherEntry._ID)
                val selectionStatement = WeatherContract.WeatherEntry.getSqlSelectForTodayOnwards()
                val cursor = context.contentResolver.query(
                    forecastQueryUri,
                    projectionColumns,
                    selectionStatement,
                    null,
                    null)
                if (cursor == null || cursor.count == 0){
                    startImmediateSync(context)
                }
                cursor?.close()
            }
        }
        thread.start()

    }

    /**
     * Method to sync weather data immediately starting an intent service
     */
    fun startImmediateSync (context: Context){
        val intentToSyncImmediately = Intent(context, SunshineSyncIntentService::class.java)
        context.startService(intentToSyncImmediately)
    }





}