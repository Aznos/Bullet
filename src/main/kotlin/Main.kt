package com.aznos

import com.aznos.event.EventListener

fun main() {
    Server.setEventListener(object : EventListener {
        override fun onPongReceived(timestamp: Long) {
            Server.logger.info("Pong request received with timestamp: $timestamp")
        }
    })

    Server.start()
}