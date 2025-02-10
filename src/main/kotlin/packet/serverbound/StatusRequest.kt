package com.aznos.packet.serverbound

import com.aznos.Server.eventListener
import com.aznos.packet.clientbound.PongResponse
import com.aznos.packet.clientbound.StatusResponse
import com.aznos.util.VarInt.readVarInt
import java.io.DataInputStream
import java.net.Socket

object StatusRequest {
    /**
     * Handles a status request from the client
     *
     * Reads a status request packet (0x00) then sends a status response
     * After reads a ping request packet (0x01) with a timestamp then sends a pong response
     *
     * @param clientSocket The [Socket] of the client
     */
    fun handleStatus(clientSocket: Socket) {
        try {
            val input = clientSocket.getInputStream()
            val output = clientSocket.getOutputStream()

            val requestLen = input.readVarInt()
            val requestPacketID = input.readVarInt()

            if(requestPacketID != 0x00) {
                println("Expected Status Request (0x00) but got packetID: $requestPacketID")
                return
            }

            println("Received Status Request from ${clientSocket.inetAddress.hostAddress}")
            StatusResponse.sendStatusResponse(output)
            println("Sent Status Response to ${clientSocket.inetAddress.hostAddress}")

            val pingLength = input.readVarInt()
            val pingPacketID = input.readVarInt()
            if(pingPacketID != 0x01) {
                println("Expected Ping Request (0x01) but got packet ID: $pingPacketID")
                return
            }

            val dataInput = DataInputStream(input)
            val timestamp = dataInput.readLong()
            println("Received Ping Request with timestamp: $timestamp from ${clientSocket.inetAddress.hostAddress}")

            eventListener.onPongReceived(timestamp)

            PongResponse.sendPongResponse(output, timestamp)
            println("Sent Pong Response to ${clientSocket.inetAddress.hostAddress}")
        } catch(e: Exception) {
            println("Error handling status for ${clientSocket.inetAddress.hostAddress}: ${e.message}")
        }
    }
}