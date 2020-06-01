package com.example.android.sunshine.framework

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.android.sunshine.core.data.Resource

/**
 * This is a type of NetworkBoundResource created for retrieving the user
 * data from the api when the user signs in with Google.
 * As we don't need to save it on local db, but save its token and other data
 * in shared preferences, we make this resource with an status to make it simpler.
 */
abstract class RemoteBoundResource<RequestType> {

    private val result = MediatorLiveData<Resource<RequestType>>()

    init {
        setValue(Resource.loading(null))
        fetchFromNetwork()
    }

    @MainThread
    private fun setValue(newValue: Resource<RequestType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork() {
        val apiResponse = createCall()

        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)

            response.apply {
                if (status.isSuccessful()){
                    setValue(Resource.success(processResponse(response)))
                }
                else if (status.isError()){
                    onFetchFailed()
                    setValue(Resource.error(response.message.toString(), null))
                }
            }

        }
    }

    protected fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<RequestType>>

    @WorkerThread
    protected open fun processResponse(response: Resource<RequestType>) = response.data

    @MainThread
    protected abstract fun createCall(): LiveData<Resource<RequestType>>
}