package com.aznos

import Logger.logger
import com.aznos.packets.Session
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.*
import packets.HandshakePacket

fun main(): Unit = runBlocking(SupervisorJob()) {
    System.setProperty(DEBUG_PROPERTY_NAME, DEBUG_PROPERTY_VALUE_ON)

    val server = aSocket(ActorSelectorManager(Dispatchers.IO))
        .tcp()
        .bind("0.0.0.0", 25565)

    logger.info("Bullet server started at ${server.localAddress}")
    registerPackets()

    while(true) {
        val socket = server.accept()

        launch(CoroutineName("session/${socket.remoteAddress}")) {
            logger.info("Accepted connection from ${socket.remoteAddress}")
            val session = Session(socket)
            val handshake = session.readPacket()
            println(handshake)
        }
    }
}

private fun registerPackets() {
    HandshakePacket.register()
}