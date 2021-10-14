package com.sixt.sixtcar.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.sixt.sixtcar.R
import com.sixt.sixtcar.model.data.Vehicle
import com.sixt.sixtcar.utils.UiUtil
import com.sixt.sixtcar.view.base.BaseFragment
import com.sixt.sixtcar.viewmodel.VehicleSharedViewModel
import kotlinx.android.synthetic.main.fragment_vehicle_map.*
import timber.log.Timber


class VehicleMapFragment : BaseFragment(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    companion object {
        fun newInstance(): VehicleMapFragment {
            return VehicleMapFragment()
        }
    }
    private lateinit var viewModel: VehicleSharedViewModel
    private var gMap: GoogleMap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_vehicle_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(VehicleSharedViewModel::class.java)

        initMap()

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
                        plotPinOnMap(data)
                    }
                }
            })

            error.observe(viewLifecycleOwner, Observer {
                showErrorUI(true, it)
            })
        }
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
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

    private fun fetchData() {
        context?.let {
            viewModel?.fetchVehicleDetails(it)
        }
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    // ---------Google Map -------------------//

    private fun initMap() {
        mapView?.onCreate(Bundle())
        mapView?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        gMap?.clear()
        gMap?.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        Timber.i("onMarkerClick..")
        if (marker != null && marker.tag is Vehicle) {
            showVehicleDetailsDialog(marker.tag as Vehicle)
        }
        return false
    }

    private fun plotPinOnMap(data: List<Vehicle>) {
        Timber.i("plotPinOnMap..")
        val builder = LatLngBounds.Builder()
        var markerOp = MarkerOptions()

        data?.let {
            if (it.isNotEmpty()) {
                gMap?.let { map ->
                    map.clear()
                    for (item in it) {
                        var LatLng = LatLng(
                            item?.latitude ?: 0.0,
                            item?.longitude ?: 0.0
                        )
                        markerOp.position(LatLng)
                        val marker = map.addMarker(markerOp)
                        builder.include(marker?.position)
                        marker?.tag = item

                       val icon: BitmapDescriptor? = UiUtil.bitmapDescriptorFromVector(context, R.drawable.ic_car)
                        marker?.apply {
                            setIcon(icon)
                        }
                    }
                    setMarkerOnMap(builder, markerOp, data)
                }
            } else {
                gMap?.clear()
            }
        }
    }

    private fun setMarkerOnMap(builder: LatLngBounds.Builder, markerOptions: MarkerOptions, data: List<Vehicle>) {

        if (mapView?.visibility == View.VISIBLE && data.isNotEmpty()) {
            val marker = gMap?.addMarker(markerOptions)
            context?.let {
                marker?.setIcon(UiUtil.bitmapDescriptorFromVector(it, R.drawable.ic_car))
            }
            val bounds = builder.build()
            val padding = 100 // offset from edges of the map in pixels
            try {
                mapView.post {
                    val cu = CameraUpdateFactory.newLatLngBounds(
                        bounds,
                        mapView.width,
                        mapView.height,
                        padding
                    )
                    gMap?.moveCamera(cu)
                }
            } catch (e: NullPointerException) {
                Timber.e(e, "Map Not Ready")
            } catch (e: ApiException) {
                Timber.e(e, "Map Not Ready")
            } catch (e: Exception) {
                Timber.e(e, "Map Not Ready")
            }
        }
    }

}