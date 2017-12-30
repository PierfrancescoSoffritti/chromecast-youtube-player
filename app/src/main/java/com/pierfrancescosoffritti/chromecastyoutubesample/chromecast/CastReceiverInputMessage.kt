package com.pierfrancescosoffritti.chromecastyoutubesample.chromecast

/**
 * Message received from a cast receiver
 */
data class CastReceiverInputMessage(val type: String, val data: String)