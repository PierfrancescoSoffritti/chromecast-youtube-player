package com.pierfrancescosoffritti.chromecastyoutubesample.chromecast

/**
 * A ChromecastContainer is a class that "contains" the Chromecast button and UI. TODO: should find a better name.
 */
interface ChromecastContainer {
    fun onSessionResuming()
    fun onSessionStarting()
    fun onSessionEnding()
    fun setCommunicationChannel(communicationChannelChromecast: ChromecastCommunicationChannel)
}