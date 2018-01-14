package com.pierfrancescosoffritti.chromecastyoutubesample.chromecast

interface ChromecastConnectionListener {
    fun onApplicationConnecting()
    fun onApplicationConnected(chromecastCommunicationChannel: ChromecastCommunicationChannel)
    fun onApplicationDisconnected()
}