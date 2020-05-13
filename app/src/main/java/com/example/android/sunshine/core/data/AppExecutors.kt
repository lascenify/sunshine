package com.example.android.sunshine.core.data

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Global executor pools for the whole application
 * @param diskIO is a new thread apart from the ui thread where one task is executed at a time
 * @param networkIO Thought to execute network tasks.
 * Executor with 3 threads to assign tasks among them and execute together
 * @param mainThread is the main thread, in which Android handles the UI and its drawing
 */

@Singleton
open class AppExecutors(
    private val diskIO : Executor,
    private val networkIO : Executor,
    private val mainThread : Executor
) {
    @Inject
    constructor() : this(
        Executors.newSingleThreadExecutor(),
        Executors.newFixedThreadPool(3),
        MainThreadExecutor()
    )

    fun diskIO() : Executor = diskIO

    fun networkIO() : Executor = networkIO

    fun mainThread() : Executor = mainThread

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}