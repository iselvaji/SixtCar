package com.sixt.sixtcar.model.repository

import androidx.lifecycle.MutableLiveData
import com.sixt.sixtcar.utils.SingleLiveEvent

open class BaseRepo {

    var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    var error: SingleLiveEvent<String> = SingleLiveEvent()

    fun setLoading(message: String? = null) {
        isLoading.postValue(message != null)
    }

}