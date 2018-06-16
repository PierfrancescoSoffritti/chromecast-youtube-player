package com.pierfrancescosoffritti.chromecastyoutubesample.chromecast


class MessageToReceiver private constructor() {

//    override fun toString(): String {
//        return message.toString()
//    }
//
//    companion object Builder {
//        private val COMMAND = "command"
//
//        fun build(command: String): MessageToReceiver {
//            val message: JsonObject = jsonObject(COMMAND to command)
//
//            return MessageToReceiver(message)
//        }
//
//        fun build(command: String, vararg arguments: Pair<String, JsonObject>): MessageToReceiver {
//            val message: JsonObject = jsonObject(COMMAND to command)
//            arguments.forEach { message.addProperty(it.first, it.second) }
//
//            return MessageToReceiver(message)
//        }
//
//        fun build(command: String, vararg arguments: String): MessageToReceiver {
//            val message: JsonObject = jsonObject(COMMAND to command)
//            arguments.forEach { message.addProperty(it, it) }
//
//            return MessageToReceiver(message)
//        }
//    }
}