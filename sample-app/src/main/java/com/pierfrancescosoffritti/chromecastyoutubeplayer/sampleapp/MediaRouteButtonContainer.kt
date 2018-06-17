package com.pierfrancescosoffritti.chromecastyoutubeplayer.sampleapp

import android.support.v7.app.MediaRouteButton

interface MediaRouteButtonContainer {
    fun addMediaRouteButton(mediaRouteButton: MediaRouteButton)
    fun removeMediaRouteButton(mediaRouteButton: MediaRouteButton)
}