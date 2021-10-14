package com.sixt.sixtcar.view.base

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.sixt.sixtcar.model.data.Vehicle
import com.sixt.sixtcar.utils.ConnectionChangeListener
import com.sixt.sixtcar.utils.ConnectivityLiveData
import com.sixt.sixtcar.view.custom.VehicleDetailsDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_vehicle_map.*
import timber.log.Timber

abstract class BaseFragment : Fragment(), VehicleDetailsDialog.ProceedClickListener,
    ConnectionChangeListener {

    private lateinit var connectivityLiveData:ConnectivityLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("Entering Screen -> ${javaClass.simpleName}")

        connectivityLiveData = activity?.application?.let { ConnectivityLiveData(it) }!!

        connectivityLiveData.observe(this, Observer { isAvailable->
            onConnectionChanged(isAvailable)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("Exiting Screen -> ${javaClass.simpleName}")
    }

    protected fun showErrorUI(showError: Boolean, msg :String?) {
        msg?.let { Log.i("showErrorUI", it + showError) }
        if(showError) {
            requireActivity()?.apply {
                container?.visibility = View.GONE
                textError?.visibility = View.VISIBLE
                msg?.let { textError?.setText(msg) }
            }
            mapView?.visibility = View.GONE
           // Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        } else {
            requireActivity()?.apply {
                container?.visibility = View.VISIBLE
                textError?.visibility = View.GONE
            }
            mapView?.visibility = View.VISIBLE

        }
    }

    override fun onProceedTapped(vehicle: Vehicle?) {
        Toast.makeText(context, getString(com.sixt.sixtcar.R.string.proceed_msg), Toast.LENGTH_LONG).show()
    }

    fun showVehicleDetailsDialog(vehicle: Vehicle?) {
        Timber.i("ShowVehicleDetailsDialog..")
        val dialog = VehicleDetailsDialog()
        dialog.data = vehicle
        dialog.proceedClickListener = this
        dialog.show(
            childFragmentManager,
            VehicleDetailsDialog::class.java.simpleName
        )
    }
}