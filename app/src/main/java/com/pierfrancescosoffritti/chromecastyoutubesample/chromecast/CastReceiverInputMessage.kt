package com.pierfrancescosoffritti.chromecastyoutubesample.chromecast

// input message from a cast receiver
data class CastReceiverInputMessage(val type: String, val data: String)