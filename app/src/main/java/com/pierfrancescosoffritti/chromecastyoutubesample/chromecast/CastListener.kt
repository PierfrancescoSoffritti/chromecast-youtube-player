package com.pierfrancescosoffritti.chromecastyoutubesample.chromecast

import com.pierfrancescosoffritti.chromecastyoutubesample.youTube.chromecast.ChromecastYouTubeIOChannel

interface CastListener {
    fun onSessionStarting()
    fun onSessionEnding()
    fun setCommunicationChannel(communicationChannelChromecast: ChromecastYouTubeIOChannel)
}