package com.aznos

import com.aznos.event.EventListener
import com.aznos.packet.serverbound.StatusRequest
import com.aznos.protocol.readHandshakePacket
import kotlinx.coroutines.*
import java.net.ServerSocket
import java.net.Socket

object Server {
    /**
     * Event listener for bullet events
     */
    var eventListener: EventListener = object : EventListener {
        override fun onPongReceived(timestamp: Long) {}
    }

    /**
     * Sets custom event listener
     *
     * @param listener The custom [EventListener] to use
     */
    fun setEventListener(listener: EventListener) {
        eventListener = listener
    }

    /**
     * Start the server on the given [port]
     *
     * @param port The port to start the server on (default 25565)
     */
    fun start(port: Int = 25565) {
        val serverScope = CoroutineScope(Dispatchers.IO)
        val serverSocket = ServerSocket(port)
        println("Bullet server started on port $port")

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
        println("New connection from ${clientSocket.inetAddress.hostAddress}")
        try {
            val input = clientSocket.getInputStream()
            val handshake = input.readHandshakePacket()
            if(handshake != null) {
                println(
                    "Received handshake: ProtocolVersion=${handshake.protocolVersion}, " +
                    "ServerAddress='${handshake.serverAddress}', " +
                    "ServerPort=${handshake.serverPort}, NextState=${handshake.nextState}"
                )

                if(handshake.nextState == 1) {
                    StatusRequest.handleStatus(clientSocket)
                }
            } else {
                println("Received non-handshake packet from ${clientSocket.inetAddress.hostAddress}")
            }
        } catch(e: Exception) {
            println("Error handling client ${clientSocket.inetAddress.hostAddress}: ${e.message}")
        } finally {
            clientSocket.close()
            println("Closed connection with ${clientSocket.inetAddress.hostAddress}")
        }
    }
}