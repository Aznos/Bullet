package packets

import io.ktor.utils.io.core.*

object PacketRegistry {
    private val packetMap = mutableMapOf<Int, (ByteReadPacket) -> Packet>()

    fun register(id: Int, deserializer: (ByteReadPacket) -> Packet) {
        packetMap[id] = deserializer
    }

    fun getPacket(id: Int, data: ByteReadPacket): Packet {
        return packetMap[id]?.invoke(data) ?: throw IllegalArgumentException("Unknown packet id: $id")
    }
}