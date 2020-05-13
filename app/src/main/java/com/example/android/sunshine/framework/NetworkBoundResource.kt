package com.example.android.sunshine.framework

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.android.sunshine.core.data.AppExecutors
import com.example.android.sunshine.core.data.Resource
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


abstract class NetworkBoundResource <ResultType, RequestType>
@MainThread constructor(private val appExecutors: AppExecutors){

    /**
     * MediatorLiveData is a type of LiveData that observes other LiveData objects
     * and react on onChanged events from them
     */
    private val result = MediatorLiveData<Resource<ResultType>>()

    private var mDisposable: Disposable? = null

    private lateinit var dbSource: LiveData<ResultType?>

    init {
        // Initialize loading
        result.value = Resource.loading(null)
        // Load the data from db
        appExecutors.diskIO().execute {
            dbSource = loadFromDb()
            // the mediator listen to changes on the db source
            appExecutors.mainThread().execute {
                result.addSource(dbSource) { data ->
                    // when a change occurs, the source gets removed
                    result.removeSource(dbSource)
                    // we check if we should fetch data from network
                    if (shouldFetch(data)) {
                        fetchFromNetwork(dbSource)
                    } else {
                        // if not, we re-attach the db source and set the value to success
                        result.addSource(dbSource) { newData ->
                            setValue(Resource.success(newData))
                        }
                    }
                }
            }
        }
    }

    @MainThread
    private fun setValue (newValue : Resource<ResultType>){
        if (result.value != newValue)
            result.value = newValue
    }


    private fun fetchFromNetwork(dbSource: LiveData<ResultType?>) {
        val apiResponse = createCall()

        result.addSource(dbSource){
            result.value = Resource.loading(it)
        }

        result.addSource(apiResponse){ response ->
            result.removeSource(dbSource)
            result.removeSource(apiResponse)

            response?.apply {
                if (status.isSuccessful()){
                    appExecutors.diskIO().execute {
                        processResponse(this)?.let { requestType ->
                            saveCallResult(requestType)
                        }

                        appExecutors.mainThread().execute {
                            result.addSource(loadFromDb()){ newData ->
                                setValue(Resource.success(newData))
                            }
                        }
                    }
                }

                else{
                    onFetchFailed()
                    result.addSource(dbSource){
                        result.value = Resource.error(message.toString(), it)
                    }
                }
            }
        }
    }


    protected open fun onFetchFailed(){}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @WorkerThread
    protected open fun processResponse(response: Resource<RequestType>) = response.data

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType?>

    @MainThread
    protected abstract fun createCall(): LiveData<Resource<RequestType>>


}