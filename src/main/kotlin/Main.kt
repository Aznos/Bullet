package com.aznos

import com.aznos.event.EventListener

fun main() {
    Server.setEventListener(object : EventListener {
        override fun onPongReceived(timestamp: Long) {
            println("I've received a pong packet with timestamp: $timestamp")
        }
    })
}