package com.sixt.sixtcar.utils

object LogUtil {

    fun getErrorMessage(e: Throwable): String? {
        var stackTrackElementArray = e.stackTrace
        var crashLog = e.toString() + "\n\n"
        crashLog += "--------- Stack trace ---------\n\n"
        for (i in stackTrackElementArray.indices) {
            crashLog += "    " + stackTrackElementArray[i].toString() + "\n"
        }
        crashLog += "-------------------------------\n\n"
        crashLog += "--------- Cause ---------\n\n"
        val cause = e.cause
        if (cause != null) {
            crashLog += cause.toString() + "\n\n"
            stackTrackElementArray = cause.stackTrace
            for (i in stackTrackElementArray.indices) {
                crashLog += (
                        "    " + stackTrackElementArray[i].toString() +
                                "\n"
                        )
            }
        }
        crashLog += "-------------------------------\n\n"
        return crashLog
    }
}