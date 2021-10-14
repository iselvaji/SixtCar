package com.sixt.sixtcar.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sixt.sixtcar.R
import com.sixt.sixtcar.model.data.Vehicle
import com.sixt.sixtcar.utils.UiUtil
import com.sixt.sixtcar.view.adapter.VehicleAdapter
import com.sixt.sixtcar.view.base.BaseFragment
import com.sixt.sixtcar.viewmodel.VehicleSharedViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_vehicle_list.*
import timber.log.Timber

class VehicleListFragment : BaseFragment(), VehicleAdapter.CellClickListener {

    companion object {
        fun newInstance(): VehicleListFragment {
            return VehicleListFragment()
        }
    }
    private lateinit var viewModel: VehicleSharedViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_vehicle_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(VehicleSharedViewModel::class.java)

        fetchData()

        viewModel?.apply {
            isLoading.observe(viewLifecycleOwner, Observer {
                UiUtil.showDialog(activity, it)
            })

            vehicleResponse.observe(viewLifecycleOwner, Observer { data ->
                data?.let {
                    showErrorUI(false, null)
                    Timber.i("Vehicle response list size : %s", data.size)
                    if (data?.size == 0) {
                        showErrorUI(true, getString(R.string.error_empty_data))
                    } else {
                        recyclerVehicle?.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = VehicleAdapter(data, this@VehicleListFragment)
                        }
                    }
                }
            })

            error.observe(viewLifecycleOwner, Observer {
               showErrorUI(true, it)
            })
        }
    }

    private fun fetchData() {
        context?.let {
            viewModel?.fetchVehicleDetails(it)
        }
    }

    override fun onConnectionChanged(isConnected: Boolean) {
        Timber.i("onConnectionChanged - isConnected %s", isConnected)
        when(isConnected) {
           true -> {
               fetchData()
           }
           false -> {
               showErrorUI(true, getString(R.string.error_network))
           }
       }
    }

    override fun onCellClickListener(vehicle: Vehicle) {
        showVehicleDetailsDialog(vehicle)
    }

}