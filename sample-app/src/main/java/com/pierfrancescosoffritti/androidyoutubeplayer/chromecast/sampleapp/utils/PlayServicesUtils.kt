package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.utils

import android.app.Activity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

object PlayServicesUtils {
    fun checkGooglePlayServicesAvailability(activity: Activity, googlePlayServicesAvailabilityRequestCode: Int, onSuccess: () -> Unit) {
        val googlePlayServicesAvailabilityResult = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity)
        if(googlePlayServicesAvailabilityResult == ConnectionResult.SUCCESS)
            onSuccess()
        else
            GoogleApiAvailability.getInstance().getErrorDialog(activity, googlePlayServicesAvailabilityResult, googlePlayServicesAvailabilityRequestCode, null).show()
    }
}