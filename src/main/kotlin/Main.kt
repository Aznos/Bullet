package com.aznos

import com.aznos.event.EventManager
import com.aznos.event.impl.PlayerLoginEvent

fun main() {
    EventManager.registerListener<PlayerLoginEvent> { event ->
        Server.logger.info("${event.playerName} has joined the server!")
    }

    Server.start()
}