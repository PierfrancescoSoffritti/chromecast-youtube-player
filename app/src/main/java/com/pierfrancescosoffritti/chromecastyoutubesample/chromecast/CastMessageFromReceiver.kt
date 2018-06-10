package com.pierfrancescosoffritti.chromecastyoutubesample.chromecast

/**
 * POJO of message received from a cast receiver
 */
data class CastMessageFromReceiver(val type: String, val data: String)