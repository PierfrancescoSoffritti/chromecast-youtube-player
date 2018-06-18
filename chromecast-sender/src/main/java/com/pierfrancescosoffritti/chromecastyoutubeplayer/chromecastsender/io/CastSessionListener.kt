package com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.io

import com.google.android.gms.cast.framework.CastSession

interface CastSessionListener {
    fun onCastSessionConnecting()
    fun onCastSessionConnected(castSession: CastSession)
    fun onCastSessionDisconnected(castSession: CastSession)
}