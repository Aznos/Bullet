package com.aznos.packets

import com.aznos.datatypes.VarInt
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.datatypes.VarInt.writeVarInt
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*

data class Session(val socket: io.ktor.network.sockets.Socket) {
    private val input = socket.openReadChannel()
    private val output = socket.openWriteChannel(autoFlush = true)

    suspend fun <A: Any> writePacket(packetID: Int, serializePacket: BytePacketBuilder.() -> Unit) {
        val packet = buildPacket {
            writeVarInt(packetID)
            serializePacket()
        }

        output.writeVarInt(VarInt.VarInt(packet.remaining.toInt()))
        output.writePacket(packet)
    }

    suspend fun <A: Any> receivePacket (deserializePacket: ByteReadPacket.() -> A,): A {
        val size = input.readVarInt()
        val packet = input.readPacket(size.toInt())
        val id = packet.readVarInt().toInt()

        return deserializePacket(packet)
    }
}