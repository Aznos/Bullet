package com.aznos.packet.clientbound

import com.aznos.event.EventManager
import com.aznos.event.impl.PlayerLoginEvent
import com.aznos.packet.PacketWriter
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.util.*

/**
 * Sends the login success packet (0x02)
 *
 * Packet Structure:
 *  - Packet ID (VarInt) (0x02)
 *  - UUID (String)
 *  - Username (String)
 *  - Properties (Empty Array)
 */
object LoginSuccessPacket {
    fun send(output: OutputStream, username: String) {
        val packetID = 0x02
        val uuid = UUID.randomUUID()

        val payloadBuffer = ByteArrayOutputStream()
        with(PacketWriter) {
            payloadBuffer.writeLong(uuid.mostSignificantBits)
            payloadBuffer.writeLong(uuid.leastSignificantBits)
            payloadBuffer.writeString(username)
            payloadBuffer.writeVarInt(0)
        }

        val payload = payloadBuffer.toByteArray()
        with(PacketWriter) {
            output.writeVarInt(payload.size + 1)
            output.writeVarInt(packetID)
        }

        EventManager.fireEvent(PlayerLoginEvent(username, uuid))

        output.write(payload)
        output.flush()
    }
}