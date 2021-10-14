package com.sixt.sixtcar.view.custom

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.sixt.sixtcar.R
import com.sixt.sixtcar.model.data.Vehicle
import kotlinx.android.synthetic.main.dialog_marker_information.*

class VehicleDetailsDialog : DialogFragment() {

    var data: Vehicle? = null
    var proceedClickListener: ProceedClickListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mDialog = super.onCreateDialog(savedInstanceState)
        mDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDialog.window?.attributes?.gravity = Gravity.BOTTOM
        return mDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context)
            .inflate(R.layout.dialog_marker_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpData(context)

        btnProceed.setOnClickListener {
            proceedClickListener?.onProceedTapped(data)
            dismiss()
        }

        btnClose.setOnClickListener {
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun setUpData(context: Context?) {

        data?.apply {
            tvVehicleName.text = name
            tvLicense.text = licensePlate
            tvGroup.text = group
            tvMake.text = make
            context?.let {
                Glide.with(it).
                load(carImageUrl).
                placeholder(R.drawable.ic_car).
                centerCrop().into(imgVehicle)
           }
        }
    }

    interface ProceedClickListener {
        fun onProceedTapped(vehicle: Vehicle?)
    }
}
