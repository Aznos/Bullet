package com.aznos

import com.aznos.event.EventManager
import com.aznos.event.impl.StatusResponseEvent
import com.aznos.util.Color

fun main() {
    EventManager.registerListener<StatusResponseEvent> { e ->
        e.maxPlayers = 25
        e.motd = Color.colorize("&hex(#ecc452)Bold &lText &hex(#ecde52)Underlined &nText")
    }

    Server.start()
}