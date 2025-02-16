package com.aznos

import Logger.logger
import PacketRegistry
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.*
import packets.Session
import packets.SessionState
import packets.clientbound.ConfigurationDisconnectPacket
import packets.clientbound.LoginSuccessPacket
import packets.clientbound.PongResponsePacket
import packets.clientbound.StatusResponsePacket
import packets.serverbound.*

fun main(): Unit = runBlocking(SupervisorJob()) {
    val selectorManager = ActorSelectorManager(Dispatchers.IO)
    val server = aSocket(selectorManager)
        .tcp()
        .bind("0.0.0.0", 25565)

    logger.info("Bullet server started at ${server.localAddress}")
    registerPackets()

    while(true) {
        acceptAndHandleConnections(server)
    }
}

private suspend fun acceptAndHandleConnections(server: ServerSocket) {
    val socket = server.accept()
    logger.info("Accepted connection from ${socket.remoteAddress}")

    CoroutineScope(Dispatchers.IO).launch(CoroutineName("session/${socket.remoteAddress}")) {
        handleSession(socket)
    }
}

private suspend fun handleSession(socket: Socket) {
    val session = Session(socket)
    try {
        val handshake = session.readPacket() as? HandshakePacket
        if(handshake == null) {
            logger.warn("Expected HandshakePacket, closing session")
            return
        }

        if(handshake.nextState == 1) {
            session.setState(SessionState.STATUS)
            processStatus(session)
        } else if(handshake.nextState == 2) {
            session.setState(SessionState.LOGIN_START)
            handleLogin(session)
        }
    } catch(e: Exception) {
        logger.warn("Unexpected error: ${e.message}")
    } finally {
        session.close()
    }
}

private suspend fun processStatus(session: Session) {
    when(val packet = session.readPacket()) {
        is StatusRequestPacket -> {
            session.sendPacket(StatusResponsePacket.createDefault())
            when(val pingPacket = session.readPacket()) {
                is PingRequestPacket -> session.sendPacket(PongResponsePacket(payload = pingPacket.payload))
                else -> logger.warn("Expected PingRequestPacked, received: ${pingPacket?.javaClass?.simpleName}")
            }
        } else -> logger.warn("Expected StatusRequestPacket, received: ${packet?.javaClass?.simpleName}")
    }
}

private suspend fun handleLogin(session: Session) {
    when(val packet = session.readPacket()) {
        is LoginStartPacket -> {
            session.sendPacket(LoginSuccessPacket.createDefault())
            session.setState(SessionState.LOGIN_ACKNOWLEDGED)
            when(val acknowledgedPacket = session.readPacket()) {
                is LoginAcknowledgedPacket -> {
                    logger.info("Client acknowledged login")
                    session.sendPacket(ConfigurationDisconnectPacket(reason = "Work in progress"))
                    delay(500)
                    session.close()
                }
                else -> logger.warn("Expected LoginAcknowledgedPacket, received: ${acknowledgedPacket?.javaClass?.simpleName}")
            }
        } else -> logger.warn("Expected LoginStartPacket, received: ${packet?.javaClass?.simpleName}")
    }
}

private fun registerPackets() {
    listOf(
        Triple(0x00, SessionState.HANDSHAKE, HandshakePacket.Companion::deserialize),
        Triple(0x00, SessionState.STATUS, StatusRequestPacket.Companion::deserialize),
        Triple(0x01, SessionState.STATUS, PingRequestPacket.Companion::deserialize),
        Triple(0x00, SessionState.LOGIN_START, LoginStartPacket.Companion::deserialize),
        Triple(0x03, SessionState.LOGIN_ACKNOWLEDGED, LoginAcknowledgedPacket.Companion::deserialize)
    ).forEach { (id, state, deserializer) ->
        PacketRegistry.register(id, state, deserializer)
    }
}