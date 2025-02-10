package com.aznos.event.impl

import com.aznos.event.Event

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
    var motd: String = "§6Bullet"
) : Event