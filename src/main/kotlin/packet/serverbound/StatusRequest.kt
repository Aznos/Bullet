package com.aznos.packet.serverbound

import com.aznos.Server
import com.aznos.event.EventManager
import com.aznos.event.PongEvent
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
                Server.logger.warn("Expected Status Request (0x00) but got packetID: $requestPacketID")
                return
            }

            StatusResponse.sendStatusResponse(output)

            val pingLength = input.readVarInt()
            val pingPacketID = input.readVarInt()
            if(pingPacketID != 0x01) {
                Server.logger.warn("Expected Ping Request (0x01) but got packet ID: $pingPacketID")
                return
            }

            val dataInput = DataInputStream(input)
            val timestamp = dataInput.readLong()

            EventManager.fireEvent(PongEvent(timestamp))

            PongResponse.sendPongResponse(output, timestamp)
        } catch(e: Exception) {
            Server.logger.warn("Error handling status for ${clientSocket.inetAddress.hostAddress}: ${e.message}")
        }
    }
}