package com.aznos.event

/**
 * Event fired when a Pong request is received
 *
 * @property timestamp The timestamp of the request
 */
data class PongEvent(val timestamp: Long) : Event