package com.aznos.packet.serverbound

import com.aznos.Server
import com.aznos.util.VarInt.readVarInt
import java.io.InputStream

/**
 * Represents login acknowledged packet (0x03)
 */
object LoginAcknowledgedPacket {
    fun read(input: InputStream): Boolean {
        val packetLen = input.readVarInt()
        val packetID = input.readVarInt()

        Server.logger.info("LoginAcknowledgedPacket received: packetID=$packetID, packetLen=$packetLen")
        return packetID == 0x03
    }
}