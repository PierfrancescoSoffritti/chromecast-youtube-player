package com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.castIO

/**
 * POJO for message received from the cast receiver
 */
data class MessageFromReceiver(val type: String, val data: String)