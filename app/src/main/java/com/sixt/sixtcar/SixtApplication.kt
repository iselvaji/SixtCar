package com.sixt.sixtcar

import android.app.Application
import androidx.annotation.RequiresPermission
import com.facebook.stetho.Stetho
import com.github.anrwatchdog.ANRWatchDog
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.sixt.sixtcar.utils.LogUtil
import timber.log.Timber

class SixtApplication : Application() {

    private var defaultUEH: Thread.UncaughtExceptionHandler? =
        Thread.getDefaultUncaughtExceptionHandler()

    private val uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { thread, exception ->
        Timber.i("CRASH - ${LogUtil.getErrorMessage(exception)}")
        defaultUEH?.uncaughtException(thread, exception)
    }

    init {
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler)
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        initLogger()

        ANRWatchDog()
            .setANRListener { error ->
                if (error != null) {
                    FirebaseCrashlytics.getInstance().recordException(error)
                    FirebaseCrashlytics.getInstance().sendUnsentReports()
                    Timber.e(error)
                }
            }
            .setReportMainThreadOnly()
            .start()

        Stetho.initializeWithDefaults(this)
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    override fun onTerminate(){
        super.onTerminate()
    }

    // Timber logger
    private fun initLogger() {
        Timber.plant(Timber.DebugTree())
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Timber.i("!!!!!!!!!!!!!---LOW MEMORY---!!!!!!!!!!")
    }

}