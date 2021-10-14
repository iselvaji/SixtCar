package com.sixt.sixtcar.model.remote

import com.sixt.sixtcar.model.data.Vehicle
import com.sixt.sixtcar.utils.Constants.Companion.URL_CARS
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET(URL_CARS)
    fun getVehicleList(): Call<List<Vehicle>>
}