package com.aznos

import com.aznos.event.EventManager
import com.aznos.event.PongEvent

fun main() {
    EventManager.registerListener<PongEvent> { e ->
        println("Pong request received with timestamp: ${e.timestamp}")
    }

    Server.start()
}