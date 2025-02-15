package com.aznos

import com.aznos.datatypes.VarInt
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.datatypes.VarInt.writeVarInt
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

fun main(): Unit = runBlocking(SupervisorJob()) {
    val logger: Logger = LogManager.getLogger("Bullet")

    System.setProperty(DEBUG_PROPERTY_NAME, DEBUG_PROPERTY_VALUE_ON)

    val server = aSocket(ActorSelectorManager(Dispatchers.IO))
        .tcp()
        .bind("0.0.0.0", 25565)

    logger.info("Bullet server started at ${server.localAddress}")

    val packet = buildPacket {
        writeVarInt(300)
    }
    val readVarInt = packet.readVarInt()
    println("Decoded VarInt: ${readVarInt.toInt()}")

    while(true) {
        val socket = server.accept()

        launch(CoroutineName("session/${socket.remoteAddress}")) {
            logger.info("Accepted connection from ${socket.remoteAddress}")
        }
    }
}