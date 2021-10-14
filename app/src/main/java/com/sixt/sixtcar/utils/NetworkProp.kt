package com.sixt.sixtcar.utils

import android.util.Log
import kotlin.properties.Delegates

object NetworkProp {
    var isNetworkConnected: Boolean by Delegates.observable(false) { property, oldValue, newValue ->
        Log.i("Network connectivity", "$newValue")
    }
}