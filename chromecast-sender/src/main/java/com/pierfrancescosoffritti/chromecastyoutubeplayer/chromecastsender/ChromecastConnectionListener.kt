package com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender

import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.ChromecastCommunicationChannel

interface ChromecastConnectionListener {
    fun onChromecastConnecting()
    fun onChromecastConnected(chromecastCommunicationChannel: ChromecastCommunicationChannel)
    fun onChromecastDisconnected()
}