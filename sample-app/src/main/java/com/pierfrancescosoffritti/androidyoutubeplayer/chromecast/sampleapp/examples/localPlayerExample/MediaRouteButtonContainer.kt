package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.examples.localPlayerExample

import android.support.v7.app.MediaRouteButton

interface MediaRouteButtonContainer {
    fun addMediaRouteButton(mediaRouteButton: MediaRouteButton)
    fun removeMediaRouteButton(mediaRouteButton: MediaRouteButton)
}