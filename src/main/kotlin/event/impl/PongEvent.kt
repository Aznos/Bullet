package com.aznos.event.impl

import com.aznos.event.Event

/**
 * Event fired when a Pong request is received
 *
 * @property timestamp The timestamp of the request
 * @see Event
 */
data class PongEvent(val timestamp: Long) : Event