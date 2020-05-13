package com.example.android.sunshine.core.data

import retrofit2.Response

/*
sealed class ApiResponse <T> {
    companion object{
        /**
         * If it is created with an error, the result is an error response
         */
        fun <T> create(error: Throwable): ApiErrorResponse<T> =
            ApiErrorResponse(errorMessage = error.message ?: "unknown error")

        /**
         * If the result is a response, we need to check which kind of response.
         * The response might be a success, an empty or an error response
         */
        fun <T> create(response: Response<T>) : ApiResponse<T>{
            return if (response.isSuccessful){
                val body = response.body()
                // Check if the response is empty
                if (body == null || response.code() == 204){
                    ApiEmptyResponse()
                }
                else{
                    // return a success response
                    ApiSuccessResponse(
                        body = body,
                        links = response.headers()?.get("link")
                    )
                }
            }else{
                val msg = response.errorBody()?.string()
                val errorMsg = if (msg.isNullOrEmpty()){
                    response.message()
                } else { msg }
                ApiErrorResponse(errorMsg ?: "unknown error")
            }
        }
    }
}


*//*
class ApiEmptyResponse<T> : ApiResponse<T>()

class ApiSuccessResponse<T>(
    val body : T,
    val links: Map<String, String>
) : ApiResponse<T> (){
    constructor(body: T, linkHeader: String?) : this(
        body = body,
        links = linkHeader?.extractLinks() ?: emptyMap()
    )


    companion object{
        private fun String.extractLinks(): Map<String, String>
    }
}

data class ApiErrorResponse<T>(val errorMessage: String) : ApiResponse<T>()*/