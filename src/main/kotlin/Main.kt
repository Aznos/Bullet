package com.aznos

import com.aznos.event.EventManager
import com.aznos.event.impl.StatusResponseEvent

fun main() {
    EventManager.registerListener<StatusResponseEvent> { e ->
        e.maxPlayers = 25
        e.motd = "This is a long motd!"
    }

    Server.start()
}