package com.aznos.packet.clientbound

import com.aznos.packet.PacketWriter
import java.io.ByteArrayOutputStream
import java.io.OutputStream

object PongResponse {
    /**
     * Sends a pong response packet to the client.
     *
     * Packet structure (Clientbound, Pong, Packet ID 0x01)
     *  - Timestamp (Long): The same timestamp received in the Ping Request
     *
     *  @param output The [OutputStream] to write the packet to
     *  @param timestamp The timestamp to echo back
     */
    fun sendPongResponse(output: OutputStream, timestamp: Long) {
        val packetID = 0x01
        val payloadBuffer = ByteArrayOutputStream()
        with(PacketWriter) {
            payloadBuffer.writeLong(timestamp)
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