package com.sixt.sixtcar.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.sixt.sixtcar.model.data.Vehicle
import com.sixt.sixtcar.model.repository.VehicleRepo
import com.sixt.sixtcar.viewmodel.base.BaseViewModel

class VehicleSharedViewModel(application: Application) :
    BaseViewModel<VehicleRepo>(application, VehicleRepo()) {

    private val repo = getRepository()
    val vehicleResponse: MutableLiveData<List<Vehicle>> = repo.vehicleResponse

    fun fetchVehicleDetails(context: Context) {
        repo.getVehicleDetails(context)
    }
}