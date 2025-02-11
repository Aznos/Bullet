package com.aznos.event.impl

import com.aznos.event.Event
import java.util.*

/**
 * Event fired as soon as the server accepts the connection request
 *
 * @property playerName The display name of the player
 * @property playerUUID The uuid of the player
 * @see Event
 */
data class PlayerLoginEvent(val playerName: String, val playerUUID: UUID) : Event