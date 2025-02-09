package com.aznos

import com.aznos.protocol.readHandshakePacket
import jdk.internal.org.jline.utils.Colors.s
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

object Server {
    /**
     * Start the server on the given [port]
     *
     * @param port The port to start the server on (default 25565)
     */
    fun start(port: Int = 25565) {
        val serverSocket = ServerSocket(port)
        println("Bullet server started on port $port")

        while(true) {
            val clientSocket = serverSocket.accept()
            thread(start = true) {
                handleClient(clientSocket)
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
                    "ServerPort=${handshake.serverPort}, " +
                    "NextState=${handshake.nextState}"
                )
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