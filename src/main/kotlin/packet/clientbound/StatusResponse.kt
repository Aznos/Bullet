package com.aznos.packet.clientbound

import com.aznos.event.EventManager
import com.aznos.event.impl.StatusResponseEvent
import com.aznos.packet.PacketWriter
import com.google.gson.JsonObject
import java.io.ByteArrayOutputStream
import java.io.OutputStream

object StatusResponse {
    /**
     * Sends a status response packet to the client.
     *
     * Packet structure (Clientbound, Status Response, Packet ID 0x00)
     *  - JSON Response (String): Json response containing server information
     *
     *  @param output The [OutputStream] to write the packet to
     */
    fun sendStatusResponse(output: OutputStream) {
        val packetID = 0x00
        val event = StatusResponseEvent()
        EventManager.fireEvent(event)

        val jsonResponse = JsonObject().apply {
            add("version", JsonObject().apply {
                addProperty("name", event.version)
                addProperty("protocol", event.protocol)
            })
            add("players", JsonObject().apply {
                addProperty("max", event.maxPlayers)
                addProperty("online", event.onlinePlayers)
            })
            add("description", JsonObject().apply {
                addProperty("text", event.motd)
            })
        }

        val payloadBuffer = ByteArrayOutputStream()
        with(PacketWriter) {
            payloadBuffer.writeString(jsonResponse.toString())
        }

        val payload = payloadBuffer.toByteArray()
        val idBuffer = ByteArrayOutputStream()
        with(PacketWriter) {
            idBuffer.writeVarInt(packetID)
        }

        val idBytes = idBuffer.toByteArray()
        val fullLen = idBytes.size + payload.size
        with(PacketWriter) {
            output.writeVarInt(fullLen)
            output.write(idBytes)
        }

        output.write(payload)
        output.flush()
    }
}