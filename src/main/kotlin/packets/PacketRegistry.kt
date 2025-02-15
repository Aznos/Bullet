import io.ktor.utils.io.core.*
import packets.ServerboundPacket
import packets.SessionState

object PacketRegistry {
    private val packetMap = mutableMapOf<Pair<Int, SessionState>, (ByteReadPacket) -> ServerboundPacket>()

    fun register(id: Int, state: SessionState, deserializer: (ByteReadPacket) -> ServerboundPacket) {
        packetMap[id to state] = deserializer
    }

    fun getPacket(id: Int, state: SessionState, data: ByteReadPacket): ServerboundPacket {
        return packetMap[id to state]?.invoke(data) ?: throw IllegalArgumentException("Unknown packet id: $id for state: $state")
    }
}