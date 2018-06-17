package com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.castIO

import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.ChromecastYouTubePlayerContext

/**
 * Implement this interface to be notified about changes in the cast connection.
 */
interface ChromecastConnectionListener {
    fun onChromecastConnecting()
    fun onChromecastConnected(chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext)
    fun onChromecastDisconnected()
}