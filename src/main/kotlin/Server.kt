package com.aznos

import com.aznos.session.SessionManager
import kotlinx.coroutines.*
import java.net.ServerSocket
import java.net.Socket

object Server {
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
        val player = SessionManager.addPlayer(clientSocket)
        println("Player connected: ${player.name}")

        try {
            clientSocket.getInputStream().bufferedReader().use { reader ->
                val line = reader.readLine()
                println("Received from ${player.name}: $line")
            }
        } catch(e: Exception) {
            println("Error handling ${player.name}: ${e.message}")
        } finally {
            SessionManager.removePlayer(player.id)
            clientSocket.close()
            println("Connection closed for ${player.name}")
        }
    }
}