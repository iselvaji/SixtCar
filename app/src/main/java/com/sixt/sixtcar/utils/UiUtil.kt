package com.sixt.sixtcar.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.sixt.sixtcar.R
import timber.log.Timber

class UiUtil {

    companion object {
        private var progressDialog: Dialog? = null

        fun showDialog(activity: Activity?, show: Boolean) {
            if (show) {
                if (!(progressDialog != null && progressDialog?.isShowing == true)) {
                    activity?.let {
                        progressDialog = Dialog(activity)
                    }
                    progressDialog?.setCancelable(false)
                    progressDialog?.setContentView(R.layout.layout_progress_bar)
                    try {
                        progressDialog?.show()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } else {
                try {
                    progressDialog?.let {
                        if (it.isShowing) {
                            it.dismiss()
                        }
                    }
                } catch (e: IllegalArgumentException) {
                    Timber.e(e, "Exception while showing standard progress dialog")
                } finally {
                    progressDialog = null
                }
            }
        }

        fun bitmapDescriptorFromVector(
            context: Context?,
            vectorResId: Int
        ): BitmapDescriptor? {
            context?.let {
                val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
                vectorDrawable!!.setBounds(
                    0,
                    0,
                    vectorDrawable.intrinsicWidth,
                    vectorDrawable.intrinsicHeight
                )
                val bitmap = Bitmap.createBitmap(
                    vectorDrawable.intrinsicWidth,
                    vectorDrawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                vectorDrawable.draw(canvas)
                return BitmapDescriptorFactory.fromBitmap(bitmap)
            }
            return null
        }
    }
}