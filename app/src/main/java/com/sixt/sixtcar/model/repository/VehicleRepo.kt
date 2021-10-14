package com.sixt.sixtcar.model.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.sixt.sixtcar.R
import com.sixt.sixtcar.model.data.Vehicle
import com.sixt.sixtcar.model.remote.ApiCallback
import com.sixt.sixtcar.model.remote.ApiClientHelper
import com.sixt.sixtcar.utils.NetworkProp

import okhttp3.Headers
import timber.log.Timber

class VehicleRepo : BaseRepo() {

    var vehicleResponse: MutableLiveData<List<Vehicle>> = MutableLiveData()

    fun getVehicleDetails(context: Context) {
        Timber.i("getVehicleDetails.. isNetworkConnected - " +  NetworkProp.isNetworkConnected)

        if (!NetworkProp.isNetworkConnected) {
            error.value = context.getString(R.string.error_network)
            return
        }

        setLoading(context.getString(R.string.loading))

        val call = ApiClientHelper.getInterface()?.getVehicleList()

        call?.enqueue(object : ApiCallback<List<Vehicle>>() {
            override fun onError(code: Int, response: Any?, headers: Headers?) {
                setLoading()
                Timber.i("Vehicle Response fetch error " + response.toString())
                error.value = context.getString(R.string.error_loading_data)
                vehicleResponse.value = null
            }

            override fun onSuccess(response: List<Vehicle>?, headers: Headers) {
                Timber.i("Vehicle Response fetch success :" + response.toString())
                setLoading()
                vehicleResponse.value = response
            }
        })
    }

}
