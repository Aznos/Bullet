package com.aznos

import com.aznos.packet.serverbound.StatusRequest
import com.aznos.protocol.readHandshakePacket
import kotlinx.coroutines.*
import mu.KotlinLogging
import java.net.ServerSocket
import java.net.Socket

object Server {
    val logger = KotlinLogging.logger("Bullet")

    /**
     * Start the server on the given [port]
     *
     * @param port The port to start the server on (default 25565)
     */
    fun start(port: Int = 25565) {
        val serverScope = CoroutineScope(Dispatchers.IO)
        val serverSocket = ServerSocket(port)
        logger.info("Bullet server started on port $port")

        //Check for client connections
        serverScope.launch {
            while(isActive) {
                val clientSocket: Socket = withContext(Dispatchers.IO) { serverSocket.accept() }
                launch(Dispatchers.IO) { handleClient(clientSocket) }
            }
        }

        //Keep the server running
        runBlocking {
            while(true) {
                delay(1000)
            }
        }
    }

    /**
     * Handles individual client connections
     * Reads the handshake packet from the client and logs its details
     *
     * @param clientSocket The [Socket] representing the client connection
     */
    private fun handleClient(clientSocket: Socket) {
        try {
            val input = clientSocket.getInputStream()
            val handshake = input.readHandshakePacket()
            if(handshake != null) {
                if(handshake.nextState == 1) {
                    StatusRequest.handleStatus(clientSocket)
                }
            }
        } catch(e: Exception) {
            logger.warn("Error handling client ${clientSocket.inetAddress.hostAddress}: ${e.message}")
        } finally {
            clientSocket.close()
        }
    }
}