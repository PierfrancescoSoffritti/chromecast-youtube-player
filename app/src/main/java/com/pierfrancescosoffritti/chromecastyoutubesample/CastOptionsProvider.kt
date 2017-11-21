package com.pierfrancescosoffritti.chromecastyoutubesample

import android.content.Context
import com.google.android.gms.cast.framework.CastOptions
import com.google.android.gms.cast.framework.SessionProvider
import com.google.android.gms.cast.framework.OptionsProvider


internal class CastOptionsProvider : OptionsProvider {
    private val APP_ID = "C5CBE8CA";

    override fun getCastOptions(appContext: Context): CastOptions {
        return CastOptions.Builder()
                .setReceiverApplicationId(APP_ID)
                .build()
    }

    override fun getAdditionalSessionProviders(context: Context): List<SessionProvider>? {
        return null
    }
}