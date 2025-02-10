package com.aznos

import com.aznos.event.EventManager
import com.aznos.event.impl.StatusResponseEvent

fun main() {
    EventManager.registerListener<StatusResponseEvent> { e ->
        e.maxPlayers = 25
        e.motd = "server!"
        e.onlinePlayers = 10
    }

    Server.start()
}