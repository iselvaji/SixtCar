package com.sixt.sixtcar.model.remote

import okhttp3.Headers
import okhttp3.Request
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

abstract class ApiCallback<T> : Callback<T> {

    override fun onFailure(call: Call<T>, t: Throwable) {
        checkAndPrintException(call, t)
        Timber.i("API Request Failure [%s] : %s | %s", (call.request() as Request).url(), t.message, t.localizedMessage)
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        val errorBody = response.errorBody()?.string()

        if (response.isSuccessful) onSuccess(response.body(), response.headers())
        else {
            Timber.i("API Request Failure : $errorBody")
            if (response.body() != null) {
                Timber.i("API Request Failure : ${response.body()}")
            } else {
                onError(
                    code = response.code(),
                    response = "error", // we can parse the JSON
                    headers = response.headers()
                )
            }
        }
    }

    protected abstract fun onError(code: Int, response: Any?, headers: Headers?)

    protected abstract fun onSuccess(response: T?, headers: Headers)

    private fun checkAndPrintException(call: Call<T>, t: Throwable) {
        Timber.e(t)
    }
}
