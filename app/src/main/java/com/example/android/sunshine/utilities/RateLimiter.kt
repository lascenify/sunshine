package com.example.android.sunshine.utilities

import android.os.SystemClock
import androidx.collection.ArrayMap
import androidx.collection.arrayMapOf
import java.util.concurrent.TimeUnit

/**
 * This class is used within the repository, in order to check whether the data should be extracted
 * from the API or not.
 * @param timeOut: if the time elapsed since last fetched is greather than this timeOut,
 * shouldFetch will return true
 * @param timeUnit: time unit in which the timeOut is given
 * @param KEY is the key that will be checked to retrieve the timestamps
 */
class RateLimiter <in KEY> (timeOut: Int, timeUnit: TimeUnit){
    private val timestamps = ArrayMap<KEY, Long>()
    private val timeout = timeUnit.toMillis(timeOut.toLong())

    /**
     * Check if we should fetch data in these two cases:
     *      - there is no timestamps with that key
     *      - the time elapsed since last fetched is greater than the timeout
     */
    @Synchronized
    fun shouldFetch(key: KEY): Boolean{
        val lastFetched = timestamps[key]
        val now = now()
        if (lastFetched == null){
            timestamps[key] = now
            return true
        }
        if (now - lastFetched > timeout){
            timestamps[key] = now
            return true
        }
        return false
    }

    private fun now() = SystemClock.uptimeMillis()

    @Synchronized
    fun reset(key: KEY){
        timestamps.remove(key)
    }

}
