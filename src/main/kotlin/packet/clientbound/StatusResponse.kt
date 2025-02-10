package com.aznos.packet.clientbound

import com.aznos.packet.PacketWriter
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
        val jsonResponse =
            "{\"version\": {\"name\": \"Bullet 1.0\", \"protocol\": 761}, " +
            "\"players\": {\"max\": 100, \"online\": 0}, " +
            "\"description\": {\"text\": \"\"}}"

        val payloadBuffer = ByteArrayOutputStream()
        with(PacketWriter) {
            payloadBuffer.writeString(jsonResponse)
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