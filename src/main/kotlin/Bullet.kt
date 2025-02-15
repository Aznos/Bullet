package com.aznos

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.*
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

fun main(): Unit = runBlocking(SupervisorJob()) {
    val logger: Logger = LogManager.getLogger("Bullet")

    System.setProperty(DEBUG_PROPERTY_NAME, DEBUG_PROPERTY_VALUE_ON)

    val server = aSocket(ActorSelectorManager(Dispatchers.IO))
        .tcp()
        .bind("0.0.0.0", 25565)

    while(true) {
        val socket = server.accept()

        launch(CoroutineName("session/${socket.remoteAddress}")) {
            logger.info("Accepted connection from ${socket.remoteAddress}")
        }
    }
}