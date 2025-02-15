package com.aznos.packets

import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.datatypes.VarInt.writeVarInt
import io.ktor.network.sockets.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import packets.Packet
import packets.PacketRegistry

data class Session(private val socket: Socket) {
    private val input = socket.openReadChannel()
    private val output = socket.openWriteChannel(autoFlush = true)

    suspend fun sendPacket(packet: Packet) {
        val data = packet.serialize()
        val packetSize = buildPacket {
            writeVarInt(data.size)
        }.readBytes()

        withContext(Dispatchers.IO) {
            output.writeFully(packetSize, 0, packetSize.size)
            output.writeFully(data, 0, data.size)
        }
    }

    suspend fun readPacket(): Packet? {
        return withContext(Dispatchers.IO) {
            val packetSize = input.readVarInt()
            val packetData = input.readPacket(packetSize.toInt())
            val packetID = packetData.readVarInt().toInt()

            PacketRegistry.getPacket(packetID, packetData)
        }
    }
}