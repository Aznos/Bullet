package com.aznos.event.impl

import com.aznos.event.Event

/**
 * Event fired as soon as the server accepts the connection request
 *
 * @property playerName The display name of the player
 * @see Event
 */
data class PlayerDisconnectEvent(val playerName: String) : Event