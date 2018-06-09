package com.pierfrancescosoffritti.chromecastyoutubesample.chromecast

interface ChromecastConnectionListener {
    fun onChromecastConnecting()
    fun onChromecastConnected(chromecastCommunicationChannel: ChromecastCommunicationChannel)
    fun onChromecastDisconnected()
}