package com.aznos.event

/**
 * Defines events for the Bullet server framework
 */
interface EventListener {
    /**
     * Called when a Pong request is received
     *
     * @param timestamp The timestamp echoed in the ping request
     */
    fun onPongReceived(timestamp: Long)
}