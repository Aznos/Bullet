package com.aznos.event.impl

import com.aznos.event.Event
import com.aznos.util.Color

/**
 * Event fired when a Pong request is received
 *
 * @property timestamp The timestamp of the request
 * @see Event
 */
data class StatusResponseEvent(
    var version: String = "1.21.4",
    var protocol: Int = 769,
    var maxPlayers: Int = 100,
    var onlinePlayers: Int = 0,
    var motd: String = "${Color.GOLD}A server that runs as fast as a Bullet"
) : Event